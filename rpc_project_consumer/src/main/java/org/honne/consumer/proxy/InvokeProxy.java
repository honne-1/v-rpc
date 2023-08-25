package org.honne.consumer.proxy;

import org.honne.consumer.annotation.RemoteInvoke;
import org.honne.consumer.client.TCPClient;
import org.honne.consumer.payload.ClientRequest;
import org.honne.consumer.payload.FutureResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {
    public static Enhancer enhancer = new Enhancer();

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        // 获取所有的字段
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断字段是否有RemoteInvoke注解
            if(field.isAnnotationPresent(RemoteInvoke.class)){
                field.setAccessible(true);
//                final Map<Method, Class> methodClassMap = new HashMap<>();
//                putMethodClass(methodClassMap, field);
//                Enhancer enhancer = new Enhancer();
                // 设置代理类的父类
                enhancer.setInterfaces(new Class[]{field.getType()});
                // 设置回调函数 该函数会调用被代理类的方法
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        ClientRequest clientRequest = new ClientRequest();
                        clientRequest.setContent(args[0]);
//                        String command = methodClassMap.get(method).getName() + "." + method.getName();
                        String command = method.getName();
//                        System.out.println("command: " + command);
                        clientRequest.setCommand(command);
                        FutureResponse futureResponse = TCPClient.send(clientRequest);
                        return futureResponse;
                    }
                });
                try {
                    field.set(o, enhancer.create());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return o;
    }

    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for (Method method : methods) {
            methodClassMap.put(method, field.getType());
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
