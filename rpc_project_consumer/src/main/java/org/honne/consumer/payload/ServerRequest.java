package org.honne.consumer.payload;

import lombok.Data;

@Data
public class ServerRequest {
    private Long id;
    private Object content;

    private String command;
}
