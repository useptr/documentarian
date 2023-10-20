package com.example.documentarian;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        getInfoAboutClass("com.example.documentarian.Polygon");
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
    }

    public String getInfoAboutClass(Class<?> clazz) {
//        Field[] fields = clazz.getFields();
//        for (Field field : fields) {
//            System.out.println(field.getName());
//        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Class<? extends Field> aClass = field.getClass();
            if (field.getType().isArray()) {
                System.out.println(field.getType().arrayType());
                getInfoAboutClass(field.getType().componentType());
            }
            System.out.println(field.getType());
            System.out.println(field.getName());
            getInfoAboutClass(aClass);
        }
        return null;
    }


    public String getInfoAboutClass(String className) throws ClassNotFoundException {
        Class<?> aClass1 = Class.forName(className);
        getInfoAboutClass(aClass1);
        return null;
    }
}