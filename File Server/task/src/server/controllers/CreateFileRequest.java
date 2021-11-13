package server.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CreateFileRequest implements Request {
    private User user;

    public CreateFileRequest(User user) {
        this.user = user;
    }

    @Override
    public void executeRequest(DataInputStream inputStream, DataOutputStream output) {
         user.createFile(inputStream, output);
    }
}
