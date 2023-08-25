package org.honne.rpc.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class ClientRequest {
    private final long id;
    private Object content;
    private final AtomicLong atomicId = new AtomicLong(0);
    private String command;


    public ClientRequest() {
        this.id = atomicId.incrementAndGet();
    }


}
