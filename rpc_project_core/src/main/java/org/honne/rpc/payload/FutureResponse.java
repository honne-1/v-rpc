package org.honne.rpc.payload;

import lombok.Data;

@Data
public class FutureResponse {
    private Long id;
    private Object result;
    // code: 0000 - success, 1111 - fail, 2222 - timeout
    private String code = "0000";
    // fail message
    private String message;

}
