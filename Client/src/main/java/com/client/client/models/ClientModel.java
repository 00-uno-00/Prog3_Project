package com.client.client.models;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientModel {

    private ExecutorService executorService;

    private CommsHandler commsHandler;

    private String email;


    public ClientModel() {
            try {
                executorService = Executors.newFixedThreadPool(2);
                commsHandler = new CommsHandler( executorService, email);
                System.out.println("Client model started");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    public boolean login(){
        try {
            return commsHandler.login();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(String email){
        try {
            return commsHandler.register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Email> refresh(List<Integer> emailIDs) {
        try {
            return commsHandler.refresh(emailIDs);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public void setEmail(String email){
        this.email = email;
        commsHandler.setEmail(email);
    }
}
