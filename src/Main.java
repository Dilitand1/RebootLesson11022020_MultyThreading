import TestClass.MyClass;
import TestClass.MyInterface;
import ProxyThreadPoolRun.Handler;
import ThreadPool.MyThreadPool;

import java.lang.reflect.Proxy;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    static List<Thread> threadList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        //Пользовательский класс который будет работать многопоточно
        MyClass myClass = new MyClass("Тестовый текст будет печатать многопоточно в кашу");

        //1.Вариант простой:
        System.out.println("1 вариант");
        //задаем количество потоков
        int count = 10;
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(new SimpleThread(myClass));
            threadList.add(thread);
            thread.start();
        }
        //ждем когда все выполнится чтобы не пересечься со вторым вариантом
        while (true) {
            int x = 0;
            for (Thread thread : threadList) {
                x += thread.isAlive() ? 1 : 0;
            }
            if (x == 0) break;
        }

        //2 вариант через ExecutorService
        System.out.println("\n2 вариант");
        List<Future> futureList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            futureList.add(executorService.submit(new SimpleThread(new MyClass("Поток " + i + " из 2 варианта"))));
        }
        //ждем когда все выполнится чтобы не пересечься с 3 вариантом
        while (true) {
            Thread.sleep(3000);
            Integer optionalI = futureList.stream()
                    .map(x -> x.isDone() ? 0 : 1)
                    .reduce(0, (x, y) -> x + y);
            if (optionalI == 0) break;
            //ExecutorService не сразу завершает потоки, некоторое время ждет.
        }

        //3 вариант через проксю - самодеятельность, вспоминаем рефлексию
        //передаем наш класс в проксю и говорим сколько потоков будет запущено
        System.out.println("\n3 вариант");
        MyInterface myInterface = getProxy(myClass, 5);
        //Запускаем наш метод
        myInterface.print("Печатаем текст");

        //4 вариант каноничный с хабра
        TimeUnit.SECONDS.sleep(6);
        MyThreadPool threadPool = new MyThreadPool(4);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                try {
                    myClass.print("Тест №" + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static MyInterface getProxy(Object myClass, Integer countThreads) {
        Handler handler = new Handler(myClass, countThreads);
        return (MyInterface) Proxy.newProxyInstance(myClass.getClass().getClassLoader(), new Class[]{MyInterface.class}, handler);
    }
}
