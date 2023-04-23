package ru.otus.appcontainer.api;

import ru.otus.appcontainer.exceptions.ComponentInitializationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Object getInstance(Class<?> clazz){
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentInitializationException(String.format("Error initialize %s", clazz.getName()));
        }
    }

    public static Object invokeMethod(Object clazz, Method method, Object... args){
        try {
            return method.invoke(clazz, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentInitializationException(String.format("Error initialize %s", method.getName()));
        }
    }
}