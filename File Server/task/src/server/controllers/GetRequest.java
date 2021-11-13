package server.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class GetRequest implements Request {
    private User user;

    public GetRequest(User userRequest) {
        this.user = userRequest;
    }

    @Override
    public void executeRequest(DataInputStream inputStream, DataOutputStream output) {
        user.get(inputStream, output);
    }
}
