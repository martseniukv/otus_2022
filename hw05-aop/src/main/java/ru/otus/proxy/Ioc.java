package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Objects.isNull;

public class Ioc {

    private Ioc (){}

    public static <T, V extends T> T getProxyInstance(Class<T> targetInterface, V targetClass) {

        if (isNull(targetClass) || isNull(targetInterface)) {
            throw new IllegalArgumentException("Can not create instance");
        }
        InvocationHandler handler = new LoggingInvocationHandler(targetClass);
        final Object proxyInstance = newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{targetInterface}, handler);
        return targetInterface.cast(proxyInstance);
    }
}