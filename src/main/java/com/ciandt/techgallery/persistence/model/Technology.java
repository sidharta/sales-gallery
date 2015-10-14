package com.ciandt.techgallery.persistence.model;

import com.google.api.server.spi.config.ApiTransformer;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import com.ciandt.techgallery.service.transformer.TechnologyTransformer;

import java.util.Date;

/**
 * Technology entity.
 *
 * @author Felipe Goncalves de Castro
 *
 */
@Entity
@ApiTransformer(TechnologyTransformer.class)
public class Technology extends BaseEntity<String> {

  /*
   * Constants --------------------------------------------
   */
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String SHORT_DESCRIPTION = "shortDescription";
  public static final String DESCRIPTION = "description";
  public static final String WEBSITE = "website";
  public static final String AUTHOR = "author";
  public static final String IMAGE = "image";
  public static final String RECOMMENDATION = "recommendation";
  public static final String POSITIVE_RECOMMENDATIONS_COUNTER = "positiveRecommendationsCounter";
  public static final String NEGATIVE_RECOMMENDATIONS_COUNTER = "negativeRecommendationsCounter";
  public static final String COMMENTARIES_COUNTER = "commentariesCounter";
  public static final String ENDORSERS_COUNTER = "endorsersCounter";

  /*
   * Attributes --------------------------------------------
   */
  @Id
  private String id;

  @Index
  private String name;

  @Unindex
  private String shortDescription;

  @Unindex
  private String description;

  @Unindex
  private String website;

  @Unindex
  private String author;

  @Unindex
  private String image;

  /** company recommendation info. */
  @Unindex
  private String recommendation;

  @Index
  private Integer positiveRecommendationsCounter;

  @Index
  private Integer negativeRecommendationsCounter;

  @Index
  private Integer commentariesCounter;

  @Index
  private Integer endorsersCounter;

  @Unindex
  private Date creationDate;

  /*
   * Methods --------------------------------------------
   */
  /**
   * Add 1 to the positive recomndations counter.
   */
  public void addPositiveRecommendationsCounter() {
    positiveRecommendationsCounter++;
  }

  /**
   * Remove 1 to the positive recomndations counter.
   */
  public void removePositiveRecommendationsCounter() {
    if (positiveRecommendationsCounter > 0) {
      positiveRecommendationsCounter--;
    } else {
      positiveRecommendationsCounter = 0;
    }
  }

  /**
   * Add 1 to the negative recomndations counter.
   */
  public void addNegativeRecommendationsCounter() {
    negativeRecommendationsCounter++;
  }

  /**
   * Remove 1 to the negative recomndations counter.
   */
  public void removeNegativeRecommendationsCounter() {
    if (negativeRecommendationsCounter > 0) {
      negativeRecommendationsCounter--;
    } else {
      negativeRecommendationsCounter = 0;
    }
  }

  /**
   * Add 1 to the commentary counter.
   */
  public void addCommentariesCounter() {
    commentariesCounter++;
  }

  /**
   * Remove 1 to the commentary counter.
   */
  public void removeCommentariesCounter() {
    if (commentariesCounter > 0) {
      commentariesCounter--;
    } else {
      commentariesCounter = 0;
    }
  }

  /**
   * Initialize the technology counters.
   */
  public void initCounters() {
    commentariesCounter = 0;
    endorsersCounter = 0;
    negativeRecommendationsCounter = 0;
    positiveRecommendationsCounter = 0;
  }

  /*
   * Getter's and Setter's --------------------------------------------
   */
  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(String recommendation) {
    this.recommendation = recommendation;
  }

  public Integer getPositiveRecommendationsCounter() {
    return positiveRecommendationsCounter;
  }

  public void setPositiveRecommendationsCounter(Integer positiveRecommendationsCounter) {
    this.positiveRecommendationsCounter = positiveRecommendationsCounter;
  }

  public Integer getNegativeRecommendationsCounter() {
    return negativeRecommendationsCounter;
  }

  public void setNegativeRecommendationsCounter(Integer negativeRecommendationsCounter) {
    this.negativeRecommendationsCounter = negativeRecommendationsCounter;
  }

  public Integer getCommentariesCounter() {
    return commentariesCounter;
  }

  public void setCommentariesCounter(Integer commentariesCounter) {
    this.commentariesCounter = commentariesCounter;
  }

  public Integer getEndorsersCounter() {
    return endorsersCounter;
  }

  public void setEndorsersCounter(Integer endorsersCounter) {
    this.endorsersCounter = endorsersCounter;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }
}
