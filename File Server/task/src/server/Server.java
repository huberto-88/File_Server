package server;

import server.controllers.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Server {
    private final int PORT;
    private final Controller controller = new Controller();

    public Server(int port) {
        this.PORT = port;
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket serverSocket = server.accept();    //accepting new client
                        DataInputStream inputStream = new DataInputStream(serverSocket.getInputStream());
                        DataOutputStream output = new DataOutputStream(serverSocket.getOutputStream())
                ) {
                    String requestString = inputStream.readUTF();
                    System.out.println(requestString);
                    Request request = parseRequest(requestString);
                    if (Objects.isNull(request)) {
                        serverSocket.close();
                        break;
                    } else {
                        controller.setRequest(request);
                        controller.executeRequest(inputStream, output);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request parseRequest(String requestString) {
        String type = requestString.split("\\s+", 2)[0];
        User user = new User(requestString);
        switch (type) {
            case "get":
                return new GetRequest(user);
            case "save":
                return new CreateFileRequest(user);
            case "delete":
                return new DeleteRequest(user);
            case "exit":
                break;
        }
        return null;
    }

}
