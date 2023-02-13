package ru.otus.homework.test.executor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.test.executor.TestExecutor;
import ru.otus.homework.test.model.StatisticTestInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class TestExecutorImpl implements TestExecutor {

    private static final Logger log = LoggerFactory.getLogger(TestExecutorImpl.class);
    public void execute(String className) throws Exception {

        log.info("Start process {}", className);
        final Class<?> testClass = Class.forName(className);
        final List<Method> testMethods = new ArrayList<>();
        final List<Method> beforeMethods = new ArrayList<>();
        final List<Method> afterMethods = new ArrayList<>();

        stream(testClass.getDeclaredMethods()).forEach(method -> {
            getMethodIfContainAnnotation(method, Before.class).ifPresent(beforeMethods::add);
            getMethodIfContainAnnotation(method, Test.class).ifPresent(testMethods::add);
            getMethodIfContainAnnotation(method, After.class).ifPresent(afterMethods::add);
        });
        verifyQuantityOfMethods(beforeMethods, Before.class.getName(), 1);
        verifyQuantityOfMethods(afterMethods, After.class.getName(), 1);

        StatisticTestInfo statisticInfo = new StatisticTestInfo(testMethods.size());

        for (Method testMethod : testMethods) {
            executeTest(testClass, testMethod, statisticInfo, beforeMethods, afterMethods);
        }

        final int totalTest = statisticInfo.getTotalTest();
        final int failedTest = statisticInfo.getFailedTest();
        final int successTest = statisticInfo.getSuccessTest();
        log.info("Class: {} - Success: {}, Failed: {}, Total: {}", testClass.getSimpleName(), successTest, failedTest, totalTest);
    }

    private static void executeTest(Class<?> clazz, Method testMethod, StatisticTestInfo statisticInfo,
                                    List<Method> beforeMethods, List<Method> afterMethods) throws Exception {

        log.info("Start process method: {}", testMethod.getName());
        boolean isSuccess = true;
        Object testClass = clazz.getConstructor().newInstance();
        try {
            executeOptionalMethod(beforeMethods, testClass);

            log.info("Start execute method {}", testMethod.getName());
            invokeMethod(testMethod, testClass);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logReflectiveOperationException(e);
            isSuccess = false;
        } finally {
            try {
                executeOptionalMethod(afterMethods, testClass);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logReflectiveOperationException(e);
                isSuccess = false;
            }
        }
        if (isSuccess) {
            statisticInfo.incrementSuccess();
        } else {
            statisticInfo.incrementFailed();
        }
    }

    private static void executeOptionalMethod(List<Method> beforeMethods, Object testClass)
            throws IllegalAccessException, InvocationTargetException {

        final Method method = beforeMethods.isEmpty() ? null : beforeMethods.get(0);
        if (nonNull(method)) {
            invokeMethod(method, testClass);
        }
    }

    private static void invokeMethod(Method testMethod, Object testClass)
            throws IllegalAccessException, InvocationTargetException {
        testMethod.setAccessible(true);
        testMethod.invoke(testClass);
    }

    private static <T extends Annotation> Optional<Method> getMethodIfContainAnnotation(Method method, Class<T> clazz) {
        final T annotation = method.getAnnotation(clazz);
        if (nonNull(annotation)) {
            return of(method);
        }
        return empty();
    }

    private static void verifyQuantityOfMethods(List<Method> methods, String annotationName, int count) {
        if (nonNull(methods) && methods.size() > count) {
            throw new IllegalStateException(String.format("Error method with annotation '%s' must be only one", annotationName));
        }
    }

    private static void logReflectiveOperationException(ReflectiveOperationException e) {
        if (nonNull(e.getCause())) {
            log.error(e.getCause().getMessage(), e.getCause());
        } else {
            log.error(e.getMessage(), e);
        }
    }
}