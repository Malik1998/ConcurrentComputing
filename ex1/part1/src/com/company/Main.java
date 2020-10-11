package com.company;

public class Main {

    public static void checkImpls(int countThreads, Barier barier) throws InterruptedException {
        Thread[] threads = new Thread[countThreads];

        for(int i = 0; i < countThreads; i++) {
            threads[i] = new ThreadCounter(barier, i, countThreads);
           // System.out.println("Thread " + String.valueOf(i + 1) + " created!");
        }

        for(int i = 0; i < countThreads; i++) {
            threads[i].start();
           // System.out.println("Thread " + String.valueOf(i + 1) + " started!");
        }

        for (Thread thread : threads) {
            thread.join();
        }
        // System.out.println("All threads executed!");
        barier.Reset(countThreads);
    }

    public static void StartExp(int countThreads, Barier barier, int index) throws InterruptedException {

        // Maybe not fair to execute just from start program, so lets call multiply times
        Main.checkImpls(countThreads, barier);
        Main.checkImpls(countThreads, barier);
        Main.checkImpls(countThreads, barier);


        long start = System.currentTimeMillis();
        Main.checkImpls(countThreads, barier);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(String.valueOf(index) + " experiment TimeElapsed " + String.valueOf(timeElapsed) +
                " thread Count = " + String.valueOf(countThreads));
    }

    public static void main(String[] args) throws InterruptedException {

        for(int countThreads = 5; countThreads <= 100; countThreads += 5) {
            TTasCounter counter = new TTasCounter(countThreads);
            StartExp(countThreads, counter, 1);

            ArrayBarrier arrayBarrier = new ArrayBarrier(countThreads);
            StartExp(countThreads, arrayBarrier, 2);
        }

    }

//1 experiment TimeElapsed 1 thread Count = 5
//2 experiment TimeElapsed 1 thread Count = 5
//1 experiment TimeElapsed 46 thread Count = 10
//2 experiment TimeElapsed 48 thread Count = 10
//1 experiment TimeElapsed 52 thread Count = 15
//2 experiment TimeElapsed 229 thread Count = 15
//1 experiment TimeElapsed 558 thread Count = 20
//2 experiment TimeElapsed 641 thread Count = 20
//1 experiment TimeElapsed 1244 thread Count = 25
//2 experiment TimeElapsed 1356 thread Count = 25
//1 experiment TimeElapsed 1376 thread Count = 30
//2 experiment TimeElapsed 2177 thread Count = 30
//1 experiment TimeElapsed 3269 thread Count = 35
//2 experiment TimeElapsed 3808 thread Count = 35
//1 experiment TimeElapsed 4154 thread Count = 40
//2 experiment TimeElapsed 4031 thread Count = 40
//1 experiment TimeElapsed 3242 thread Count = 45
//2 experiment TimeElapsed 9144 thread Count = 45
//1 experiment TimeElapsed 4587 thread Count = 50
//2 experiment TimeElapsed 5613 thread Count = 50
//1 experiment TimeElapsed 9671 thread Count = 55
//2 experiment TimeElapsed 12294 thread Count = 55
//1 experiment TimeElapsed 13451 thread Count = 60
//2 experiment TimeElapsed 12603 thread Count = 60
//1 experiment TimeElapsed 14189 thread Count = 65
//2 experiment TimeElapsed 17418 thread Count = 65
//1 experiment TimeElapsed 18002 thread Count = 70
//2 experiment TimeElapsed 20868 thread Count = 70
}
