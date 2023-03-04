package ru.otus.proxy;

import ru.otus.log.Log;
import ru.otus.service.CalculationService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Ioc {

    private Ioc (){}

    public static CalculationService createCalculationService(CalculationService object) {
        if (isNull(object)) {
            throw new IllegalArgumentException("Can not create instance");
        }
        InvocationHandler handler = new LoggingInvocationHandler(object);
        return (CalculationService) newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{CalculationService.class}, handler);
    }

    private static class LoggingInvocationHandler implements InvocationHandler {

        private static final String METHOD_WITH_PARAM_FORMAT = "%s %s";
        private static final String LOG_MESSAGE = "%s, executed method: %s, with param: %s%n";

        private final Object originalClass;
        private final Set<String> logMethods;

        private LoggingInvocationHandler(Object originalClass) {
            this.originalClass = originalClass;
            final Class<?> aClass = originalClass.getClass();
            logMethods = Arrays.stream(aClass.getDeclaredMethods())
                    .filter(method -> nonNull(method.getAnnotation(Log.class)))
                    .map(this::getMethodWithParameters)
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            final String methodName = getMethodWithParameters(method);
            if (logMethods.contains(methodName)){
                System.out.printf(LOG_MESSAGE, originalClass.getClass().getName(), method.getName(), Arrays.toString(args));
            }
            return method.invoke(originalClass, args);
        }

        private String getMethodWithParameters(Method method) {
            return format(METHOD_WITH_PARAM_FORMAT, method.getName(), Arrays.toString(method.getParameters()));
        }
    }
}