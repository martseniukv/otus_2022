package ru.otus.homework.cache.listeners;


public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}