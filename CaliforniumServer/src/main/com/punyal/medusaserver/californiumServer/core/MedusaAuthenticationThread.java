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
package com.punyal.medusaserver.californiumServer.core;

import static com.punyal.medusaserver.californiumServer.core.MedusaConfiguration.*;
import com.punyal.medusaserver.californiumServer.core.security.Cryptonizer;
import com.punyal.medusaserver.californiumServer.core.security.Ticket;
import com.punyal.medusaserver.californiumServer.utils.UnitConversion;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MedusaAuthenticationThread extends Thread{
    private static final Logger LOGGER = Logger.getLogger(MedusaAuthenticationThread.class.getCanonicalName());
    private boolean running;
    private Ticket ticket;
    private CoapClient coapClient;
    private String medusaServerAddress = null;
    private String medusaSecretKey = null;
    private String medusaUserName = null;
    private String medusaUserPass = null;
    
    public MedusaAuthenticationThread(String serverAddress, String secretKey, String userName, String userPass) {
        running = false;
        medusaServerAddress = serverAddress;
        medusaSecretKey = secretKey;
        medusaUserName = userName;
        medusaUserPass = userPass;
        ticket = new Ticket();
        coapClient = new CoapClient();
    }
    
    @Override
    public void run() {
        running = true;
        LOGGER.log(Level.INFO, "Thread [{0}] running", MedusaAuthenticationThread.class.getSimpleName());
        CoapResponse response;
                
        while(running) {
            if(ticket.isValid()) {
                //System.out.println("Valid Ticket");
            } else {
                //System.out.println("Not Valid Ticket");
                coapClient.setURI(medusaServerAddress+"/"+SERVER_AUTHENTICATION_SERVICE_NAME);
                response = coapClient.get();
                
                if (response!=null) {
                    try {
                        JSONObject json = (JSONObject)JSONValue.parse(response.getResponseText());
                        ticket.setAuthenticator(json.get(JSON_AUTHENTICATOR).toString());
                        //System.out.println(ticket.getAuthenticator());


                        json.clear();
                        json.put(JSON_USER_NAME, medusaUserName);
                        json.put(JSON_USER_PASSWORD, Cryptonizer.encrypt(medusaSecretKey, ticket.getAuthenticator() , medusaUserPass));
                        //System.out.println(json.toString());

                        response = coapClient.put(json.toString(), 0);

                        if(response!=null) {
                            //System.out.println(response.getResponseText());
                            json.clear();
                            System.out.println("["+response.getResponseText()+"]");
                            try {
                            json = (JSONObject)JSONValue.parse(response.getResponseText());
                            ticket.setTicket(UnitConversion.hexStringToByteArray(json.get(JSON_TICKET).toString()));
                            ticket.setExpireTime((Long) json.get(JSON_TIME_TO_EXPIRE));
                            System.out.println("Ticket updated!! :) -> ["+json.get(JSON_TICKET).toString()+"] expire time "+UnitConversion.Timestamp2String(ticket.getExpireTime()));
                            } catch(Exception e) {
                                System.out.println("JSON Error "+e);
                            }
                        } else {
                            System.out.println("No Ticket received.");
                        }
                    } catch(Exception e) {
                        System.out.println("JSON Error "+e);
                    }
                    
                } else {
                    System.out.println("No Authentication received.");
                }
                
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MedusaAuthenticationThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
              
        }
        
        LOGGER.log(Level.WARNING, "Thread [{0}] dying", MedusaAuthenticationThread.class.getSimpleName()); 
    }
    
    public void ShutDown() {
        running = false;
    }
}