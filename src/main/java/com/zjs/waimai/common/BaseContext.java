package com.zjs.waimai.common;

/**
 * 基于ThreadLocal封装的工具类，实现用户保存和获取当前登录用户的ID
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentID(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrendId(){
        return threadLocal.get();
    }
}
