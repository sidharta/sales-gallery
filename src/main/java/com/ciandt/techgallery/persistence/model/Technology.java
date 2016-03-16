package com.ciandt.techgallery.persistence.model;

import com.google.api.server.spi.config.ApiTransformer;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import com.ciandt.techgallery.service.enums.TechnologyOrderOptionEnum;
import com.ciandt.techgallery.service.transformer.TechnologyTransformer;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
	public static final String CLIENT = "client";
	public static final String OFFERS = "offers";
	public static final String TOWER = "tower";
	public static final String OWNER_EMAIL = "ownerEmail";
	public static final String OWNER_NAME = "ownerName";
	public static final String TECHNOLOGIES = "technologies";
	public static final String STATUS = "status";
	public static final String COMMENTARIES_COUNTER = "commentariesCounter";
	public static final String ENDORSERS_COUNTER = "endorsersCounter";
	public static final String LAST_ACTIVITY = "lastActivity";
	public static final String UPDATE_USER = "updateUser";
	public static final String ACTIVE = "active";

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

	@Unindex
	private String client;

	@Unindex
	private List<String> offers;

	@Unindex
	private String tower;

	@Unindex
	private String ownerEmail;

	@Unindex
	private String ownerName;

	@Unindex
	private String technologies;

	/** company recommendation info. */
	@Unindex
	private String status;

	@Index
	private Integer commentariesCounter;

	@Index
	private Integer endorsersCounter;

	@Unindex
	private Date creationDate;

	@Ignore
	private boolean followedByUser;

	@Unindex
	private Date lastActivity;

	@Unindex
	private String lastActivityUser;

	@Index
	private Boolean active;

	@Ignore
	private String imageContent;

	@Index
	private String customerName;

	@Index
	private String pipedriveLink;

	/*
	 * Getter's and Setter's --------------------------------------------
	 */

	public String getPipedriveLink() {
		return pipedriveLink;
	}

	public void setPipedriveLink(String value) {
		this.pipedriveLink = value;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String value) {
		this.customerName = value;
	}

	public List<String> getOffers() {
		return offers;
	}

	public void setOffers(List<String> value) {
		this.offers = value;
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

	public void setTechnologies(String value) {
		this.technologies = value;
	}

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

	public String getImageContent() {
		return imageContent;
	}

	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
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
		if (website != null && !website.contains("http")) {
			this.website = "http://" + website;
		} else {
			this.website = website;
		}
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getLastActivityUser() {
		return lastActivityUser;
	}

	public void setLastActivityUser(String lastActivityUser) {
		this.lastActivityUser = lastActivityUser;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getTower() {
		return tower;
	}

	public void setTower(String tower) {
		this.tower = tower;
	}

	/*
	 * Methods --------------------------------------------
	 */

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
	}

	/**
	 * Sort the technology list by Last Activity Date.
	 *
	 * @param techEntities
	 *            List of technologies.
	 */
	public static void sortTechnologiesDefault(List<Technology> techEntities) {
		Collections.sort(techEntities, new Comparator<Technology>() {
			@Override
			public int compare(Technology counter1, Technology counter2) {
				return counter2.getLastActivity().compareTo(counter1.getLastActivity());
			}
		});
	}

	/**
	 * Sort the Technology list according to the given enum value.
	 *
	 * @param techList
	 *            List of technologies.
	 * @param orderBy
	 *            Enum value.
	 * @return sorted list.
	 */
	public static List<Technology> sortTechnologies(List<Technology> techList, TechnologyOrderOptionEnum orderBy) {
		orderBy.sort(techList);
		return techList;
	}

	/**
	 * Method that gets the name of the technology and creates the id.
	 *
	 * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros
	 *         Moreira </a>
	 * @since 13/10/2015
	 *
	 * @param name
	 *            to format.
	 *
	 * @return the id formatted.
	 */
	public String convertNameToId(String name) {
		name = Normalizer.normalize(name, Normalizer.Form.NFD);
		name = name.replaceAll("[^\\p{ASCII}]", "");
		return name.toLowerCase().replaceAll(" ", "_").replaceAll("#", "_").replaceAll("\\/", "_").replaceAll("\\.",
				"");
	}
}
