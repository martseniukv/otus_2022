package ru.otus.homework;


import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.model.StatisticTestInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;

@Slf4j
public class TestStarter {

    /**
     *
     * @param args
     * ru.otus.homework.test.SimplePersonServiceTest
     * ru.otus.homework.test.SimpleStringServiceTest
     * ru.otus.homework.test.ErrorPersonServiceTest
     * ru.otus.homework.test.ErrorStringServiceTest
     */
    public static void main(String[] args) {

        for (var className : args) {
            try {
                processTestClass(className);
            } catch (ClassNotFoundException e) {
                log.error("Can not found class: " + className);
            }
        }
    }

    private static void processTestClass(String className) throws ClassNotFoundException {

        log.info("Start process {}", className);
        final Class<?> clazz = Class.forName(className);

        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        stream(clazz.getDeclaredMethods()).forEach(method -> {
            getMethodIfContainAnnotation(method, Before.class).ifPresent(beforeMethods::add);
            getMethodIfContainAnnotation(method, Test.class).ifPresent(testMethods::add);
            getMethodIfContainAnnotation(method, After.class).ifPresent(afterMethods::add);
        });
        verifyCountOfMethods(beforeMethods, Before.class.getName(), 1);
        verifyCountOfMethods(afterMethods, After.class.getName(), 1);

        StatisticTestInfo statisticInfo = new StatisticTestInfo();

        testMethods.forEach(testMethod -> executeTest(clazz, testMethod, statisticInfo, beforeMethods, afterMethods));

        final int totalTest = statisticInfo.getTotalTest();
        final int failedTest = statisticInfo.getFailedTest();
        final int successTest = statisticInfo.getSuccessTest();
        log.info("Class: {} - Success: {}, Failed: {}, Total: {}", clazz.getSimpleName(), successTest, failedTest, totalTest);
    }

    private static void executeTest(Class<?> clazz, Method testMethod, StatisticTestInfo statisticInfo,
                                    List<Method> beforeMethods, List<Method> afterMethods) {

        log.info("Start process method: {}", testMethod.getName());
        try {

            final Constructor<?> clazzConstructor = clazz.getConstructor();
            final Object testClass = clazzConstructor.newInstance();

            executeOptionMethod(beforeMethods, testClass);

            log.info("Start execute method {}", testMethod.getName());
            testMethod.setAccessible(true);
            testMethod.invoke(testClass);

            executeOptionMethod(afterMethods, testClass);

            statisticInfo.setSuccessTest(statisticInfo.getSuccessTest() + 1);
        } catch (Exception e) {
            statisticInfo.setFailedTest(statisticInfo.getFailedTest() + 1);
            if (nonNull(e.getCause())){
                log.error(e.getCause().getMessage(), e.getCause());
            } else {
                log.error(e.getMessage(), e);
            }
        }
        statisticInfo.setTotalTest(statisticInfo.getTotalTest() + 1);
    }

    private static void executeOptionMethod(List<Method> beforeMethods, Object testClass)
            throws IllegalAccessException, InvocationTargetException {

        final Method method = beforeMethods.isEmpty() ? null : beforeMethods.get(0);
        if (nonNull(method)) {
            method.setAccessible(true);
            method.invoke(testClass);
        }
    }

    private static <T extends Annotation> Optional<Method> getMethodIfContainAnnotation(Method method, Class<T> clazz) {
        final T annotation = method.getAnnotation(clazz);
        if (nonNull(annotation)) {
            return Optional.of(method);
        }
        return empty();
    }

    private static void verifyCountOfMethods(List<Method> methods, String methodsName, int count) {
        if (nonNull(methods) && methods.size() > count) {
            throw new IllegalStateException(String.format("Error '%s' methods must be only one", methodsName));
        }
    }
}