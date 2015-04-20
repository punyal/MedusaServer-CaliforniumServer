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

import java.util.Date;

public class Ticket {
    private byte ticket[];
    private String authenticator;
    private long expireTime;
            
    public Ticket() {
        ticket = null;
        expireTime = 0;
        this.authenticator = null;
    }
    
    public void setTicket(byte ticket[]) {
        this.ticket = ticket;
    }
    
    public byte[] getTicket() {
        return ticket;
    }
    
    public String getAuthenticator() {
        return authenticator;
    }
    
    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
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
    
}