package modules;

import interaction.Response;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

@Getter
public class ResponseSenderModule implements Runnable {
    private final ObjectOutputStream writer;
    @Setter
    private Response res;

    public ResponseSenderModule(OutputStream writer) throws IOException {
        this.writer = new ObjectOutputStream(writer);
    }

    public void sendResponse(Response<?> response) throws IOException {
        writer.writeObject(response);
        writer.flush();
    }

    public void sendResponse() throws IOException {
        writer.writeObject(res);
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }


    @Override
    public void run() {
        try {
            sendResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
