package cn.hackingwu.promise.jdk8;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static cn.hackingwu.promise.jdk8.Promise.all;

/**
 * @Author: Jason Wu
 * @Date: 2020/5/8
 * @Description:
 */
public class IOPromise {

    static Executor executor;

    static {

        //因为Executors提供的newCachedThreadPool和singleThreadExecutor
        //允许请求的队列长度最大为Integer.MAX_VALUE，可能会堆积大量的请求，导致OOM
//        executor = Executors.newCachedThreadPool();
//        executor = Executors.newSingleThreadExecutor();
        executor = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), new PromiseThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

    }

    Executor getExecutor(){
        return this.getExecutor();
    }


    static class PromiseThreadFactory implements ThreadFactory {

        final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            //daemon的thread在运行不影响JVM关闭
            thread.setDaemon(true);
            thread.setName("IOPromise" + count.getAndIncrement());
            thread.setPriority(Thread.NORM_PRIORITY);
            return null;
        }
    }

    public static Supplier getSupplier(int i){
        return () -> {
            System.out.println("supplier"+i+": "+Thread.currentThread().getId());
            Supplier supplier1_1 = () ->{
                System.out.println("supplier"+i+"_1: "+Thread.currentThread().getId());
                return null;
            };
            Supplier supplier1_2 = () ->{
                System.out.println("supplier"+i+"_2: "+Thread.currentThread().getId());
                return null;
            };
            try {
                Promise.all(supplier1_1, supplier1_2).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public static void main(String[] args){
        Supplier supplier1 = getSupplier(1);
        Supplier supplier2 = getSupplier(2);
        Supplier supplier3 = getSupplier(3);

        all(supplier1, supplier2, supplier3);
    }

}
