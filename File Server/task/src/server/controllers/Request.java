package server.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Request {
    void executeRequest(DataInputStream inputStream, DataOutputStream output);
}
