package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private final String SERVER_ADDRESS;
    private final int SERVER_PORT;
    private final Scanner scanner = new Scanner(System.in);


    public Client(String SERVER_ADDRESS, int SERVER_PORT) {
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.SERVER_PORT = SERVER_PORT;
    }

    public void run() {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
                String request = requestMenu();
                System.out.println("The request was sent.");
                outputStream.writeUTF(request);
               if (request.equals("exit")) {
                   socket.close();
               } else {
                   String response = inputStream.readUTF();
                   System.out.println(response);
               }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String requestMenu() {
        System.out.println("Enter action (1 - get a file, 2 - save a file, 3 - delete a file):");
        String request = scanner.nextLine().strip();
        if (request.equals("exit")) {
            return "exit";
        }
        System.out.println("Enter filename:");
        String fileName = scanner.nextLine();

        switch (request) {
            case "1":
                return get() + " " + fileName;
            case "2":
                return "create " + fileName + " " + create();
            case "3":
                return delete() + " " + fileName;
        }
        return "exit";
    }

    private String get() {
        return "get";
    }

    private String create() {
        System.out.println("Enter file content:");
        return scanner.nextLine();
    }

    private String delete() {
        return "delete";
    }
}
