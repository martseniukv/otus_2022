package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.NumberResponse;
import ru.otus.protobuf.generated.NumberServiceGrpc;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumberServiceImpl extends NumberServiceGrpc.NumberServiceImplBase {

    private static final int PERIOD = 2;
    private static final int INITIAL_DELAY = 0;

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    @Override
    public void getNumber(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {

        var firstValue = new AtomicLong(request.getFirstValue());
        var lastValue = request.getLastValue();
        Runnable runnable = () -> {
            var number = firstValue.incrementAndGet();
            NumberResponse response = NumberResponse.newBuilder().setNumber(number).build();
            responseObserver.onNext(response);
            if (number == lastValue) {
                executorService.shutdown();
                responseObserver.onCompleted();
            }
        };
        executorService.scheduleAtFixedRate(runnable, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }
}