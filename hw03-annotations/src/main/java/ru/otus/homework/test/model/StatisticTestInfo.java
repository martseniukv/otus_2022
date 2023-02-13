package ru.otus.homework.test.model;

public class StatisticTestInfo {

    private final int totalTest;
    private int failedTest;
    private int successTest;

    public StatisticTestInfo(int totalTest) {
        this.totalTest = totalTest;
    }
    public void incrementSuccess(){
        successTest++;
    }
    public void incrementFailed(){
        failedTest++;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public int getFailedTest() {
        return failedTest;
    }

    public int getSuccessTest() {
        return successTest;
    }
}