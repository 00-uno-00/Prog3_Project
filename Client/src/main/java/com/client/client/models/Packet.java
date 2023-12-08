package com.client.client.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * Packet class that represents a packet of data sent between the client and the server.
 * The packet is serializable so that it can be sent over the network.
 * Protocol is yet to be defined but the overall structure is as follows:
 *     type:   the operation type (list of operations to be defined)
 *     object: the object to be sent (e.g.  User, Email, ArrayList<Email>)
 *     sender: the sender of the packet (to authenticate the sender)
 * The server receives a packet, reads the type, and performs the operation accordingly. (e.g. casts the object to the appropriate type)
 */
public class Packet implements Serializable{

    @Serial
    private static final long serialVersionUID = 6529685098267757691L;
    String operation;
    Object payload;

    String sender;

    /**
     * Constructor for the Packet class.
     * @param operation  the operation type (e.g. "login", "logged", "register", "send", "receive", "refresh", "delete")
     * @param payload    the object to be sent (e.g.  User, Email, ArrayList<Email>)
     * @param sender    the sender of the packet
     */
    public Packet(String operation, Object payload, String sender) {
        this.operation = operation; //operation type
        this.sender = sender; //for security reasons
        this.payload = payload; //payload
    }

    //Getters and Setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString(){
        return "Packet: " + operation + " " + payload + " " + sender;
    }
}