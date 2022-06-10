package modules;

import interaction.Response;
import lombok.Getter;
import utils.Serializator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Getter
public class ResponseSenderModule implements Runnable {

    SocketChannel client;
    Response response;

    public ResponseSenderModule(SocketChannel client, Response response) {
        this.client = client;
        this.response = response;
    }

    @Override
    public void run() {
        byte[] output = Serializator.serializeObject(response);
        ByteBuffer buffer = ByteBuffer.wrap(output);
        try {
            while (client.write(buffer) > 0) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
