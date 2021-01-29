package by.epam.training.javaWEB.task04.client.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Logger logger = Logger.getLogger("ClientLog");
        FileHandler logFile;
        try {
            logFile = new FileHandler("LogFile.log");
            logger.addHandler(logFile);
            SimpleFormatter formatter = new SimpleFormatter();
            logFile.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        Map<Integer, String> requests = new LinkedHashMap<>();
        requests.put(1,"Get book");
        requests.put(2,"Get sentences sorted by order");
        requests.put(3, "Swap words in sentences");
        requests.put(0, "Exit");


        String hostName = "localhost";
        int portNumber = 4999;
        Socket client;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        //while (true) {
        for (Integer request : requests.keySet()) {
            try {
                client = new Socket(hostName, portNumber);
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                out.writeObject(request.toString());
                if (request == 0) {
                    return;
                }
                saveAsJson(in.readObject(), request + ".json");

            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }

    }

    public static void saveAsJson(Object object, String pathname) throws IOException {
        File jsonFile = new File(pathname);
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
            } catch (IOException ex) {
                throw new IOException(ex.getMessage());
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(jsonFile, object);
    }

    public static void saveAsTXT(Object object, String pathname) {

    }

}
