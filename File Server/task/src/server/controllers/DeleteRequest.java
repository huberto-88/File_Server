package server.controllers;

public class DeleteRequest implements Request {
    private User user;

    public DeleteRequest(User user) {
        this.user = user;
    }

    @Override
    public String executeRequest() {
        return user.delete();
    }
}
