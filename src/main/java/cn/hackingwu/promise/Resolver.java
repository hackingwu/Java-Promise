package cn.hackingwu.promise;

/**
 * @author hackingwu.
 * @since 2016/3/11.
 */
public interface Resolver {

    void execute(OnFulfill<Object, Object> onFulfill, OnReject<Object, Object> onReject) throws Exception;

}

