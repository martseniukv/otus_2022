package ru.otus.homework.cache;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.otus.homework.cache.listeners.HwListener;

import java.lang.reflect.Field;
import java.util.List;
import java.util.WeakHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
class MyCacheTest {

    @Test
    @SneakyThrows
    void test_with_max_size_cache_listeners() {

        int cacheSize = 5;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        long statValue = 10000;
        long endValue = statValue + 100;
        for (long i = statValue; i < endValue; i++) {
            cache.put(i, String.valueOf(i));
            HwListener<Long, String> listener = new HwListener<>() {
                @Override
                public void notify(Long key, String value, String action) {
                    log.info("cache key: {}, value: {}, action: {}", key, value, action);
                }
            };
            cache.addListener(listener);
        }

        assertEquals(String.valueOf(statValue), cache.get(statValue));
        assertEquals(String.valueOf(statValue + cacheSize - 1), cache.get(statValue - 1 + cacheSize));
        assertNull(cache.get(statValue + cacheSize + 1));

        Field fieldListeners = cache.getClass().getDeclaredField("listeners");
        Field fieldMap = cache.getClass().getDeclaredField("cache");
        fieldListeners.setAccessible(true);
        fieldMap.setAccessible(true);
        List<HwListener<Long, String>> listeners = (List<HwListener<Long, String>>) fieldListeners.get(cache);
        WeakHashMap<Long, String> weakHashMap = (WeakHashMap<Long, String>) fieldMap.get(cache);
        assertEquals(5, listeners.size());
        assertEquals(5, weakHashMap.size());
    }

    @Test
    @SneakyThrows
    void check_cache_after_GC() {

        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        long statValue = 10000;
        long endValue = statValue + cacheSize;
        HwListener<Long, String> listener = (key, value, action) ->
                log.info("cache key: {}, value: {}, action: {}", key, value, action);

        cache.addListener(listener);

        for (long i = statValue; i < endValue; i++) {
            cache.put(i, String.valueOf(i));
        }

        System.gc();
        Thread.sleep(1000);

        assertNull(cache.get(statValue));
        assertNull(cache.get(statValue + 1));
        assertNull(cache.get(endValue - 1));
    }

    @Test
    @SneakyThrows
    void listener_as_anonymous_class() {

        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        HwListener<Long, String> listener = new HwListener<>() {
            @Override
            public void notify(Long key, String value, String action) {
                log.info("cache key: {}, value: {}, action: {}", key, value, action);
            }
        };
        Field fieldListeners = cache.getClass().getDeclaredField("listeners");
        fieldListeners.setAccessible(true);
        List<HwListener<Long, String>> listeners = (List<HwListener<Long, String>>) fieldListeners.get(cache);

        cache.addListener(listener);
        assertEquals(1, listeners.size());

        cache.removeListener(listener);
        assertEquals(0, listeners.size());
    }

    @Test
    @SneakyThrows
    void listener_as_lambda() {

        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);
        HwListener<Long, String> listener = (key, value, action) -> log.info("cache key: {}, value: {}, action: {}", key, value, action);

        Field fieldListeners = cache.getClass().getDeclaredField("listeners");
        fieldListeners.setAccessible(true);
        List<HwListener<Long, String>> listeners = (List<HwListener<Long, String>>) fieldListeners.get(cache);

        cache.addListener(listener);
        assertEquals(1, listeners.size());

        cache.removeListener(listener);
        assertEquals(0, listeners.size());
    }

    @Test
    @SneakyThrows
    void remove_with_key_null() {


        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        long key = 10001L;
        cache.put(key, String.valueOf(key));

        cache.remove(null);

        Field fieldMap = cache.getClass().getDeclaredField("cache");
        fieldMap.setAccessible(true);
        WeakHashMap<Long, String> weakHashMap = (WeakHashMap<Long, String>) fieldMap.get(cache);
        assertEquals(1, weakHashMap.size());
    }

    @Test
    @SneakyThrows
    void remove() {

        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        long key = 10001L;
        cache.put(key, String.valueOf(key));

        cache.remove(key);

        Field fieldMap = cache.getClass().getDeclaredField("cache");
        fieldMap.setAccessible(true);
        WeakHashMap<Long, String> weakHashMap = (WeakHashMap<Long, String>) fieldMap.get(cache);
        assertEquals(0, weakHashMap.size());
    }

    @Test
    @SneakyThrows
    void get_with_key_null() {


        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        long key = 10001L;
        cache.put(key, String.valueOf(key));

        assertNull(cache.get(null));
    }

    @Test
    @SneakyThrows
    void get() {

        int cacheSize = 10;
        int listenerSize = 5;
        HwCache<Long, String> cache = new MyCache<>(cacheSize, listenerSize);

        long key = 10001L;
        String value = String.valueOf(key);

        cache.put(key, value);

        assertEquals(value, cache.get(key));
    }
}