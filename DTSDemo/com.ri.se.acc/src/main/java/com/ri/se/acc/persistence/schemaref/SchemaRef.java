package com.ri.se.acc.persistence.schemaref;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @category Entity
 * @version 1.0.0,
 * @apiNote Microcredential Microservice Development
 * @apiNote The SchemaRef entity to store SchemaRef's values.
 *
 */


@Entity
@Table(name = "SchemaRef") // Name of the entity in the database
public class SchemaRef {

	@Id
	@Column(nullable = false, length = 50, unique = true)
	private String ref;
	
	@Column(nullable = false, length = 150)
	private String cotextURL;
	
	@Column(nullable = false)
	private Date creationDate;
	
	@Column(nullable = false, length = 30)
	private String ownerEmail;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getCotextURL() {
		return cotextURL;
	}

	public void setCotextURL(String cotextURL) {
		this.cotextURL = cotextURL;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
}