package modules;

import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

@Getter
public class ConnectionModule {
    private ServerSocketChannel serverSocketChannel;
    private Socket clientSocket = null;

    public ConnectionModule(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
    }

//    public void connect() throws IOException {
////        System.out.println("Waiting...");
//        clientSocket = serverSocket.accept();
//    }

//    public void disconnect() throws IOException {
//        serverSocket.close();
//    }

//    public boolean isConnected() {
//        return clientSocket.isConnected();
//    }
}
