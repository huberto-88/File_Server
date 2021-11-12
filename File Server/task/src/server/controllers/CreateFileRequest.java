package server.controllers;

public class CreateFileRequest implements Request {
    private User user;

    public CreateFileRequest(User user) {
        this.user = user;
    }

    @Override
    public String executeRequest() {
        return user.createFile();
    }
}
