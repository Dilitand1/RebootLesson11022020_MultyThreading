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
            myClass.print();
            Thread.sleep((long) (Math.random()*10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Поток " + Thread.currentThread().getName() + " завершен");
        }
    }
}
