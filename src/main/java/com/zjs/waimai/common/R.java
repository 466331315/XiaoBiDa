package com.zjs.waimai.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类，服务端的返回结果都会封装成此对象,@Data封装了get set tostring方法
 * @param <T>
 */
@Data
public class R<T>{
   private Integer code;//编码：1为成功
    private String msg;//错误信息
    private T data;
    private Map map=new HashMap();
    public static <T> R<T> success(T object){
        //泛型类的使用语法,类名<具体的数据类型> 对象名= new 类名<具体的数据类型>();
        //在java1.7以后<>中的具体的数据类型可以不写
        R<T> r =new R<>();
        r.data=object;
        r.code=1;
        return r;
    }
    public static <T> R<T> error(String msg){
        R r =new R<>();
        r.msg=msg;
        r.code=0;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
