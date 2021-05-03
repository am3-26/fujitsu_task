package erika.fujitsu.backend.service;

import erika.fujitsu.backend.entity.PersonEntry;

import erika.fujitsu.backend.repository.PersonRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * PersonService is a class that gives access to the repository/database to other classes.
 * No changes should be made directly in the repository! This is what PersonService is here for.
 *
 * @author Erika Maksimova
 */
@Service
public class PersonService {

    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());
    private PersonRepository personRepository;

    /**
     * PersonService constructor.
     *
     * @param personRepository the repository of our feedback data
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Find all stored feedback data entries.
     *
     * @return a list of stored PersonEntries.
     */
    public List<PersonEntry> findAll() {
        return personRepository.findAll();
    }

    /**
     * Count all stored entries.
     *
     * @return the number of entries.
     */
    public long count() {
        return personRepository.count();
    }

    /**
     * Delete a specific entry.
     *
     * @param entry to delete
     */
    public void delete(PersonEntry entry) {
        personRepository.delete(entry);
    }

    /**
     * Save a specific entry,
     *
     * @param entry to save in the repository
     */
    public void save(PersonEntry entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "The entry is null. Are you sure you have connected your form to the application?");
            return;
        }
        personRepository.save(entry);
    }

    /**
     * This is a method that makes sure that when our application has started, it automatically saves the first entry.
     * All of the data has been provided by Fujitsu Estonia AS.
     */
    @PostConstruct
    public void populateWithFujitsuData() {
        if (personRepository.count() == 0) {
            PersonEntry fujitsuEntry = new PersonEntry();
            fujitsuEntry.setName("Otto Lakk");
            fujitsuEntry.setEmail("test@fujitsu.ee");
            fujitsuEntry.setCategories("Patients portal, Microsoft SharePoint");
            fujitsuEntry.setText("Feedback text");
            personRepository.save(fujitsuEntry);
        }
    }
}
