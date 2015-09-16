package com.ciandt.techgallery.service;

import java.util.ArrayList;
import java.util.List;

import com.ciandt.techgallery.persistence.dao.TechnologyDAO;
import com.ciandt.techgallery.persistence.dao.TechnologyDAOImpl;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.TechnologiesResponse;
import com.ciandt.techgallery.service.model.TechnologyResponse;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;

/**
 * Services for Technology Endpoint requests.
 * 
 * @author felipers
 *
 */
public class TechnologyServiceImpl implements TechnologyService {

  TechnologyDAO technologyDAO = new TechnologyDAOImpl();

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
      throw new BadRequestException("Technology's id cannot be blank.");
    }
    // technology name can't be null or empty
    if (techName == null || techName.equals("")) {
      throw new BadRequestException("Technology's name cannot be blank.");
    } else {
      Technology entity = new Technology();
      entity.setId(techId);
      entity.setName(techName);
      entity.setShortDescription(technology.getShortDescription());
      entity.setDescription(technology.getDescription());
      entity.setAuthor(technology.getAuthor());
      entity.setWebsite(technology.getWebsite());
      entity.setImage(technology.getImage());
      entity.setRecommendation(technology.getRecommendation());
      technologyDAO.add(entity);
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
      throw new NotFoundException("No technology was found.");
    } else {
      TechnologiesResponse response = new TechnologiesResponse();
      List<TechnologyResponse> internList = new ArrayList<TechnologyResponse>();

      for (int i = 0; i < techEntities.size(); i++) {
        Technology tech = techEntities.get(i);
        TechnologyResponse techResponseItem = new TechnologyResponse();
        techResponseItem.setId(tech.getId());
        techResponseItem.setName(tech.getName());
        techResponseItem.setShortDescription(tech.getShortDescription());
        techResponseItem.setAuthor(tech.getAuthor());
        techResponseItem.setDescription(tech.getDescription());
        techResponseItem.setImage(tech.getImage());
        techResponseItem.setWebsite(tech.getWebsite());
        techResponseItem.setRecommendation(tech.getRecommendation());
        internList.add(techResponseItem);
      }
      response.setTechnologies(internList);
      return response;
    }
  }

  /**
   * GET for getting one technology.
   */
  @Override
  public Response getTechnology(final String id) throws NotFoundException {
    Technology techEntity = technologyDAO.findById(id);
    // if technology is null, return a not found exception
    if (techEntity == null) {
      throw new NotFoundException("No technology was found.");
    } else {
      TechnologyResponse response = new TechnologyResponse();
      response.setId(techEntity.getId());
      response.setName(techEntity.getName());
      response.setShortDescription(techEntity.getShortDescription());
      response.setAuthor(techEntity.getAuthor());
      response.setDescription(techEntity.getDescription());
      response.setImage(techEntity.getImage());
      response.setWebsite(techEntity.getWebsite());
      response.setRecommendation(techEntity.getRecommendation());
      return response;
    }
  }

}
