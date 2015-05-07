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
import com.punyal.medusaserver.californiumServer.core.security.Client;
import com.punyal.medusaserver.californiumServer.utils.UnitConversion;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MedusaValidation{
    
    private MedusaValidation() {
    }
    
    public static Client check(String medusaServerAddress, String myTicket, String ticket) {
        CoapClient coapClient = new CoapClient();
        Logger.getLogger("org.eclipse.californium.core.network.CoAPEndpoint").setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.EndpointManager").setLevel(Level.OFF);
        Logger.getLogger("org.eclipse.californium.core.network.stack.ReliabilityLayer").setLevel(Level.OFF);
        
        CoapResponse response;
        
        coapClient.setURI(medusaServerAddress+"/"+MEDUSA_SERVER_VALIDATION_SERVICE_NAME);
        JSONObject json = new JSONObject();
        json.put(JSON_MY_TICKET, myTicket);
        json.put(JSON_TICKET, ticket);
        response = coapClient.put(json.toString(),0);
        
        if(response != null) {
            //System.out.println(response.getResponseText());
            
            try {
                json.clear();
                json = (JSONObject)JSONValue.parse(response.getResponseText());
                long expireTime = (Long)json.get(JSON_TIME_TO_EXPIRE) + (new Date()).getTime();
                String userName = json.get(JSON_USER_NAME).toString();
                String[] temp = json.get(JSON_ADDRESS).toString().split("/");
                String address;
                if (temp[1] != null)
                    address = temp[1];
                else
                    address = "0.0.0.0";
                Client client = new Client(InetAddress.getByName(address), userName, null, UnitConversion.hexStringToByteArray(ticket), expireTime);
                return client;
            } catch(Exception e) {
            }
            
        } else {
            // TODO: take 
        }
        return null;
    }
}