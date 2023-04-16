package ru.otus.homework.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.cache.listeners.HwListener;


public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>(5, 5);

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = (key, value, action) -> new HwListener<>() {
            @Override
            public void notify(Object key, Object value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }
}