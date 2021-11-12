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
    private Controller controller = new Controller();


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
                    Request request = parseRequest(requestString);
                    if (Objects.isNull(request)) {
                        serverSocket.close();
                        break;
                    } else {
                        controller.setRequest(request);
                        output.writeUTF(controller.executeRequest());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Request parseRequest(String requestString) {
        String[] requestParsed = requestString.split("\\s+", 3);
        switch (requestParsed[0]) {
            case "get":
                return new GetRequest(new User(requestParsed[1]));
            case "create":
                return new CreateFileRequest(new User(requestParsed[1], requestParsed[2]));
            case "delete":
                return new DeleteRequest(new User(requestParsed[1]));
            case "exit":
                break;
        }
        return null;
    }

}
