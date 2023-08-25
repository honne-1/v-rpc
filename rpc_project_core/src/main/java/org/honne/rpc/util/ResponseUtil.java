package org.honne.rpc.util;

import org.honne.rpc.payload.FutureResponse;

public class ResponseUtil {

    /**
     * 创建成功的响应
     *
     * @param result 消息
     * @return FutureResponse
     */
    public static FutureResponse createSuccessResponse(Object result) {
        FutureResponse response = new FutureResponse();
        response.setResult(result);
        return response;
    }

    /**
     * 创建失败的响应
     *
     * @param message 消息
     * @return FutureResponse
     */

    public static FutureResponse createFailResponse(String message) {
        FutureResponse response = new FutureResponse();
        response.setCode("1111");
        response.setMessage(message);
        return response;
    }

    /**
     * 创建超时的响应
     *
     * @param id 消息id
     * @return FutureResponse
     */
    public static FutureResponse createTimeoutResponse(long id) {
        FutureResponse response = new FutureResponse();
        response.setId(id);
        response.setCode("2222");
        response.setMessage("timeout");
        return response;
    }


}
