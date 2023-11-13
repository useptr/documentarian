package placeholders;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneBook {
    private Person[] phoneBook = new Person[4];
    private int phoneCounter = 0;
    private String city;
    public PhoneBook(String city) {
        this.city = city;
    }
    public String city() {
        return city;
    }
    public void addNumber(String phone, Person person) {
//        phoneBook.add(person);
        phoneBook[phoneCounter] = person;
        ++phoneCounter;
    }

}
