package com.client.client.models;

import com.client.client.controllers.ClientController;

import java.util.ArrayList;

public class PacketHandler implements Runnable {
    private final Packet packet;
    private final ClientController controller;

    public PacketHandler(Packet packet, ClientController controller) {
        this.packet = packet;
        this.controller = controller;
    }

    @Override
    public void run() {
        switch (packet.getType()) {
            case "mail":
                if (packet.getContent() instanceof ArrayList) {
                    for (Object email: (ArrayList<?>) packet.getContent()) {
                         if (email instanceof Email) {
                             controller.handleEmail((Email) email);
                             //TODO handle email
                         } else {
                             break;
                         }

                    }
                }
                //TODO handle email
                break;
            case "user":

                break;
            default:
        }
    }
}