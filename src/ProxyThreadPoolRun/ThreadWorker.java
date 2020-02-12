package ProxyThreadPoolRun;

public class ThreadWorker implements Runnable{

    @Override
    public void run() {
        System.out.println("Поток " + " " + Thread.currentThread().getName() + " Стартовал");

        System.out.println("Поток " + " " + Thread.currentThread().getName() + " Окончен");
    }
}
