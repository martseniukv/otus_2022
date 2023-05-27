package ru.otus.protobuf.service;

import io.grpc.ServerBuilder;
import ru.otus.protobuf.generated.NumberServiceGrpc;

import java.io.IOException;

public class NumberServer {

    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        NumberServiceGrpc.NumberServiceImplBase numberService = new NumberServiceImpl();

        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(numberService)
                .build();

        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}