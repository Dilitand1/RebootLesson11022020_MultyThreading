import TestClass.MyClass;

public class SimpleThread implements Runnable {
    MyClass myClass;

    public SimpleThread(MyClass myClass) {
        this.myClass = myClass;
    }

    @Override
    public void run() {
        try {
            myClass.print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
