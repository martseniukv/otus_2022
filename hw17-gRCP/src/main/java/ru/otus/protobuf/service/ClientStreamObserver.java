package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.nonNull;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {

    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);
    private final CountDownLatch latch;
    private final AtomicLong lastValue = new AtomicLong(0);

    public ClientStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(NumberResponse value) {
        if (nonNull(value)) {
            var number = value.getNumber();
            log.info("new value: {}", number);
            lastValue.set(number);
        } else {
            log.info("ClientStreamObserver, method onNext response are null");
        }
    }

    @Override
    public void onError(Throwable t) {
        log.info("ClientStreamObserver, method onError: {}", t.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.info("ClientStreamObserver, method onCompleted");
        latch.countDown();
    }

    public long getLast(){
        return lastValue.getAndSet(0);
    }
}