package com.ciandt.techgallery.persistence.dao.impl;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Ref;

import com.ciandt.techgallery.ofy.OfyService;
import com.ciandt.techgallery.persistence.dao.TechnologyCommentDAO;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.persistence.model.TechnologyComment;

import java.util.List;

/**
 * Class that implements DAO of TechnologyComment
 *
 * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros Moreira </a>
 * @since 22/09/2015
 *
 */
public class TechnologyCommentDAOImpl extends GenericDAOImpl<TechnologyComment, Long>
    implements TechnologyCommentDAO {

  private static TechnologyCommentDAOImpl instance;

  /*
   * Constructor --------------------------------------------
   */
  private TechnologyCommentDAOImpl() {}

  public static TechnologyCommentDAOImpl getInstance() {
    if (instance == null) {
      instance = new TechnologyCommentDAOImpl();
    }
    return instance;
  }

  @Override
  public List<TechnologyComment> findAllActivesByTechnology(Technology technology) {
    Objectify objectify = OfyService.ofy();
    List<TechnologyComment> entities =
        objectify.load().type(TechnologyComment.class).order("-" + TechnologyComment.TIMESTAMP)
            .filter(TechnologyComment.TECHNOLOGY, Ref.create(technology))
            .filter(TechnologyComment.ACTIVE, Boolean.TRUE).list();

    return entities;
  }
}
