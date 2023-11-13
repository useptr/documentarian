package placeholders;

import java.util.ArrayList;
import java.util.HashMap;

public class CitiesPhoneBook {
    private PhoneBook[] citiesPhoneBook = new PhoneBook[3];
    private int citiesCounter = 0;
    public void addPhoneBook(String city, PhoneBook book) {
//        citiesPhoneBook.add(book);
        citiesPhoneBook[citiesCounter] = book;
        ++citiesCounter;
    }
}
