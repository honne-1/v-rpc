package org.honne.rpc.medium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanMethod {
    private Object bean;
    private Method method;
}
