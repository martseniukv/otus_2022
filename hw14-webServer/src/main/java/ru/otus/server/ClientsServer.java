package ru.otus.server;

public interface ClientsServer {

    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}