package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> historyMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        ofNullable(msg).ifPresent(message -> historyMap.put(message.getId(), message.copy()));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return ofNullable(historyMap.get(id));
    }
}
