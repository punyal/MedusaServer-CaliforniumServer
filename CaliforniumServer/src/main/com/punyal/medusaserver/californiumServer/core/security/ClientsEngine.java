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

import com.punyal.medusaserver.californiumServer.utils.UnitConversion;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientsEngine extends Thread{
    private static final Logger LOGGER = Logger.getLogger(ClientsEngine.class.getCanonicalName());
    private boolean running;
    private ArrayList<Client> clientsList;
    
    public ClientsEngine() {
        running = false;
        clientsList = new ArrayList<>();
    }
    
    public void run() {
        running = true;
        
        while(running) {
            
            // CLEAN OLD TICKETS ===============================================
            long actualTime = (new Date()).getTime();
            while((!clientsList.isEmpty())  && (clientsList.get(0).getExpireTime() < actualTime)) {
                //System.out.print("Client " + clientsList.get(0).getUserName() + " expired!");
                Client tmp = clientsList.remove(0);
            }
            // CLEAN OLD TICKETS (END)==========================================
            
            // Sleep 1ms to prevent synchronization errors it's possible to remove with other code :)
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientsEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        LOGGER.log(Level.WARNING, "Thread [{0}] dying", ClientsEngine.class.getSimpleName());        
    }
    
    public void ShutDown() {
        this.running = false;
    }
    
    public synchronized void addClient(InetAddress address, String userName, String userPass, byte[] ticket, long expireTime) {
        clientsList.add(new Client(address, userName, userPass, ticket, expireTime));
    }
    public synchronized void addClient(Client client) {
        clientsList.add(client);
    }
    
    private synchronized void removeByClient(Client client) {
        clientsList.remove(client);
    }
    
    public synchronized Client getClient(String ticket) {
        int i=0;
        while(clientsList.size() > i) {
            try {
                if(UnitConversion.ByteArray2Hex(clientsList.get(i).getTicket()).equals(ticket))
                    return clientsList.get(i);
            } catch(NullPointerException e) {
                LOGGER.log(Level.WARNING, "Get Possible Clients By ticket exception {0}", e);
                return null;
            }
            i++;
        }
        return null;
    }
    
    public synchronized ArrayList<Client> getClientsList() {
        return clientsList;
    }
    
    public synchronized void printList(ArrayList<Client> list) {
        System.out.println("-------- Tickets --------");
        
        list.stream().forEach((client) -> {
            System.out.println("IP[" + client.getAddress() +
                    "] UserName[" + client.getUserName() +
                    "] UserPass[" + client.getUserPass()+
                    "] Ticket[" + UnitConversion.ByteArray2Hex(client.getTicket())+
                    "] ExpireTime[" + UnitConversion.Timestamp2String(client.getExpireTime())+
                    "]" );
        });
        
        System.out.println("-------------------------");
    }
}