package ru.otus.protobuf.service;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.NumberServiceGrpc;

import java.util.concurrent.CountDownLatch;

public class NumberClient {

    private static final Logger log = LoggerFactory.getLogger(NumberClient.class);

    private static final int SERVER_PORT = 8190;
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) throws InterruptedException {

        var chanel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = NumberServiceGrpc.newStub(chanel);
        var latch = new CountDownLatch(1);

        var numberRequest = getNumberRequest();
        var responseObserver = new ClientStreamObserver(latch);
        stub.getNumber(numberRequest, responseObserver);

        var currentValue = 0L;
        for (int i = 0; i < 50; i++) {
            currentValue = currentValue + responseObserver.getLast() + 1;
            log.info("currentValue: {}", currentValue);
            Thread.sleep(1000);
        }
        latch.await();
        chanel.shutdown();
    }

    private static NumberRequest getNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
    }
}