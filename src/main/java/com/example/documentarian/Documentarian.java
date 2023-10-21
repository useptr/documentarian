package com.example.documentarian;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Documentarian {
    public void classInstanceInfo(Object obj) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();

        String classModifiers = getModifiers(aClass.getModifiers());
        System.out.println(classModifiers);

        String className = aClass.getSimpleName().toString();
        System.out.println(className);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {

            int mod = field.getModifiers();
            String modifiers = getModifiers(mod);
            System.out.println(modifiers);

            String type = field.getType().getSimpleName();
            System.out.println(type);

            String value = "";
            field.setAccessible(true);
            if (field.get(obj) == null) {
                value += "null";
            } else {
                if (field.getType().isArray()) {
                        value += "[ ";
                    Class<?> subClass = field.get(obj).getClass();
                    Field[] subFields = subClass.getDeclaredFields();
                    for (Field subField : subFields) {
                        value += getValueInfo(subField, obj) + ", ";
                    }
                    value += " ]";
                } else {
                    value = getValueInfo(field, obj);
                }
            }



            System.out.println(value);

        }

    }
    public String getValueInfo(Field field, Object obj) throws IllegalAccessException {
        String value = "";
        if (field.get(obj) == null) {
            value = "null";
        } else {
            if (isBasicType(field.getType())) {
                value = field.get(obj).toString();
            } else {
                String fieldRef = field.get(obj).toString();
                int SimpleRefStart = fieldRef.lastIndexOf(".") + 1;
                if (SimpleRefStart < 0 || SimpleRefStart >= fieldRef.length())
                    SimpleRefStart =0;
                value = fieldRef.substring(SimpleRefStart) ;
//                classInstanceInfo(field.get(obj));
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
