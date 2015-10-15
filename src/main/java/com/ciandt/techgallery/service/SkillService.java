package com.ciandt.techgallery.service;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.model.Skill;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.service.model.ImportUserSkillTO;

/**
 * Services for Skills.
 * 
 * @author Felipe Goncalves de Castro
 *
 */
public interface SkillService {

  /**
   * Service for adding a technology.
   * 
   * @param skill json with skill info.
   * @return skill info or message error.
   * @throws InternalServerErrorException in case something goes wrong
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  Skill addOrUpdateSkill(Skill skill, User user)
      throws InternalServerErrorException, BadRequestException, NotFoundException;

  /**
   * Service for getting an user skill.
   * 
   * @param techId technology id.
   * @param user oauth user.
   * @return skill info or message error.
   * @throws InternalServerErrorException in case something goes wrong
   * @throws OAuthRequestException in case of authentication problem
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  Skill getUserSkill(String techId, User user) throws BadRequestException, OAuthRequestException,
      NotFoundException, InternalServerErrorException;

  /**
   * Service for getting a TechGalleyUser skill.
   * 
   * @param techId technology id.
   * @param user datastore user.
   * @return skill info or message error.
   * @throws InternalServerErrorException in case something goes wrong
   * @throws OAuthRequestException in case of authentication problem
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  Skill getUserSkill(String techId, TechGalleryUser user) throws BadRequestException,
      OAuthRequestException, NotFoundException, InternalServerErrorException;

  /**
   * Service for import user´s skills from sheet.
   * 
   * @param userSkills Array of Transient objects with user email and skills for each technology.
   * @param user oauth user.
   * @return String with error or success message.
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   * @throws InternalServerErrorException in case something goes wrong
   */
  String importUserSkill(ImportUserSkillTO[] userSkills, User user)
      throws NotFoundException, InternalServerErrorException, BadRequestException;
}
