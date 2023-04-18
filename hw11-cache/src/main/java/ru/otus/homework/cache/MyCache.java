package ru.otus.homework.cache;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.cache.listeners.HwListener;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.otus.homework.cache.listeners.Action.*;

@Slf4j
@ToString
public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K, V> cache;
    private final List<HwListener<K, V>> listeners;
    private final int maxCacheSize;
    private final int maxListenerSize;

    public MyCache(int maxCacheSize, int maxListenerSize) {
        this.cache = new WeakHashMap<>(maxCacheSize);
        this.listeners = new ArrayList<>(maxListenerSize);
        this.maxCacheSize = maxCacheSize;
        this.maxListenerSize = maxListenerSize;
    }

    @Override
    public void put(K key, V value) {
        if (isNull(key) || isNull(value) || cache.size() >= maxCacheSize) {
            return;
        }
        notifyListeners(key, value, PUT.getName());
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        if (isNull(key)) {
            return;
        }
        final V removeValue = cache.remove(key);
        notifyListeners(key, removeValue, REMOVE.getName());
    }

    @Override
    public V get(K key) {
        if (isNull(key)) {
            return null;
        }
        final V value = cache.get(key);
        notifyListeners(key, value, GET.getName());
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (isNull(listener) || listeners.size() >= maxListenerSize) {
            return;
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        if (isNull(listener)) {
            return;
        }
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String get) {
        listeners.forEach(hwListener -> {
            try {
                if (nonNull(hwListener)) {
                    hwListener.notify(key, value, get);
                }
            } catch (Exception e) {
                log.error("Error during notify listener");
            }
        });
    }
}