package SimpleThread;

import TestClass.MyClass;

import java.util.Random;

public class SimpleThread implements Runnable {
    MyClass myClass;

    public SimpleThread(MyClass myClass) {
        this.myClass = myClass;
    }

    @Override
    public void run() {
        try {
            System.out.println("Поток " + Thread.currentThread().getName() + " запущен");
            Thread.sleep((long) (Math.random() * 3000) + 1000);
            myClass.print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Поток " + Thread.currentThread().getName() + " завершен");
        }
    }
}
