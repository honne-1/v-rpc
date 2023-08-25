package org.honne.rpc.payload;

import lombok.Data;
/**
 * 服务端请求
 */

@Data
public class ServerRequest {
    private Long id;
    private Object content;

    private String command;
}
