package cn.hackingwu.promise;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;

/**
 * @author hackingwu.
 * @since 2016/3/10.
 */
class PromiseList {
    private ArrayBlockingQueue<Promise> promises;

    private Object value;

    private Boolean isError = false;

    private Future future = null;
    private boolean pending = false;

    private Thread currentThread = null;

    public Thread getCurrentThread() {
        return currentThread;
    }

    public void setCurrentThread(Thread currentThread) {
        this.currentThread = currentThread;
    }

    public PromiseList() {
        this.promises = new ArrayBlockingQueue<Promise>(100);
    }

    public ArrayBlockingQueue<Promise> getPromises() {
        return promises;
    }

    public void add(Promise promise) {
        if (promise != null) this.promises.add(promise);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean isError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }
}

