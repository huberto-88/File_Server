package server.controllers;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class User {
    private File file;
    private String path = "C:\\Users\\Hubert\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
    private ConcurrentMap<Integer, String> indexesMap;
    private String pathToMap = "C:\\Users\\Hubert\\IdeaProjects\\File Server\\File Server\\task\\src\\indexes";

    public User(String requestString) {
        readIndexMap();

        String[] requestParsed = requestString.split("\\s+", 3);
        if (requestParsed[1].equals("name")) {
            this.file = new File(path + requestParsed[2]);
        } else if (requestParsed[1].equals("id")) {
            this.file = new File(path + indexesMap.getOrDefault(Integer.parseInt(requestParsed[2]),
                    "idNotExists88"));
        }
    }

    public void get(DataInputStream inputStream, DataOutputStream output) {
        if (file.exists()) {
            try {
                output.writeUTF("200");
                output.writeInt((int) file.length());

                byte[] bytes = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytes, 0, (int) file.length());
                output.write(bytes, 0, (int) file.length());
                output.flush();
                saveIndexMap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                output.writeUTF("404");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createFile(DataInputStream inputStream, DataOutputStream output) {
        try {
            int length = inputStream.readInt();
            int id = new Random().nextInt(10000);
            byte[] bytes = new byte[length];

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            inputStream.readFully(bytes, 0, length);

            bos.write(bytes, 0, length);
            bos.flush();

            indexesMap.put(id, file.getName());
            output.writeUTF("200 " + id);

            saveIndexMap();
            bos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(DataInputStream inputStream, DataOutputStream output) {
        if (this.file.exists()) {
            if (this.file.delete()) {
                try {
                    output.writeUTF("200");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    output.writeUTF("404");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readIndexMap() {
        File file = new File(pathToMap);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);
                this.indexesMap = (ConcurrentMap<Integer, String>) ois.readObject();
                ois.close();

            } catch (IOException | ClassNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        } else {
            this.indexesMap = new ConcurrentHashMap<>();
        }
    }

    private void saveIndexMap() {
        try {
            File file = new File(pathToMap);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(indexesMap);
            oos.close();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}


