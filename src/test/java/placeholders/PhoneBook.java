package placeholders;

import java.util.HashMap;

public class PhoneBook {
    private HashMap<String,Person> phoneBook = new HashMap<>();
    private int phoneCounter = 0;
    private String city;
    public PhoneBook(String city) {
        this.city = city;
    }
    public String city() {
        return city;
    }
    public void addNumber(String phone, Person person) {
        phoneBook.put(phone, person);
        ++phoneCounter;
    }

}
