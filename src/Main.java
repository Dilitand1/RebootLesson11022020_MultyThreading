import TestClass.MyClass;
import TestClass.MyInterface;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ProxyThreadPoolRun.Handler;

public class Main {

    static List<Thread> threadList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        //Пользовательский класс который будет работать многопоточно
        MyClass myClass = new MyClass("Тестовый текст будет печатать многопоточно в кашу");

        //1.Вариант простой:
        System.out.println("1 вариант");
        //задаем количество потоков
        int count = 10;
        for(int i = 0 ;i < count;i++) {
            Thread thread = new Thread(new SimpleThread(myClass));
            threadList.add(thread);
            thread.start();
        }
        //ждем когда все выполнится чтобы не пересечься со вторым вариантом
        while (true){
            int x = 0;
            for(Thread thread : threadList){
                x += thread.isAlive() ? 1 : 0;
            }
            if (x == 0) break;
        }

        //2 вариант через ExecutorService
        System.out.println("\n2 вариант");
        List<Future> futureList = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0 ;i < 10;i++){
            futureList.add(executorService.submit(new SimpleThread(new MyClass("Потоки из 2 варианта"))));
        }
        while (true){
            Thread.sleep(3000);
            Integer optionalI = futureList.stream()
                    .map(x -> x.isDone() ? 0 : 1)
                    .reduce(0,(x,y) -> x+y);
            if (optionalI == 0) break;
            //ExecutorService не сразу завершает потоки, некоторое время ждет.
        }

        //3 вариант через проксю - самодеятельность, попытка универсализировать
        //передаем наш класс в проксю и говорим сколько потоков будет запущено
        System.out.println("\n3 вариант");
        MyInterface myInterface = getProxy(myClass,10);
        //Запускаем наш метод
        myInterface.print("Печатаем текст");
    }

    static MyInterface getProxy(Object myClass,Integer countThreads){
        Handler handler = new Handler(myClass,countThreads);
        return  (MyInterface)Proxy.newProxyInstance(myClass.getClass().getClassLoader(),new Class[]{MyInterface.class},handler);
    }
}
