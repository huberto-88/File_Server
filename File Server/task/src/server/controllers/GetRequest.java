package server.controllers;

public class GetRequest implements Request {
    private User user;

    public GetRequest(User userRequest) {
        this.user = userRequest;
    }

    @Override
    public String executeRequest() {
        return user.get();
    }
}
