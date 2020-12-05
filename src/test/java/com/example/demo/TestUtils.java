package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObject(Object target, String fieldName, Object toInject) {
        boolean isPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if(!f.isAccessible()) {
                f.setAccessible(true);
                isPrivate = true;
            }
            f.set(target, toInject);

            if(isPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}