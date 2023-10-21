package com.example.documentarian;

import java.lang.reflect.Array;
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
        Class<?> aClass = obj.getClass();

        ArrayList<FieldDTO> fieldDTOs = new ArrayList<>();
        FieldDTO baseClass = new FieldDTO();

        baseClass.modifiers = getModifiers(aClass.getModifiers());
        baseClass.type =  aClass.getSimpleName();
        baseClass.value = getValueInfo(obj);
        baseClass.isBasicType = isBasicType(aClass);
        baseClass.isArray = false;
        fieldDTOs.add(baseClass);

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            FieldDTO fieldDTO = new FieldDTO();

            fieldDTO.isArray = false;
            fieldDTO.isBasicType = true;
            int mod = field.getModifiers();
            fieldDTO.modifiers = getModifiers(mod);
//            System.out.println(modifiers);

            fieldDTO.type = field.getType().getSimpleName();
            fieldDTO.value = "";
            Object subObj = field.get(obj);
            if (subObj == null) {
                fieldDTO.value  += "null";
            } else {
                Class<?> subClass = subObj.getClass();
                fieldDTO.isBasicType = isBasicType(subClass);
                if (field.getType().isArray()) {
                    fieldDTO.isArray = true;
                    fieldDTO.value  += "[ ";

                    Field[] subFields = subClass.getDeclaredFields();
                    Object[] objects = (Object[]) subObj;
                    for (Object objItem : objects) {
//                        subField.setAccessible(true);
                        fieldDTO.value  += "<a href=\""+ htmlController.absoluteReportPath() + "\\" + getValueInfo(objItem)+ ".html"+"\">" + getValueInfo(objItem) + "</a>" + ", ";
                    }
                    fieldDTO.value  += " ]";
                } else {
                    fieldDTO.value  = getValueInfo(field.get(obj));
                }
            }
            fieldDTOs.add(fieldDTO);
        }
        htmlController.createHtmlFileFromClassInstance(fieldDTOs);

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
