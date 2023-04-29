package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.ReflectionUtils;
import ru.otus.appcontainer.api.annotations.AppComponent;
import ru.otus.appcontainer.api.annotations.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.NoSuchComponentException;
import ru.otus.appcontainer.exceptions.NoUniqueComponentException;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final String ERROR_NON_CONFIG_CLASS = "Error given class is not config %s";
    private static final String ERROR_NOT_UNIQUE_COMPONENT = "Error: component name %s is not unique";
    private static final String ERROR_QUALIFIER_CANDIDATES = "Error: more than one component found for %s, ";
    private static final String ERROR_COMPONENT_DOES_NOT_EXIST = "Error: component %s does not exist";

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {

        Arrays.stream(configClasses)
                .filter(Objects::nonNull)
                .filter(config -> config.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(comparing(configClass -> configClass.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEachOrdered(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).stream()
                .sorted(comparing(configClass -> configClass.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEachOrdered(this::processConfig);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        if (isNull(componentClass)) {
            return null;
        }
        List<Object> candidates = new ArrayList<>();
        for (Object component : appComponents) {
            if (component.getClass().equals(componentClass) || componentClass.isAssignableFrom(component.getClass())) {
                candidates.add(component);
            }
        }
        if (candidates.size() == 1) {
            return componentClass.cast(candidates.get(0));
        } else {
            if (candidates.isEmpty()) {
                throw new NoSuchComponentException(String.format(ERROR_COMPONENT_DOES_NOT_EXIST, componentClass.getSimpleName()));
            } else {
                StringBuilder message = new StringBuilder(String.format(ERROR_QUALIFIER_CANDIDATES, componentClass.getName()));
                for (var candidate : candidates) {
                    message.append("\n").append("-").append(candidate.getClass().getName());
                }
                throw new NoSuchComponentException(message.toString());
            }
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (isNull(componentName) || componentName.length() == 0) {
            return null;
        }
        return (C) ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new NoSuchComponentException(String.format(ERROR_COMPONENT_DOES_NOT_EXIST, componentName)));
    }

    private void processConfig(Class<?> configClass) {

        checkConfigClass(configClass);
        Object config = ReflectionUtils.getInstance(configClass);

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(f -> f.isAnnotationPresent(AppComponent.class))
                .sorted(comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> processConfigMethods(config, method));
    }

    private void processConfigMethods(Object config, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        AppComponent annotation = method.getAnnotation(AppComponent.class);
        String componentName = annotation.name();
        if (nonNull(appComponentsByName.get(componentName))) {
            throw new NoUniqueComponentException(String.format(ERROR_NOT_UNIQUE_COMPONENT, componentName));
        }
        Object methodResult = processMethod(config, method, parameterTypes);
        if (nonNull(methodResult)) {
            appComponents.add(methodResult);
            appComponentsByName.put(componentName, methodResult);
        }
    }

    private Object processMethod(Object config, Method method, Class<?>[] parameterTypes) {

        Object[] methodParams = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            methodParams[i] = getAppComponent(parameterTypes[i]);
        }
        return ReflectionUtils.invokeMethod(config, method, methodParams);
    }

    private void checkConfigClass(Class<?>... configClasses) {

        for (var configClass : configClasses) {
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format(ERROR_NON_CONFIG_CLASS, configClass.getName()));
            }
        }
    }
}