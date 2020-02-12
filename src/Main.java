import TestClass.MyClass;
import TestClass.MyInterface;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import ProxyThreadPoolRun.Handler;

public class Main {

    static List<Thread> threadList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        //Создаем наш класс
        MyClass myClass = new MyClass("Тестовый текст будет печатать многопоточно в кашу");
        System.out.println("1 вариант");
        //1.Вариант простой:
        //задаем количество потоков
        Integer count = 10;
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

        System.out.println("2 вариант");
        //2.вариант через проксю - самодеятельность, попытка универсализировать
        //передаем наш класс в проксю и говорим сколько потоков будет запущено
        MyInterface myInterface = getProxy(myClass,10);

        //Запускаем наш метод
        myInterface.print(String.valueOf("Печатаем текст"));
    }

    static MyInterface getProxy(Object myClass,Integer countThreads){
        Handler handler = new Handler(myClass,countThreads);
        return  (MyInterface)Proxy.newProxyInstance(myClass.getClass().getClassLoader(),new Class[]{MyInterface.class},handler);
    }
}
