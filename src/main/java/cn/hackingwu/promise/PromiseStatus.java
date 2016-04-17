package cn.hackingwu.promise;

/**
 * 标识Promise对象的三个状态,PENDING表示正在执行中,FULFILLED表示已经成功执行完成,REJECTED表示已经执行失败.
 * @author hackingwu.
 * @since 2016/3/8.
 */
enum PromiseStatus {
    PENDING("PENDING"),
    FULFILLED("FULFILLED"),
    REJECTED("REJECTED");

    private String value;

    PromiseStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
