package server.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Controller {
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    public void executeRequest(DataInputStream inputStream, DataOutputStream output) {
        request.executeRequest(inputStream, output);
    }
}
