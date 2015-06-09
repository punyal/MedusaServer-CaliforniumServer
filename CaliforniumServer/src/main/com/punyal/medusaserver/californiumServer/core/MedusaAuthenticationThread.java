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
import com.punyal.medusaserver.californiumServer.core.security.MyTicket;
import com.punyal.medusaserver.californiumServer.utils.UnitConversion;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MedusaAuthenticationThread extends Thread{
    private static final Logger LOGGER = Logger.getLogger(MedusaAuthenticationThread.class.getCanonicalName());
    private boolean running;
    private boolean authenticated;
    private final MyTicket ticket;
    private final CoapClient coapClient;
    private String medusaServerAddress;
    private String medusaSecretKey;
    private String medusaUserName;
    private String medusaUserPass;
    private String medusaUserInfo;
    private int noAuthenticationResponseCounter;
    private int noTicketResponseCounter;
    
    public MedusaAuthenticationThread(String serverAddress, String secretKey, String userName, String userPass, String userInfo) {
        running = false;
        authenticated = false;
        medusaServerAddress = serverAddress;
        medusaSecretKey = secretKey;
        medusaUserName = userName;
        medusaUserPass = userPass;
        medusaUserInfo = userInfo;
        ticket = new MyTicket();
        coapClient = new CoapClient();
        noAuthenticationResponseCounter = 0;
        noTicketResponseCounter = 0;
    }
    
    public MedusaAuthenticationThread() {
        running = false;
        authenticated = false;
        ticket = new MyTicket();
        coapClient = new CoapClient();
        noAuthenticationResponseCounter = 0;
        noTicketResponseCounter = 0;
    }
    
    public void setConfiguration(String serverAddress, String secretKey, String userName, String userPass, String userInfo) {
        medusaServerAddress = serverAddress;
        medusaSecretKey = secretKey;
        medusaUserName = userName;
        medusaUserPass = userPass;
        medusaUserInfo = userInfo;
    }
    
    
    @Override
    public void run() {
        running = true;
        Logger.getLogger("org.eclipse.californium.core.network.CoAPEndpoint").setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.EndpointManager").setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.stack.ReliabilityLayer").setLevel(Level.OFF);
        
        CoapResponse response;
                
        while(running) {
            if(ticket.isValid()) {
                //System.out.println("Valid Ticket");
            } else {
                //System.out.println("Not Valid Ticket");
                
                try {
                    coapClient.setURI(medusaServerAddress+"/"+MEDUSA_SERVER_AUTHENTICATION_SERVICE_NAME);
                    response = coapClient.get();

                    if (response!=null) {
                        try {
                            JSONObject json = (JSONObject)JSONValue.parse(response.getResponseText());
                            ticket.setAuthenticator(json.get(JSON_AUTHENTICATOR).toString());
                            //System.out.println(ticket.getAuthenticator());


                            json.clear();
                            json.put(JSON_USER_NAME, medusaUserName);
                            json.put(JSON_USER_PASSWORD, Cryptonizer.encryptCoAP(medusaSecretKey, ticket.getAuthenticator() , medusaUserPass));
                            json.put(JSON_INFO, medusaUserInfo);
                            //System.out.println(json.toString());

                            response = coapClient.put(json.toString(), 0);

                            if(response!=null) {
                                json.clear();
                                try {
                                json = (JSONObject)JSONValue.parse(response.getResponseText());
                                ticket.setTicket(UnitConversion.hexStringToByteArray(json.get(JSON_TICKET).toString()));
                                ticket.setExpireTime((Long)json.get(JSON_TIME_TO_EXPIRE) + (new Date()).getTime());
                                //System.out.println((Long)json.get(JSON_TIME_TO_EXPIRE));
                                noAuthenticationResponseCounter = 0;
                                noTicketResponseCounter = 0;
                                if(authenticated == false) {
                                    //LOGGER.log(Level.INFO, SMS_AUTHENTICATED);
                                    System.err.println(SMS_AUTHENTICATED);
                                    authenticated = true;
                                }
                                } catch(Exception e) {
                                    noTicketResponseCounter++;
                                    //System.out.println("JSON Error "+e);
                                }
                            } else {
                                //System.out.println("No Ticket received.");
                                noTicketResponseCounter++;
                            }
                        } catch(Exception e) {
                            noAuthenticationResponseCounter++;
                            //System.out.println("JSON Error "+e);
                        }

                    } else {
                        //System.out.println("No Authentication received.");
                        noAuthenticationResponseCounter++;

                    }
                } catch (IllegalArgumentException ex) {
                    noAuthenticationResponseCounter++;
                }
                
                
                
                
                if( (authenticated == true) && ((noAuthenticationResponseCounter != 0)||(noTicketResponseCounter != 0)) ) {
                    //LOGGER.log(Level.INFO, SMS_NO_AUTHENTICATED);
                    System.err.println(SMS_NO_AUTHENTICATED);
                    authenticated = false;
                }
                
                if(noAuthenticationResponseCounter > MAX_NO_AUTHENTICATION_RESPONSES) {
                    try {
                        //LOGGER.log(Level.WARNING, SMS_NO_AUTHENTICATION_RESPONSE);
                        System.err.println(SMS_NO_AUTHENTICATION_RESPONSE);
                        sleep(NO_AUTHENTICATION_RESPONSES_DELAY);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MedusaAuthenticationThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    noAuthenticationResponseCounter = 0;
                    noTicketResponseCounter = 0;
                }
                if(noTicketResponseCounter > MAX_NO_TICKET_RESPONSES) {
                    try {
                        //LOGGER.log(Level.WARNING, SMS_NO_TICKET_RESPONSE);
                        System.err.println(SMS_NO_TICKET_RESPONSE);
                        sleep(NO_TICKET_RESPONSES_DELAY);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MedusaAuthenticationThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    noAuthenticationResponseCounter = 0;
                    noTicketResponseCounter = 0;
                }
            }
            
              
        }
        
        LOGGER.log(Level.WARNING, "Thread [{0}] dying", MedusaAuthenticationThread.class.getSimpleName()); 
    }
    
    public void ShutDown() {
        running = false;
    }
    
    public synchronized MyTicket getTicket() {
        return ticket;
    }
}