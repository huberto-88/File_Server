package server.controllers;

public class Controller {
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    public String executeRequest() {
        return request.executeRequest();
    }
}
