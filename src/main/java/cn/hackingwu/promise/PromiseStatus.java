package cn.hackingwu.promise;

/**
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
