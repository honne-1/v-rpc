package org.honne.rpc.medium;

import org.honne.rpc.annotation.Remote;
import org.honne.rpc.annotation.RemoteInvoke;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class InitialMedium implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        // 如果bean上有Remote注解，说明是远程调用的接口，需要将其注册到Medium中
        if(o.getClass().isAnnotationPresent(Remote.class)){
            Method[] methods = o.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String key = method.getName();
                Map<String, BeanMethod> beanMethodMap = Medium.beanMethodMap;
                BeanMethod beanMethod = new BeanMethod(o, method);
                beanMethodMap.put(key, beanMethod);
//                System.out.println("key: " + key);
            }
        }
        return o;
    }
}
