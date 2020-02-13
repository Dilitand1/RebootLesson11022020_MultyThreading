package ThreadPool;

import java.util.Queue;
import java.util.concurrent.*;

public class MyThreadPool {
    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private Boolean isShutdown;

    public MyThreadPool(int countThreads) {
        this.isShutdown = false;
        for (int i = 0; i < countThreads; i++) {
            Thread t = new Thread(new ThreadWorker(this));
            t.start();
        }
    }

    public void setShutdown(Boolean shutdown) {
        isShutdown = shutdown;
    }

    public void execute(Runnable command) {
        if (!isShutdown) {
            queue.offer(command);
        }
    }

    public Queue<Runnable> getQueue() {
        return queue;
    }

    public Boolean getShutdown() {
        return isShutdown;
    }
}

class ThreadWorker implements Runnable {
    MyThreadPool myThreadPool;

    public ThreadWorker(MyThreadPool myThreadPool) {
        this.myThreadPool = myThreadPool;
    }

    @Override
    public void run() {
        while (!myThreadPool.getShutdown()) {
            Runnable runnable = myThreadPool.getQueue().poll();
            if (runnable != null) {
                runnable.run();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
