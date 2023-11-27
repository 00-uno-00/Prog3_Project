package com.server.server;

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
    String type;
    Object object;

    String sender;

    /**
     * Constructor for the Packet class.
     * @param type  the operation type (e.g. "login", "register", "send", "receive", "refresh")
     * @param object    the object to be sent (e.g.  User, Email, ArrayList<Email>)
     * @param sender    the sender of the packet
     */
    public Packet (String type, Object object, String sender) {
        this.type = type;
        this.sender = sender;
        this.object = object;
    }

    //Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}