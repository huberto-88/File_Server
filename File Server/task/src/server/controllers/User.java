package server.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class User {
    private File file;
    private String path = "C:\\Users\\Hubert\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
    private StringBuilder fileContent;


    public User(String fileName, String fileContent) {
        this.file = new File(path + fileName);
        this.fileContent = new StringBuilder(fileContent);
    }

    public User(String fileName) {
        this(fileName, "");
    }

    public String get() {
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    fileContent.append(scanner.nextLine());
                }
                scanner.close();
                return "The content of the file is: " + fileContent.toString();
            } catch (FileNotFoundException ignored) {}
        }
        return "The response says that the file was not found!";
    }

    public String createFile() {
        if (file.exists()) {
            return "The response says that creating the file was forbidden!";
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContent.toString());
            fileWriter.close();
            return "The response says that file was created!";
        } catch (IOException ioException) {
            return "Fail! File not created! :(";
        }
    }

    public String delete() {
        if (this.file.exists()) {
            if (this.file.delete()) {
                return "The response says that the file was successfully deleted!";
            } else {
                return "File unluckily wasn't deleted...";
            }
        } else {
            return "The response says that the file was not found!";
        }
    }
}
