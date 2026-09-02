package services;

import javafx.collections.ObservableList;
import models.Person;

public interface PersonService {
    ObservableList<Person> getPersons();
    void save(Person person);
    void update(Person oldPerson, Person newPerson);
    void delete(Person person);
}