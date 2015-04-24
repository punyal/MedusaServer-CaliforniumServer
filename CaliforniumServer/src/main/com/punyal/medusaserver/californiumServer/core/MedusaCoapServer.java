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

import static com.punyal.medusaserver.californiumServer.core.MedusaConfiguration.CoAP_TICKET_OPTION;
import com.punyal.medusaserver.californiumServer.core.security.ClientsEngine;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class MedusaCoapServer extends CoapServer {
    private MedusaAuthenticationThread mat;
    private String medusaServerAddress = null;
    private String medusaSecretKey = null;
    private String medusaUserName = null;
    private String medusaUserPass = null;
    private ClientsEngine clientsEngine = new ClientsEngine();
    
    
    public MedusaCoapServer(String serverAddress, String secretKey, String userName, String userPass) {
        medusaServerAddress = serverAddress;
        medusaSecretKey = secretKey;
        medusaUserName = userName;
        medusaUserPass = userPass;
        mat = new MedusaAuthenticationThread(medusaServerAddress, medusaSecretKey, medusaUserName, medusaUserPass);
        clientsEngine.start();
    }
    /*
    public void setMedusaServerAddress(String serverAddress) {
        medusaServerAddress = serverAddress;
    }
    
    public void setMedusaSecretKey(String secretKey) {
        medusaSecretKey = secretKey;
    }
    
    public void setMedusaUserName(String userName) {
        medusaUserName = userName;
    }
    
    public void setMedusaUserPass(String userPass) {
        medusaUserPass = userPass;
    }
    */
    
    @Override
    public void start() {
        super.start();
        mat.start();
    }
    
    @Override
    public void stop() {
        super.stop();
        mat.ShutDown();
    }
    
    public ClientsEngine getClientsEngine() {
        return clientsEngine;
    }
    
    public String getMedusaServerAddress() {
        return medusaServerAddress;
    }
    
    
    public void respond(CoapExchange exchange, CoAP.ResponseCode code) {
        Response response = new Response(code);
        // Add Ticket Option
        response.getOptions().addOption(new Option(CoAP_TICKET_OPTION, mat.getTicket().getTicket()));
        exchange.respond(response);
    }
    
    public void respond(CoapExchange exchange, String payload) {
        respond(exchange, CoAP.ResponseCode.CONTENT, payload);
    }
    
    public void respond(CoapExchange exchange, CoAP.ResponseCode code, String payload) {
        Response response = new Response(code);
        response.setPayload(payload);
        response.getOptions().setContentFormat(MediaTypeRegistry.TEXT_PLAIN);
        // Add Ticket Option
        response.getOptions().addOption(new Option(CoAP_TICKET_OPTION, mat.getTicket().getTicket()));
        exchange.respond(response);
    }
    
    public void respond(CoapExchange exchange, CoAP.ResponseCode code, byte[] payload) {
        Response response = new Response(code);
        response.setPayload(payload);
        // Add Ticket Option
        response.getOptions().addOption(new Option(CoAP_TICKET_OPTION, mat.getTicket().getTicket()));
        exchange.respond(response);
    }
    
    public void respond(CoapExchange exchange, CoAP.ResponseCode code, byte[] payload, int contentFormat) {
        Response response = new Response(code);
        response.setPayload(payload);
        response.getOptions().setContentFormat(contentFormat);
        // Add Ticket Option
        response.getOptions().addOption(new Option(CoAP_TICKET_OPTION, mat.getTicket().getTicket()));
        exchange.respond(response);
    }
    
    public void respond(CoapExchange exchange, CoAP.ResponseCode code, String payload, int contentFormat) {
        Response response = new Response(code);
        response.setPayload(payload);
        response.getOptions().setContentFormat(contentFormat);
        // Add Ticket Option
        response.getOptions().addOption(new Option(CoAP_TICKET_OPTION, mat.getTicket().getTicket()));
        exchange.respond(response);
    }
    
} 