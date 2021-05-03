package erika.fujitsu.backend.repository;

import erika.fujitsu.backend.entity.PersonEntry;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * JpaRepository (JPA = Java Persistence API) is a Spring framework public interface for storing data.
 * it is the most optimal solution for one-page websites/applications with a local database.
 *
 * @author Erika Maksimova
 */
public interface PersonRepository extends JpaRepository<PersonEntry, Long> {

}