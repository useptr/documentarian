package com.example.documentarian;

import org.junit.jupiter.api.BeforeEach;
import placeholders.*;

class DocumentarianTest {
    private static int count = 0;
    private Documentarian doc;
    @BeforeEach
    void init() {
        doc = new Documentarian();
    }

    @org.junit.jupiter.api.Test
    void htmlReportFromPolygon() throws IllegalAccessException {
        Polygon3D polygon = new Polygon3D();
        polygon.points[0] = new Point3D(0 , 9.99, 12.7997);
        polygon.points[1] = new Point3D(1, 23, 6);
        polygon.points[2] = new Point3D(2, 46, 8);
        polygon.points[3] = new Point3D(2, 454, 87.989);
        polygon.points[4] = new Point3D(12, 546, 8);
        polygon.points[5] = new Point3D(0,0,1);

        System.out.println("Test #" + getTestNumber());
        doc.getClassInstanceReport(polygon);
    }

    @org.junit.jupiter.api.Test
    void htmlReportFromArrayCitiesPhoneBook() throws IllegalAccessException {
        CitiesPhoneBook citiesPhoneBook = new CitiesPhoneBook();
        PhoneBook phoneBook1 = new PhoneBook("New York");
        Person person1 = new Person("Bob", "Dylon", 182, 90);
        phoneBook1.addNumber("(555) 555-1234",person1);
        Person person2 = new Person("Jacky", "Jonson", 193, 95);
        phoneBook1.addNumber("(555) 566-9634",person2);
        phoneBook1.addNumber("(556) 569-1211",person1);
        citiesPhoneBook.addPhoneBook(phoneBook1.city(),phoneBook1);

        PhoneBook phoneBook2 = new PhoneBook("Chicago");
        Person person3 = new Person("Thomas", "Johnson", 182, 90);
        phoneBook2.addNumber("(577) 875-4344",person3);
        Person person4 = new Person("Darrell", "Gallant", 193, 95);
        phoneBook2.addNumber("(577) 889-6789",person4);
        phoneBook2.addNumber("(577) 453-1214",person4);
        citiesPhoneBook.addPhoneBook(phoneBook2.city(),phoneBook2);

        PhoneBook phoneBook3 = new PhoneBook("Houston");
        Person person5 = new Person("Charles", "McCloy", 182, 90);
        phoneBook3.addNumber("(577) 875-4344",person5);
        Person person6 = new Person("Letitia", "Webb", 193, 95);
        phoneBook3.addNumber("(577) 889-6789",person5);
        phoneBook3.addNumber("(577) 453-1214",person6);
        citiesPhoneBook.addPhoneBook(phoneBook3.city(),phoneBook3);

        System.out.println("Test #" + getTestNumber());
        doc.getClassInstanceReport(citiesPhoneBook);
    }
    @org.junit.jupiter.api.Test
    void htmlReportFromPhoneBook() throws IllegalAccessException {
        PhoneBook phoneBook = new PhoneBook("Los Angeles");
        Person person1 = new Person("Bob", "Dylon", 182, 90);
        phoneBook.addNumber("(555) 555-1234",person1);
        Person person2 = new Person("Jacky", "Jonson", 193, 95);
        phoneBook.addNumber("(555) 566-9634",person2);
        phoneBook.addNumber("(556) 569-1211",person1);

        System.out.println("Test #" + getTestNumber());
        doc.getClassInstanceReport(phoneBook);
    }

    int getTestNumber() {
        ++count;
        return count;
    }

}