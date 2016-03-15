package cn.hackingwu.promise;

/**
 * @author hackingwu.
 * @since 2016/3/8.
 */
public interface Function<R, P> {

    public R execute(P args) throws Exception;

}


