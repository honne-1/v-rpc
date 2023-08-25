package org.honne.rpc.medium;

import com.alibaba.fastjson.JSONObject;
import lombok.NoArgsConstructor;
import org.honne.rpc.payload.FutureResponse;
import org.honne.rpc.payload.ServerRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
@NoArgsConstructor
public class Medium {
    private static Medium medium = null;
    public static final Map<String, BeanMethod> beanMethodMap = new HashMap<>();

    public static Medium newInstance() {
        if (medium == null) {
            medium = new Medium();
        }
        return medium;
    }

    public FutureResponse process(ServerRequest request) {

        FutureResponse result = null;
        try {
            // 根据请求的命令获取对应的bean和方法
            BeanMethod beanMethod = beanMethodMap.get(request.getCommand());

            if (beanMethod == null) return null;
            // 反射调用方法
            Object bean = beanMethod.getBean();
            Method method = beanMethod.getMethod();
            Class<?> parameterType = method.getParameterTypes()[0];
            Object content = request.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), parameterType);
            // 将方法的返回值封装成FutureResponse对象
            result = (FutureResponse) method.invoke(bean, args);
            result.setId(request.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
