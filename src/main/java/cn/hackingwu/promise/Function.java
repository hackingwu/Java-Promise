package cn.hackingwu.promise;

/**
 * 用Function接口,在外部调用使用匿名类的写法,实现类函数式编程.
 * new OnFulfill() {
 *   public Object execute(Object args) {
 *      System.out.println("last result : " + args);
 *      return null;
 *   }
 * }
 * @author hackingwu.
 * @since 2016/3/8.
 */
public interface Function<R, P> {

    public R execute(P args) throws Exception;

}


