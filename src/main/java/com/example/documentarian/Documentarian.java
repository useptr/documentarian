package com.example.documentarian;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Documentarian {
    private static HashSet<String> classInstances = new HashSet<>();
    private HtmlReportGenerator htmlGenerator = new HtmlReportGenerator();
    public void getClassInstanceReport(Object obj) throws IllegalAccessException {
//        htmlGenerator.clearReportDir();
        htmlGenerator.setReportFileName(getValueReference(obj));
        System.out.println(htmlGenerator.htmlReportFileName());
        classInstanceInfo(obj);
        htmlGenerator.saveHtmlReport();

    }
    public void classInstanceInfo(Object obj) throws IllegalAccessException {
        if (obj == null || classInstances.contains(obj.toString()))
            return;
        classInstances.add(obj.toString());
        Class<?> aClass = obj.getClass();
        ArrayList<FieldDTO> fieldDTOs = new ArrayList<>();
        FieldDTO baseClass = new FieldDTO();
        baseClass.modifiers = getModifiers(aClass.getModifiers());
        baseClass.modifiers += " class";
        baseClass.type =  aClass.getSimpleName();
        baseClass.value = getValueReference(obj);
        baseClass.isBasicType = isBasicType(aClass);
        baseClass.isCollection = false;
        fieldDTOs.add(baseClass);

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.isCollection = false;
            fieldDTO.isBasicType = true;
            int mod = field.getModifiers();
            fieldDTO.modifiers = getModifiers(mod);
            fieldDTO.type = field.getType().getSimpleName();
            fieldDTO.name = field.getName();

            Object subObj = field.get(obj);
            Class<?> subClass = subObj.getClass();
            if (subObj == null) {
                fieldDTO.value  += "null";
            } else {
                fieldDTO.isBasicType = isBasicType(subClass);
                if (field.getType().isArray()) {
                    fieldDTO.isCollection = true;
                    fieldDTO.value = getValueInfoFromArray(subObj);
                } else if (Map.class.isAssignableFrom(subClass)) {
                    Map map = (Map) subObj;
                    Iterator<Map.Entry> it = map.entrySet().iterator();
                    Map.Entry entry = it.next();
                    Object entryKey = entry.getKey();
                    Object entryValue = entry.getValue();
                    fieldDTO.type += "["+entryKey.getClass().getSimpleName()+", "+entryValue.getClass().getSimpleName()+"]";
                    fieldDTO.isCollection = true;
                    fieldDTO.value = getValueInfoFromMap(map);
                }
                else {
                    fieldDTO.value  = getValueInfo(field.get(obj));
                }
            }
            fieldDTOs.add(fieldDTO);
        }
        htmlGenerator.addClassInstance(fieldDTOs);
//        htmlGenerator.createHtmlFileFromClassInstance(fieldDTOs);

    }
    private String getValueInfoFromMap(Map map) throws IllegalAccessException {
        String value = "[ ";
        int count = 0;
        for (Object obj : map.keySet()) {
            value += "{" + getValueInfo(obj) + ", " + getValueInfo(map.get(obj)) + "}";
            if (count < map.size()-1)
                value += ", ";
            ++count;
        }
        value  += " ]";
        return value;
    }
    private String getValueInfoFromArray(Object subObj) throws IllegalAccessException {
        String value = "[ ";
        Object[] objects = (Object[]) subObj;
        for(int i=0; i<objects.length; i++) {
            Object objItem = objects[i];
            value += getValueInfo(objItem);
            if (i < objects.length-1)
                value += ", ";
        }
        value  += " ]";
        return value;
    }
    public String getValueReference(Object obj) throws IllegalAccessException {
        String fieldRef = obj.toString();
        int SimpleRefStart = fieldRef.lastIndexOf(".") + 1;
        if (SimpleRefStart < 0 || SimpleRefStart >= fieldRef.length())
            SimpleRefStart =0;
        String ref = fieldRef.substring(SimpleRefStart);
        return ref;
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
                fieldRef = fieldRef.substring(SimpleRefStart);
                value = "<a href=\"#"+ fieldRef+"\">" + fieldRef + "</a>";
                classInstanceInfo(obj);
            }
        }
        return value;
    }
    private boolean isBasicType(Class<?> type) {
        return !type.toString().contains("com.example.") && !type.toString().contains("placeholders.");
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
