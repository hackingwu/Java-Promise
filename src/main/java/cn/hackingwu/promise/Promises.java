package cn.hackingwu.promise;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author hackingwu.
 * @since 2016/3/10.
 */
class Promises {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void subscribe(final Promise promise) {
        if (!promise.getPromiseList().isPending()) {
            Future future = publish(promise);
            promise.getPromiseList().setFuture(future);
        }
    }

    public static Future publish(final Promise promise) {
        return executorService.submit(new Callable() {
            public Object call() throws Exception {
                PromiseList promiseList = promise.getPromiseList();
                promiseList.setPending(true);
                promiseList.setCurrentThread(Thread.currentThread());
                Queue<Promise> promiseQueue = promiseList.getPromises();
                Object value = promiseList.getValue();
                Boolean isError = promiseList.isError();
                OnReject onReject;
                OnFulfill onFulfill;
                Function executor;
                Promise currentPromise;
                Resolver resolve;
                while ((currentPromise = promiseQueue.poll()) != null) {
                    value = promiseList.getValue();
                    isError = promiseList.isError();
                    onReject = currentPromise.getOnReject();
                    onFulfill = currentPromise.getOnFulfill();
                    executor = isError ? onReject : onFulfill;
                    resolve = currentPromise.getResolve();
                    try {
                        if (resolve != null) {
                            resolve.execute(onResolve(currentPromise), onReject(currentPromise));
                        } else if (executor != null) {
                            value = executor.execute(value);
                            onResolve(currentPromise).execute(value);
                        }
                    } catch (Exception e) {
                        onReject(currentPromise).execute(e);
                    }
                    value = currentPromise.getPromiseList().getValue();
                    isError = currentPromise.getPromiseList().isError();
                }
                promiseList.setPending(false);
                return value;
            }
        });
    }

    private static OnFulfill onResolve(final Promise promise) {
        return new OnFulfill() {
            public Object execute(Object args) {
                promise.setStatus(PromiseStatus.FULFILLED);
                promise.getPromiseList().setValue(args);
                promise.getPromiseList().setError(false);
                return args;
            }
        };
    }

    private static OnReject onReject(final Promise promise) {
        return new OnReject() {
            public Object execute(Object args) {
                promise.setStatus(PromiseStatus.REJECTED);
                promise.getPromiseList().setValue(args);
                promise.getPromiseList().setError(true);
                return args;
            }
        };
    }

}
