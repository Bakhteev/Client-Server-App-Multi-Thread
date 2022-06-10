package modules;

import interaction.Request;
import lombok.Getter;
import utils.Serializator;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

@Getter
public class RequestHandlerModule implements Callable<Request> {

    SocketChannel client;

    public RequestHandlerModule(SocketChannel client) {
        this.client = client;
    }

    @Override
    public Request call() throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(5000);
        client.read(buffer);
        return Serializator.deserializeObject(buffer.array());
    }
}
