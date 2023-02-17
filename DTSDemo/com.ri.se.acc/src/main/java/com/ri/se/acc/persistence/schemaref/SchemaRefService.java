package com.ri.se.acc.persistence.schemaref;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @category Service
 * @version 1.0.0,
 * @apiNote Microcredential Microservice Development
 * @apiNote The SchemaRef service handles various operations required by the other
 *          classes to perform operations on the SchemaRef entity.
 *
 */
@Service
public class SchemaRefService {

	/**
	 * repository, to access the persistence storage to perform operations on SchemaRef
	 */
	@Autowired
	private SchemaRefRepository repository;

	/**
	 * 
	 * @param UserVerification
	 * @return SchemaRef, store SchemaRef in the SchemaRef entity (persistence)
	 */
	public SchemaRef saveSchemaRef(SchemaRef schemaRef) {
		return repository.save(schemaRef);
	}

	/**
	 * 
	 * @param schemaRef
	 * @return List<SchemaRef>, store all the provided schemaRef into persistence and return
	 *         list of all recently registered schemaRef.
	 */
	public List<SchemaRef> saveSchemaRef(List<SchemaRef> schemaRef) {
		return repository.saveAll(schemaRef);
	}

	/**
	 * 
	 * @param schemaRef
	 * @return List<SchemaRef>, return list of all registered schemaRef.
	 */
	public List<SchemaRef> getSchemaRef() {
		return repository.findAll();
	}

	/**
	 * 
	 * @param id
	 * @return SchemaRef
	 */
	public SchemaRef getSchemaRefById(String id) {
		return repository.findById(id).orElse(null);
	}

	/**
	 * 
	 * @param name
	 * @return SchemaRef
	 */
	public SchemaRef findByOwnerEmail(String email) {
		Optional<SchemaRef> schemaRef = repository.findByOwnerEmail(email);
		return schemaRef.get();

	}

	/**
	 * 
	 * @param email
	 * @return SchemaRef
	 */
	public SchemaRef findByCotextURL(String cotextURL) {
		Optional<SchemaRef> schemaRef = repository.findByCotextURL(cotextURL);
		return schemaRef.get();
	}

	/**
	 * 
	 * @param schemaRef
	 * @return updated SchemaRef
	 */
	public SchemaRef updateSchemaRef(SchemaRef schemaRef) {
		return repository.save(schemaRef);
	}
}
