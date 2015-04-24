/*******************************************************************************
 * MedusaServer - Multi Protocol Access Control Server
 * 
 * Copyright (c) 2015 - Pablo Puñal Pereira <pablo@punyal.com>
 *                      EISLAB : Luleå University of Technology
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 ******************************************************************************/
package com.punyal.medusaserver.californiumServer.core.security;

import java.net.InetAddress;
import java.util.Date;

public class Client  implements Comparable<Client>{
    private final InetAddress address;
    private String userName;
    private String userPass;
    private byte ticket[];
    private long expireTime;
            
    public Client(InetAddress address, String userName, String userPass, byte[] ticket, long expireTime) {
        this.address = address;
        this.userName = userName;
        this.userPass = userPass;
        this.ticket = ticket;
        this.expireTime = expireTime;
    }
    
    public InetAddress getAddress() {
        return address;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
    
    public String getUserPass() {
        return userPass;
    }
    
    public void setTicket(byte ticket[]) {
        this.ticket = ticket;
    }
    
    public byte[] getTicket() {
        return ticket;
    }
    
    
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
    public long getExpireTime() {
        return expireTime;
    }
    
    public boolean isValid() {
        Date date = new Date();
        return date.getTime() < expireTime;
    }
    
    @Override
    public int compareTo(Client t) {
        return (int)(expireTime - t.expireTime);
    }

}