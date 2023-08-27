package com.zjs.waimai.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObiecthandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //因为此类无法获得session对象，因此需要使用ThreadLocal来获取线程的动态ID
        //为了更好的处理请求，建立了一个公共类，BaseContext
//        metaObject.setValue("createUser", new Long(1));
////        metaObject.setValue("updateUser", new Long(1));
        metaObject.setValue("createUser",BaseContext.getCurrendId());
        metaObject.setValue("updateUser",BaseContext.getCurrendId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("updateUser", BaseContext.getCurrendId());
    }
}
