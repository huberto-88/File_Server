package server.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DeleteRequest implements Request {
    private User user;

    public DeleteRequest(User user) {
        this.user = user;
    }

    @Override
    public void executeRequest(DataInputStream inputStream, DataOutputStream output) {
        user.delete(inputStream, output);
    }
}
