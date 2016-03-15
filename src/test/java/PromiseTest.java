import cn.hackingwu.promise.*;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

/**
 * promise的执行都是异步的，因此为了在测试中看到效果，让主线程sleep一段时间。
 * @author hackingwu.
 * @since 2016/3/10.
 */
public class PromiseTest {

    @After
    public void tearDown() throws Exception {
        Thread.currentThread().sleep(1000L);
    }

    @Test
    public void testPromise() {
        new Promise(new Resolver() {
            public void execute(OnFulfill<Object, Object> onFulfill, OnReject<Object, Object> onReject) throws Exception {
                int index = new Random().nextInt(2);
                if (index == 0) onFulfill.execute(index);
                else onReject.execute(index);
            }
        }).then(new OnFulfill<String, Integer>() {
            public String execute(Integer i) {
                return "success result : " + i;
            }
        }, new OnReject() {
            public Object execute(Object args) {
                return "error result : " + args;
            }
        }).then(new OnFulfill<Object, String>() {
            public Object execute(String args) {
                System.out.println(args);
                return null;
            }
        });
    }

    @Test
    public void testResolve() {
        Promise.resolve("1").then(new OnFulfill() {
            public Object execute(Object args) {
                System.out.println("last result : " + args);
                return null;
            }
        });
    }
    @Test
    public void testReject() {
        Promise.reject("1").then(new OnFulfill() {
            public Object execute(Object args) throws Exception {
                System.out.println("success result : " + args);
                return null;
            }
        }, new OnReject() {
            public Object execute(Object args) throws Exception {
                System.out.println("error result : " + args);
                return null;
            }
        });
    }

    @Test
    public void testException() {
        Promise.resolve("1").then(new OnFulfill() {
            public Object execute(Object args) throws Exception{
                throw new Exception("This is my exception, last result is "+args);
            }
        }).Catch(new OnReject<Object, Exception>() {
            public Object execute(Exception e) throws Exception {
                System.out.println(e.getMessage());
                return null;
            }
        });
    }

}
