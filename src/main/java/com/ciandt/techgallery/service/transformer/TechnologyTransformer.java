package com.ciandt.techgallery.service.transformer;

import com.google.api.server.spi.config.Transformer;

import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.model.TechnologyTO;

public class TechnologyTransformer implements Transformer<Technology, TechnologyTO> {

  @Override
  public Technology transformFrom(TechnologyTO baseObject) {
    Technology product = new Technology();
    product.setAuthor(baseObject.getAuthor());
    product.setDescription(baseObject.getDescription());
    product.setId(baseObject.getId());
    product.setImage(baseObject.getImage());
    product.setName(baseObject.getName());
    product.setRecommendation(baseObject.getRecommendation());
    product.setRecommendationJustification(baseObject.getRecommendationJustification());
    product.setShortDescription(baseObject.getShortDescription());
    product.setWebsite(baseObject.getWebsite());
    product.setPositiveRecommendationsCounter(baseObject.getPositiveRecommendationsCounter());
    product.setNegativeRecommendationsCounter(baseObject.getNegativeRecommendationsCounter());
    product.setCommentariesCounter(baseObject.getCommentariesCounter());
    product.setEndorsersCounter(baseObject.getEndorsersCounter());
    return product;
  }

  @Override
  public TechnologyTO transformTo(Technology baseObject) {
    if (baseObject.getInactivatedDate() == null) {
      TechnologyTO product = new TechnologyTO();
      product.setAuthor(baseObject.getAuthor());
      product.setDescription(baseObject.getDescription());
      product.setId(baseObject.getId());
      product.setImage(baseObject.getImage());
      product.setName(baseObject.getName());
      product.setRecommendation(baseObject.getRecommendation());
      product.setRecommendationJustification(baseObject.getRecommendationJustification());
      product.setShortDescription(baseObject.getShortDescription());
      product.setWebsite(baseObject.getWebsite());
      product.setPositiveRecommendationsCounter(baseObject.getPositiveRecommendationsCounter());
      product.setNegativeRecommendationsCounter(baseObject.getNegativeRecommendationsCounter());
      product.setCommentariesCounter(baseObject.getCommentariesCounter());
      product.setEndorsersCounter(baseObject.getEndorsersCounter());
      return product;
    } else {
      return null;
    }
  }

}
