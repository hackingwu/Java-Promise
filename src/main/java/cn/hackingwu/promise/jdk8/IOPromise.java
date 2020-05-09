package cn.hackingwu.promise.jdk8;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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

}
