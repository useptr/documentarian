package com.example.documentarian;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Documentarian {
    private  static TreeMap<String, ArrayList<FieldDTO> > instances = new TreeMap<>(); // хранит ссылки на обработанные классы для исключения повторной обработки
    private  static String mainClass; // хранит главный класс отчета
    private HtmlReportGenerator htmlGenerator = new HtmlReportGenerator(); // вспомогательный класс генерирующий html документ
    public void getClassInstanceReport(Object obj) throws IllegalAccessException {
//        htmlGenerator.clearReportDir();
        htmlGenerator.setReportFileName(getValueReference(obj));
        System.out.println(htmlGenerator.htmlReportFileName());
        mainClass =getValueReference(obj);
        classInstanceInfo(obj);
        htmlGenerator.createHtmlFileFromClassInstance(instances, mainClass);
//        htmlGenerator.saveHtmlReport();


    } // создание html документа по содержимому класса
    public void classInstanceInfo(Object obj) throws IllegalAccessException {
//        if (obj == null || classInstances.contains(obj.toString()))
//            return;
        if (obj == null || instances.containsKey(getValueReference(obj)))
            return;
        ArrayList<FieldDTO> fieldDTOs = new ArrayList<>();
        String classRef =  getValueReference(obj);
        instances.put(classRef, fieldDTOs);
//        classInstances.add(obj.toString());
        Class<?> aClass = obj.getClass();

        FieldDTO baseClass = new FieldDTO();
        baseClass.modifiers = getModifiers(aClass.getModifiers());
        baseClass.modifiers += " class";
        baseClass.type =  aClass.getSimpleName();
        baseClass.value.add(classRef);
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
                fieldDTO.value.add("null");
            } else {
                fieldDTO.isBasicType = isBasicType(subClass);
                if (field.getType().isArray()) {
                    fieldDTO.isCollection = true;
                    fieldDTO.value = getValueInfoFromArray(subObj);
                }
//                else if (Map.class.isAssignableFrom(subClass)) {
//                    Map map = (Map) subObj;
//                    Iterator<Map.Entry> it = map.entrySet().iterator();
//                    Map.Entry entry = it.next();
//                    Object entryKey = entry.getKey();
//                    Object entryValue = entry.getValue();
//                    fieldDTO.type += "["+entryKey.getClass().getSimpleName()+", "+entryValue.getClass().getSimpleName()+"]";
//                    fieldDTO.isCollection = true;
//                    fieldDTO.value = getValueInfoFromMap(map);
//                }
                else {
                    fieldDTO.value.add(getValueInfo(field.get(obj)));
                }
            }
            fieldDTOs.add(fieldDTO);
        }
//        htmlGenerator.addClassInstance(fieldDTOs);
//        htmlGenerator.createHtmlFileFromClassInstance(fieldDTOs);

    } // рекурсивная обработка экземпляров класса
    private ArrayList<String> getValueInfoFromMap(Map map) throws IllegalAccessException {
        ArrayList<String> values = new ArrayList<>();
//        int count = 0;
        for (Object obj : map.keySet()) {
//            value += "{" + getValueInfo(obj) + ", " + getValueInfo(map.get(obj)) + "}";
//            if (count < map.size()-1)
//                value += ", ";
//            ++count;
        }
//        value  += " ]";
        return values;
    } // возвращает содержимое поля-словаря
    private ArrayList<String> getValueInfoFromArray(Object subObj) throws IllegalAccessException {
//        String value = "[ ";
        ArrayList<String> values = new ArrayList<>();
        Object[] objects = (Object[]) subObj;
        for(int i=0; i<objects.length; i++) {
            Object objItem = objects[i];
            values.add(getValueInfo(objItem));
//            value += getValueInfo(objItem);
//            if (i < objects.length-1)
//                value += ", ";
        }
//        value  += " ]";
        return values;
    } // возвращает содержимое поля-массива
    public String getValueReference(Object obj) throws IllegalAccessException {
//        System.out.println(obj);
        String fieldRef = obj.toString();
        int SimpleRefStart = fieldRef.lastIndexOf(".") + 1;
        if (SimpleRefStart < 0 || SimpleRefStart >= fieldRef.length())
            SimpleRefStart =0;
        String ref = fieldRef.substring(SimpleRefStart);
        return ref;
    } // возвращает ссылку на класс
    public String getValueInfo(Object obj) throws IllegalAccessException {
        String value = "";
        if (obj == null) {
            value = "null";
        } else {
//            System.out.println(obj.getClass());
            if (isBasicType(obj.getClass())) {
                value = obj.toString();
            } else {
                String fieldRef = obj.toString();
                int SimpleRefStart = fieldRef.lastIndexOf(".") + 1;
                if (SimpleRefStart < 0 || SimpleRefStart >= fieldRef.length())
                    SimpleRefStart =0;
                fieldRef = fieldRef.substring(SimpleRefStart);
                value = fieldRef;
//                value = "<a href=\"#"+ fieldRef+"\">" + fieldRef + "</a>";
                classInstanceInfo(obj);
            }
        }
        return value;
    } // возвращает содержимое поля
    private boolean isBasicType(Class<?> type) {
        return !type.toString().contains("com.example.") && !type.toString().contains("placeholders.");
    } // определяет созданный пользователем класс или стандартный
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
    } // возвращает все модификаторы поля
}
