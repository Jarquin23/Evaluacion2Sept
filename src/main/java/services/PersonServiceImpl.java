package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Person;

public class PersonServiceImpl implements PersonService {
    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    @Override
    public ObservableList<Person> getPersons() {
        return persons;
    }

    @Override
    public void save(Person person) {
        persons.add(person);
    }

    @Override
    public void update(Person oldPerson, Person newPerson) {
        int index = persons.indexOf(oldPerson);
        if (index != -1) {
            persons.set(index, newPerson);
        }
    }

    @Override
    public void delete(Person person) {
        persons.remove(person);
    }
}