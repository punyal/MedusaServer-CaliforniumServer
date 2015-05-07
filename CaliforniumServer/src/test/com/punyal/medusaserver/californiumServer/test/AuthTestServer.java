/*******************************************************************************
 * Copyright (c) 2014 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 ******************************************************************************/
package com.punyal.medusaserver.californiumServer.test;

import com.punyal.medusaserver.californiumServer.core.MedusaAuthenticationThread;
import com.punyal.medusaserver.californiumServer.core.MedusaCoapServer;
import com.punyal.medusaserver.californiumServer.core.MedusaCoapResource;
import com.punyal.medusaserver.californiumServer.core.security.Client;
import com.punyal.medusaserver.californiumServer.utils.UnitConversion;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.californium.core.server.resources.CoapExchange;


public class AuthTestServer extends MedusaCoapServer {
    
    /*
     * Application entry point.
     */
    public static void main(String[] args) {
        
        try {
            
            // create server
            AuthTestServer server = new AuthTestServer(new MedusaAuthenticationThread());
            server.start();
            
        } catch (SocketException e) {
            
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }
    
    /*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public AuthTestServer(MedusaAuthenticationThread mat) throws SocketException {
        super(mat, "130.240.235.18", "Arrowhead", "Coap_testServer", "test");
        
        // provide an instance of a Hello-World resource
        add(new InfoResource(mat));
    }
    
    /*
     * Definition of the Hello-World Resource
     */
    class InfoResource extends MedusaCoapResource {
        
        public InfoResource(MedusaAuthenticationThread mat) {
            
            // set resource identifier
            super("Info", false, getClientsEngine(), getMedusaServerAddress(), mat.getTicket());
            
            // set display name
            getAttributes().setTitle("All Info about you");
        }
        
        @Override
        public void medusaHandleGET(CoapExchange exchange, Client client) {            
            String response = String.format("Info about you: \n\t - User Name: %s\n\t - Address: %s\n\t - Ticket: %s\n\t - ExpireTime: %s",
                    client.getUserName(),
                    //(( client.getAddress().toString().split("/")[1].equals(exchange.getSourceAddress().toString().split("/")[1])) )? client.getAddress().toString().split("/")[1]: client.getAddress().toString().split("/")[1] +" and "+ exchange.getSourceAddress().toString().split("/")[1]),
                    client.getAddress().toString().split("/")[1],
                    UnitConversion.ByteArray2Hex(client.getTicket()).toUpperCase(),
                    new SimpleDateFormat("yyyy-MM-dd H:m:s").format(new Date(client.getExpireTime())));
            
            respond(exchange,response);
        }
    }
    
    
}
