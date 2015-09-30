package com.ciandt.techgallery.service;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.googlecode.objectify.Ref;

// github.com/ciandt-dev/tech-gallery.git

import com.ciandt.techgallery.persistence.dao.TechGalleryUserDAO;
import com.ciandt.techgallery.persistence.dao.TechGalleryUserDAOImpl;
import com.ciandt.techgallery.persistence.dao.TechnologyDAO;
import com.ciandt.techgallery.persistence.dao.TechnologyDAOImpl;
import com.ciandt.techgallery.persistence.dao.TechnologyDetailsCounterDAO;
import com.ciandt.techgallery.persistence.dao.TechnologyDetailsCounterDAOImpl;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.persistence.model.counter.TechnologyDetailsCounter;
import com.ciandt.techgallery.service.enums.TechnologyOrderOptionEnum;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.TechnologiesResponse;
import com.ciandt.techgallery.service.model.TechnologyFilter;
import com.ciandt.techgallery.service.model.TechnologyResponse;
import com.ciandt.techgallery.service.util.TechnologyConverter;
import com.ciandt.techgallery.utils.i18n.I18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Services for Technology Endpoint requests.
 * 
 * @author felipers
 *
 */
public class TechnologyServiceImpl implements TechnologyService {

  private static final String NO_TECHNOLOGY_WAS_FOUND = "No technology was found.";
  private static final String TECHNOLOGY_ID_CANNOT_BE_BLANK = "Technology's id cannot be blank.";
  
  private static final I18n i18n = I18n.getInstance();
  TechGalleryUserDAO techGalleryUserDAO = new TechGalleryUserDAOImpl();
  TechnologyDAO technologyDAO = new TechnologyDAOImpl();
  TechnologyDetailsCounterDAO technologyDetailsCounterDao =
      TechnologyDetailsCounterDAOImpl.getInstance();

  /**
   * POST for adding a technology.
   */
  @Override
  public Response addTechnology(final TechnologyResponse technology)
      throws InternalServerErrorException, BadRequestException {
    String techId = technology.getId();
    String techName = technology.getName();

    // technology id can't be null or empty
    if (techId == null || techId.equals("")) {
      throw new BadRequestException(i18n.t(TECHNOLOGY_ID_CANNOT_BE_BLANK));
    }
    // technology name can't be null or empty
    if (techName == null || techName.equals("")) {
      throw new BadRequestException(i18n.t(TECHNOLOGY_ID_CANNOT_BE_BLANK));
    } else {
      Technology entity = TechnologyConverter.fromTransientToEntity(technology);
      technologyDAO.add(entity);
      TechnologyDetailsCounter techDetails = new TechnologyDetailsCounter();
      techDetails.setTechnology(Ref.create(technologyDAO.findById(entity.getId())));
      technologyDetailsCounterDao.add(techDetails);

      // set the id and return it
      technology.setId(entity.getId());
      return technology;
    }
  }

  /**
   * GET for getting all technologies.
   */
  @Override
  public Response getTechnologies() throws InternalServerErrorException, NotFoundException {
    List<Technology> techEntities = technologyDAO.findAll();
    // if list is null, return a not found exception
    if (techEntities == null) {
      throw new NotFoundException(i18n.t(NO_TECHNOLOGY_WAS_FOUND));
    } else {
      TechnologiesResponse response = new TechnologiesResponse();
      List<TechnologyResponse> internList = TechnologyConverter.fromEntityToTransient(techEntities);
      response.setTechnologies(internList);
      return response;
    }
  }

  private List<TechnologyDetailsCounter> sortTechnologies(List<Technology> techEntities,
      TechnologyOrderOptionEnum orderBy) {
    List<TechnologyDetailsCounter> counterList = new ArrayList<TechnologyDetailsCounter>();
    for (Technology technology : techEntities) {
      counterList.add(technologyDetailsCounterDao.findByTechnology(technology));
    }
    switch (orderBy) {
      case POSITIVE_RECOMENDATION_QUANTITY:
        Collections.sort(counterList, new Comparator<TechnologyDetailsCounter>() {
          @Override
          public int compare(TechnologyDetailsCounter counter1, TechnologyDetailsCounter counter2) {
            return Integer.compare(counter2.getPositiveRecomendationsCounter(),
                counter1.getPositiveRecomendationsCounter());
          }
        });
        break;
      case NEGATIVE_RECOMENDATION_QUANTITY:
        Collections.sort(counterList, new Comparator<TechnologyDetailsCounter>() {
          @Override
          public int compare(TechnologyDetailsCounter counter1, TechnologyDetailsCounter counter2) {
            return Integer.compare(counter2.getNegativeRecomendationsCounter(),
                counter1.getNegativeRecomendationsCounter());
          }
        });
        break;
      case COMENTARY_QUANTITY:
        Collections.sort(counterList, new Comparator<TechnologyDetailsCounter>() {
          @Override
          public int compare(TechnologyDetailsCounter counter1, TechnologyDetailsCounter counter2) {
            return Integer.compare(counter2.getCommentariesCounter(),
                counter1.getCommentariesCounter());
          }
        });
        break;
      case ENDORSEMENT_QUANTITY:
        Collections.sort(counterList, new Comparator<TechnologyDetailsCounter>() {
          @Override
          public int compare(TechnologyDetailsCounter counter1, TechnologyDetailsCounter counter2) {
            return Integer.compare(counter2.getEndorsedsCounter(), counter1.getEndorsedsCounter());
          }
        });
        break;
      default:
        break;
    }
    return counterList;
  }

  /**
   * GET for getting one technology.
   */
  @Override
  public Response getTechnology(final String id) throws NotFoundException {
    Technology techEntity = technologyDAO.findById(id);
    // if technology is null, return a not found exception
    if (techEntity == null) {
      throw new NotFoundException(i18n.t(NO_TECHNOLOGY_WAS_FOUND));
    } else {
      TechnologyResponse response = TechnologyConverter.fromEntityToTransient(techEntity);
      return response;
    }
  }

  @Override
  public Response findTechnologiesByFilter(TechnologyFilter techFilter, User user)
      throws InternalServerErrorException, NotFoundException, BadRequestException {

    validateUser(user);
    List<Technology> completeList = technologyDAO.findAll();
    List<Technology> filteredList = new ArrayList<>();
    if (techFilter.getTitleContains() == null || techFilter.getTitleContains().isEmpty()) {
      filteredList.addAll(completeList);
    } else {
      for (Technology technology : completeList) {
        if (technology.getName().toLowerCase().contains(techFilter.getTitleContains().toLowerCase())
            || technology.getShortDescription().toLowerCase()
                .contains(techFilter.getShortDescriptionContains().toLowerCase())) {
          filteredList.add(technology);
        }
      }
    }
    List<TechnologyResponse> internList = TechnologyConverter.fromEntityToTransient(filteredList);
    if (!filteredList.isEmpty() && techFilter.getOrderOption() != null
        && !techFilter.getOrderOption().isEmpty()) {
      TechnologyOrderOptionEnum orderBy =
          TechnologyOrderOptionEnum.fromString(techFilter.getOrderOption());
      List<TechnologyDetailsCounter> technologiesDetailList =
          sortTechnologies(filteredList, orderBy);
      List<Technology> sortedTechnologies = new ArrayList<Technology>();
      for (TechnologyDetailsCounter detail : technologiesDetailList) {
        sortedTechnologies.add(detail.getTechnology().get());
      }

      internList = TechnologyConverter.fromEntityToTransient(sortedTechnologies);
    }
    TechnologiesResponse response = new TechnologiesResponse();
    response.setTechnologies(internList);
    return response;
  }

  @Override
  public Technology getTechnologyById(String id) throws NotFoundException {
    Technology tech = technologyDAO.findById(id);
    if (tech == null) {
      throw new NotFoundException(ValidationMessageEnums.TECHNOLOGY_NOT_EXIST.message());
    } else {
      return tech;
    }

  }

  /**
   * Validate the user logged in.
   * 
   * @param user info about user from google
   * @throws BadRequestException .
   */
  private void validateUser(User user) throws BadRequestException {

    if (user == null || user.getUserId() == null || user.getUserId().isEmpty()) {
      throw new BadRequestException(ValidationMessageEnums.USER_GOOGLE_ENDPOINT_NULL.message());
    }

    TechGalleryUser techUser = techGalleryUserDAO.findByGoogleId(user.getUserId());
    if (techUser == null) {
      throw new BadRequestException(ValidationMessageEnums.USER_NOT_EXIST.message());
    }
  }

  @Override
  public List<String> getOrderOptions(User user) {
    List<String> orderOptions = new ArrayList<String>();
    for (TechnologyOrderOptionEnum item : TechnologyOrderOptionEnum.values()) {
      orderOptions.add(item.option());
    }
    return orderOptions;
  }
}
