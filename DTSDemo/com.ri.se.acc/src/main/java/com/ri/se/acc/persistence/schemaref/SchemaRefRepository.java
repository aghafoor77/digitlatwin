package com.ri.se.acc.persistence.schemaref;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/***
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @category Repository
 * @version 1.0.0,
 * @apiNote Microcredential Microservice Development
 * @apiNote The interface to extend various operations performed on the user
 *          repository
 *
 */
public interface SchemaRefRepository extends JpaRepository<SchemaRef, String> {
	Optional<SchemaRef> findByOwnerEmail(String email);
	Optional<SchemaRef> findByCotextURL(String cotextURL);
}
