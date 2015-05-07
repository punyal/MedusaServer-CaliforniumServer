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

import static com.punyal.medusaserver.californiumServer.core.MedusaConfiguration.CoAP_TICKET_OPTION;
import com.punyal.medusaserver.californiumServer.core.MedusaValidation;
import com.punyal.medusaserver.californiumServer.utils.UnitConversion;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class Medusa {
    private Medusa(){}
    
    public static byte[] getTicket (CoapExchange exchange) {
        OptionSet optList = exchange.getRequestOptions();
        if (optList.hasOption(CoAP_TICKET_OPTION)) {
            String ticket = UnitConversion.getCoapTicket(optList.toString());
            if (ticket != null)
                return UnitConversion.hexStringToByteArray(ticket);
        }
        return null;
    }
    
    public static Client checkClient (String medusaServerAddress, CoapExchange exchange, ClientsEngine clientsEngine, MyTicket myTicket) {
        byte[] ticket_b = getTicket(exchange);
        if(ticket_b != null) {
            String ticket = UnitConversion.ByteArray2Hex(ticket_b);
            
            Client client = clientsEngine.getClient(ticket);
            
            if(client != null) {
                // TODO: check user parameters
                //System.out.println("User already on the system");
                return client;
            }else{
                // Ask to AAA Server
                // TODO: improve it!!
                client = MedusaValidation.check(medusaServerAddress, UnitConversion.ByteArray2Hex(myTicket.getTicket()), ticket);
                if(client != null) {
                    //System.out.println("New client " + client.getUserName());
                    clientsEngine.addClient(client);
                    return client;
                }
                
            }
                
        } 
        return null;
    }
    
}
