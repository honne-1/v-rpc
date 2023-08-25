package org.honne.consumer.util;

import org.honne.consumer.payload.FutureResponse;

public class ResponseUtil {

    public static FutureResponse createSuccessResponse(Object result) {
        FutureResponse response = new FutureResponse();
        response.setResult(result);
        return response;
    }

    public static FutureResponse createFailResponse(String message) {
        FutureResponse response = new FutureResponse();
        response.setCode("1111");
        response.setMessage(message);
        return response;
    }

    public static FutureResponse createTimeoutResponse(long id) {
        FutureResponse response = new FutureResponse();
        response.setId(id);
        response.setCode("2222");
        response.setMessage("timeout");
        return response;
    }


}
