package org.example.clipflow.holder;

public class UserHolder {
    private final static ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static void setUserId(Long id) {
        userThreadLocal.set(id);
    }
    public static Long getUserId() {
        return userThreadLocal.get();
    }
    public static void clear() {
        userThreadLocal.remove();
    }
}
