package cn.hackingwu.promise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author hackingwu.
 * @since 2016/3/8.
 */
public class Promise {
    private PromiseStatus status = PromiseStatus.PENDING;

    private OnFulfill onFulfill;

    private OnReject onReject;

    private PromiseList promiseList;

    private Resolver resolve;

    public Promise(PromiseList promiseList) {
        this.promiseList = promiseList;
        this.promiseList.add(this);
    }

    public Promise(Resolver resolve) {
        this.promiseList = new PromiseList();
        this.resolve = resolve;
        this.promiseList.add(this);
        Promises.subscribe(this);
    }

    public static Promise resolve(final Object value) {
        if (value instanceof Promise) return (Promise)value;
        return new Promise(new Resolver() {
            public void execute(OnFulfill<Object, Object> onFulfill, OnReject<Object, Object> onReject) throws Exception {
                onFulfill.execute(value);
            }
        });
    }

    public static Promise reject(final Object value) {
        if (value instanceof Promise) return (Promise)value;
        return new Promise(new Resolver() {
            public void execute(OnFulfill<Object, Object> onFulfill, OnReject<Object, Object> onReject) throws Exception {
                onReject.execute(value);
            }
        });
    }
    public static Promise all(Collection<Promise> promises) {
        List result = new ArrayList(promises.size());
        for (Promise promise : promises) {
            try {
                result.add(promise.getPromiseList().getFuture().get());
            } catch (InterruptedException | ExecutionException e) {
                result.add(e);
            }
        }
        return resolve(result);
    }


    public static Promise all(Promise... promises) {
        return all(Arrays.asList(promises));
    }

    public static Promise race(Promise... promises) {
        return race(Arrays.asList(promises));
    }

    public static Promise race(Collection<Promise> promises) {
        Future future = null;
        while (true){
            for (Promise promise : promises){
                future = promise.getPromiseList().getFuture();
                if (future.isDone()){
                    try {
                        Object object = future.get();
                        return resolve(object);
                    } catch (InterruptedException | ExecutionException e) {
                        return reject(e);
                    }
                } else if (future.isCancelled()) {
                    return reject(null);
                }
            }
        }
    }

    public Object get() throws ExecutionException, InterruptedException {
        Future future = getPromiseList().getFuture();
        return future.get();
    }

    public Promise then(OnFulfill onFulfill, OnReject onReject) {
        Promise next = new Promise(this.promiseList);
        next.onFulfill = onFulfill;
        next.onReject = onReject;
        Promises.subscribe(next);
        return next;
    }

    public Promise then(OnFulfill onFulfill) {
        return then(onFulfill, null);
    }

    public Promise Catch(OnReject onReject) {
        return then(null, onReject);
    }

    protected Resolver getResolve() {
        return resolve;
    }

    protected PromiseList getPromiseList() {
        return promiseList;
    }

    protected OnFulfill getOnFulfill() {
        return onFulfill;
    }

    protected OnReject getOnReject() {
        return onReject;
    }

    protected PromiseStatus getStatus() {
        return status;
    }

    protected void setStatus(PromiseStatus status) {
        this.status = status;
    }
}
