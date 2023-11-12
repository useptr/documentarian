package placeholders;

import java.util.HashMap;

public class CitiesPhoneBook {
    private HashMap<String,PhoneBook> citiesPhoneBook = new HashMap<>();
    private int citiesCounter = 0;
    public void addPhoneBook(String city, PhoneBook book) {
        citiesPhoneBook.put(city, book);
        ++citiesCounter;
    }
}
