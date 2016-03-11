package com.ciandt.techgallery.service.model;

import java.util.Date;

/**
 * Response with a technology entity.
 *
 * @author felipers
 *
 */
public class TechnologyTO implements Response {

  /** technology id. */
  private String id;
  /** technology name. */
  private String name;
  /** technology short description. */
  private String shortDescription;
  /** technology description. */
  private String description;
  /** technology website. */
  private String website;
  /** technology author. */
  private String author;
  /** technology image. */
  private String image;
  /** Proposal client. */
  private String client;
  /** Proposal offer. */
  private String offer;
  /** Proposal ownerEmail. */
  private String ownerEmail;
  /** Proposal ownerName. */
  private String ownerName;
  /** Proposal technologies. */
  private String technologies;
  /** technology company status. */
  private String status;
  /** technology commentaries. */
  private Integer commentariesCounter;
  /** technology endorseds. */
  private Integer endorsersCounter;
  /** technology image byte content. */
  private String imageContent;
  /** technology is followed by the logged user. */
  private boolean followedByUser;

  private Date lastActivity;

  private Date creationDate;

  private String pipedriveLink;

  public String getId() {
    return id;
  }

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

  public String getImageContent() {
    return imageContent;
  }

  public void setImageContent(String imageContent) {
    this.imageContent = imageContent;
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

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getOffer() {
    return offer;
  }

  public void setOffer(String offer) {
    this.offer = offer;
  }
  
  public String getOwnerEmail() {
	return ownerEmail;
  }

  public void setOwnerEmail(String ownerEmail) {
	this.ownerEmail = ownerEmail;
  }

  public String getTechnologies() {
    return technologies;
  }

  public void setTechnologies(String technologies) {
    this.technologies = technologies;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean isFollowedByUser() {
    return followedByUser;
  }

  public void setFollowedByUser(boolean followedByUser) {
    this.followedByUser = followedByUser;
  }

  public Date getLastActivity() {
    return lastActivity;
  }

  public void setLastActivity(Date lastActivity) {
    this.lastActivity = lastActivity;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getPipedriveLink() {
    return this.pipedriveLink;
  }

  public void setPipedriveLink(String pipedriveLink) {
    this.pipedriveLink = pipedriveLink;
  }

public String getOwnerName() {
	return ownerName;
}

public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
}
}
