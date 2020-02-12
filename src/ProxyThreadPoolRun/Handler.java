package ProxyThreadPoolRun;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Handler implements InvocationHandler, Runnable {
    final Object realObject;
    Integer countThreads;

    private Method method_;
    private Object[] args_;

    public Handler(Object realObject, Integer countThreads) {
        this.realObject = realObject;
        this.countThreads = countThreads;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //определяем переменные метода
        List<Class> classes = Arrays.stream(args).map(x -> (Class) x.getClass()).collect(Collectors.toList());
        //запускаем метод
        for (int i = 0; i < countThreads; i++) {
            method_ = realObject.getClass().getDeclaredMethod("print", classes.toArray(new Class[0]));
            args_ = args;
            Thread thread = new Thread(this);
            thread.start();
            Thread.sleep(1000);
        }
        return null;
    }

    @Override
    public void run() {
        synchronized (realObject) {
            System.out.println("Поток номер " + Thread.currentThread().getName() + " запущен");
            try {
                method_.invoke(realObject, args_);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Поток номер " + Thread.currentThread().getName() + " окончен");
            }
        }
    }
}