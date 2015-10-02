package com.ciandt.techgallery.persistence.dao;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Ref;

import com.ciandt.techgallery.ofy.OfyService;
import com.ciandt.techgallery.persistence.dao.impl.GenericDAOImpl;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.persistence.model.TechnologyComment;
import com.ciandt.techgallery.persistence.model.TechnologyRecommendation;

import java.util.List;

/**
 * Class that implements DAO of TechnologyRecommendation
 *
 * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros Moreira </a>
 * @since 23/09/2015
 *
 */
public class TechnologyRecommendationDAOImpl extends GenericDAOImpl<TechnologyRecommendation, Long>
    implements TechnologyRecommendationDAO {

  @Override
  public List<TechnologyRecommendation> findAllActivesByTechnology(Technology technology) {
    Objectify objectify = OfyService.ofy();
    List<TechnologyRecommendation> recommendations =
        objectify.load().type(TechnologyRecommendation.class)
            .filter(TechnologyRecommendation.TECHNOLOGY, Ref.create(technology))
            .filter(TechnologyRecommendation.ACTIVE, Boolean.TRUE).list();

    return recommendations;
  }

  @Override
  public TechnologyRecommendation findActiveByRecommenderAndTechnology(TechGalleryUser tgUser,
      Technology technology) {
    Objectify objectify = OfyService.ofy();
    List<TechnologyRecommendation> recommendations =
        objectify.load().type(TechnologyRecommendation.class)
            .filter(TechnologyRecommendation.TECHNOLOGY, Ref.create(technology))
            .filter(TechnologyRecommendation.ACTIVE, Boolean.TRUE)
            .filter(TechnologyRecommendation.RECOMMENDER, Ref.create(tgUser)).list();
    if (recommendations == null || recommendations.isEmpty()) {
      return null;
    } else {
      return recommendations.get(0);
    }
  }

  @Override
  public TechnologyRecommendation findByComment(TechnologyComment comment) {
    Objectify objectify = OfyService.ofy();
    List<TechnologyRecommendation> recommendations =
        objectify.load().type(TechnologyRecommendation.class)
            .filter(TechnologyRecommendation.COMMENT, Ref.create(comment)).list();
    if (recommendations == null || recommendations.isEmpty()) {
      return null;
    } else {
      return recommendations.get(0);
    }
  }

}
