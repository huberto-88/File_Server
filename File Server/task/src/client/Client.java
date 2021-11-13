package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private final String SERVER_ADDRESS;
    private final int SERVER_PORT;
    private final Scanner scanner = new Scanner(System.in);
    private String path = "C:\\Users\\Hubert\\IdeaProjects\\File Server\\File Server\\task\\src\\client\\data\\";


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
            System.out.println("Enter action (1 - get a file, 2 - save a file, 3 - delete a file):");
            String request = scanner.nextLine().strip();

            if (request.equals("exit")) {
                socket.close();
                return;
            }

            switch (request) {
                case "1":
                    get(inputStream, outputStream);
                    break;
                case "2":
                    saveAFile(inputStream, outputStream);
                    break;
                case "3":
                    delete(inputStream, outputStream);
                    break;
                case "exit":
                    outputStream.writeUTF("exit");
                    System.out.println("The request was sent.");
                    socket.close();
                    return;
            }
            System.out.println("The request was sent.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void get(DataInputStream inputStream, DataOutputStream outputStream) {
        System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id):");
        int chosen = Integer.parseInt(scanner.nextLine());
        String type = "name ";
        if (chosen == 2) type = "id ";
        String idOrName = scanner.nextLine();

        try {
            outputStream.writeUTF("get " + type + idOrName);
            System.out.println("The request was sent.");
            String response = inputStream.readUTF();
            if (response.equals("200")) {
                int length = inputStream.readInt();
                byte[] bytes = new byte[length];
                inputStream.readFully(bytes, 0, bytes.length);

                System.out.println("The file was downloaded! Specify a name for it:");
                String name = scanner.nextLine();
                FileOutputStream fos = new FileOutputStream(path + name);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(bytes, 0, bytes.length);
                bos.flush();
                System.out.println("File saved on the hard drive!");

                fos.close();
                bos.close();
            } else if (response.equals("404")) {
                System.out.println("The response says that this file is not found!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAFile(DataInputStream inputStream, DataOutputStream outputStream) {
        System.out.println("Enter name of the file:");
        String nameClientFile = scanner.nextLine();
        System.out.println("Enter name of the file to be saved on server:");
        String nameToSave = scanner.nextLine();
        if (nameToSave.isEmpty()) {
            nameToSave = nameClientFile;
        }

        File file = new File(path + nameClientFile);
        byte[] bytes = new byte[(int) file.length()];

        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)
        ){
            outputStream.writeUTF("save name " + nameToSave);
            outputStream.writeInt((int) file.length());
            bis.read(bytes, 0, (int)file.length());
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            String serverResponse = inputStream.readUTF();
            if (serverResponse.contains("200")) {
                System.out.println("Response says that file is saved! ID = " + serverResponse.split("\\s+")[1]);
            } else {
                // if server return !200
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    private void delete(DataInputStream inputStream, DataOutputStream outputStream) {
        System.out.println("Do you want to delete the file by name or by id (1 - name, 2 - id):");
        int chosen = Integer.parseInt(scanner.nextLine());
        System.out.printf("Enter %s:\n", chosen == 1 ? "name" : "id");
        String idOrName = scanner.nextLine();

        try {
            outputStream.writeUTF("delete " + idOrName);
            System.out.println("The request was sent.");
            String response = inputStream.readUTF();
            if (response.equals("200")) {
                System.out.println("The response says that this file was deleted successfully!");
            } else {
                System.out.println("The response says that this file is not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
