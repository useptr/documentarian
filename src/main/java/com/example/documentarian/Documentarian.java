package com.example.documentarian;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;

public class Documentarian {
    private static HashSet<String> classInstances = new HashSet<>();
    private HtmlReportController htmlController = new HtmlReportController();
    public void getClassInstanceReport(Object obj) throws IllegalAccessException {
        htmlController.clearReport();
        System.out.println(htmlController.absoluteReportPath() + "\\" +getValueInfo(obj)+ ".html");
        classInstanceInfo(obj);
    }
    public void classInstanceInfo(Object obj) throws IllegalAccessException {
        if (obj == null || classInstances.contains(obj.toString()))
            return;
        classInstances.add(obj.toString());
        ArrayList<FieldDTO> fieldDTOs = new ArrayList<>();
        FieldDTO baseClass = new FieldDTO();
        Class<?> aClass = obj.getClass();

        String classModifiers = getModifiers(aClass.getModifiers());
        baseClass.modifiers = classModifiers;
//        System.out.println(classModifiers);

        String className = aClass.getSimpleName().toString();
        baseClass.type = className;
//        System.out.println(className);

        String classValue = obj.toString();
        baseClass.value = classValue;

        baseClass.isBasicType = isBasicType(aClass);
        baseClass.isArray = false;

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.isArray = false;

            int mod = field.getModifiers();
            String modifiers = getModifiers(mod);
            fieldDTO.modifiers = modifiers;
//            System.out.println(modifiers);

            String type = field.getType().getSimpleName();
            fieldDTO.type = type;

            fieldDTO.isBasicType = isBasicType(field.get(obj).getClass());

            String value = "";
            field.setAccessible(true);
            if (field.get(obj) == null) {
                value += "null";
            } else {
                if (field.getType().isArray()) {
                    fieldDTO.isArray = true;
                    value += "[ ";
                    Object subObj = field.get(obj);
                    Class<?> subClass = subObj.getClass();
                    Field[] subFields = subClass.getDeclaredFields();
                    for (Field subField : subFields) {
                        subField.setAccessible(true);
                        value += "<a href=\""+ htmlController.absoluteReportPath() + "\\" + getValueInfo(subField.get(subObj))+ ".html"+"\">" + getValueInfo(subField.get(subObj)) + "</a>" + ", ";
                    }
                    value += " ]";
                } else {
                    value = getValueInfo(field.get(obj));
                }
            }
            fieldDTO.value = value;
//            System.out.println(value);

        }

    }
    public String getValueInfo(Object obj) throws IllegalAccessException {
        String value = "";
        if (obj == null) {
            value = "null";
        } else {
            if (isBasicType(obj.getClass())) {
                value = obj.toString();
            } else {
                String fieldRef = obj.toString();
                int SimpleRefStart = fieldRef.lastIndexOf(".") + 1;
                if (SimpleRefStart < 0 || SimpleRefStart >= fieldRef.length())
                    SimpleRefStart =0;
                value = fieldRef.substring(SimpleRefStart) ;
                classInstanceInfo(obj);
            }
        }
        return value;
    }
    private boolean isBasicType(Class<?> type) {
        return !type.toString().contains("com.example.");
    }
    private String getModifiers(int mod ) {
        String str = "";
        if (Modifier.isPrivate(mod))
            str += " private";
        else if (Modifier.isPublic(mod))
            str += " public";
        else if (Modifier.isProtected(mod))
            str += " protected";

        if (Modifier.isAbstract(mod))
            str += " abstract";

        if (Modifier.isStatic(mod))
            str += " static";

        if (Modifier.isFinal(mod))
            str += " final";

        if (Modifier.isTransient(mod))
            str += " transient";

        if (Modifier.isVolatile(mod))
            str += " volatile";

        if (Modifier.isSynchronized(mod))
            str += " synchronized";

        if (Modifier.isNative(mod))
            str += " native";

        if (Modifier.isStrict(mod))
            str += " strictfp";

        return str;
    }
}
