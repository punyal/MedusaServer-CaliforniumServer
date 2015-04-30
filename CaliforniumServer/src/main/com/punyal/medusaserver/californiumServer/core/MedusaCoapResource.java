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

import com.punyal.medusaserver.californiumServer.core.security.Client;
import com.punyal.medusaserver.californiumServer.core.security.ClientsEngine;
import com.punyal.medusaserver.californiumServer.core.security.Medusa;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class MedusaCoapResource extends CoapResource{
    private final boolean publicResource;
    private final ClientsEngine clientsEngine;
    private final String medusaServerAddress;
    
    public MedusaCoapResource(String name, boolean publicResource, ClientsEngine clientsEngine, String medusaServerAddress) {
        super(name);
        this.publicResource = publicResource;
        this.clientsEngine = clientsEngine;
        this.medusaServerAddress = medusaServerAddress;
    }
    
    public MedusaCoapResource(String name, boolean visible, boolean publicResource,  ClientsEngine clientsEngine, String medusaServerAddress) {
        super(name, visible);
        this.publicResource = publicResource;
        this.clientsEngine = clientsEngine;
        this.medusaServerAddress = medusaServerAddress;
    }
    
    @Override
    public void handleGET(CoapExchange exchange) {
        // PUBLIC RESOURCE
        if(publicResource) {
            medusaHandleGET(exchange, null);
            return;
        }
        // NOT PUBLIC RESOURCE
        Client client = Medusa.checkClient(medusaServerAddress, exchange, clientsEngine);
        if(client != null) {
            medusaHandleGET(exchange, client);
            return;
        }
        // Empty response to prevent retransmissions and saturation
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    
    @Override
    public void handlePOST(CoapExchange exchange) {
        // PUBLIC RESOURCE
        if(publicResource) {
            medusaHandlePOST(exchange, null);
            return;
        }
        // NOT PUBLIC RESOURCE
        Client client = Medusa.checkClient(medusaServerAddress, exchange, clientsEngine);
        if(client != null) {
            medusaHandlePOST(exchange, client);
            return;
        }
        // Empty response to prevent retransmissions and saturation
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    
    @Override
    public void handlePUT(CoapExchange exchange) {
        // PUBLIC RESOURCE
        if(publicResource) {
            medusaHandlePUT(exchange, null);
            return;
        }
        // NOT PUBLIC RESOURCE
        Client client = Medusa.checkClient(medusaServerAddress, exchange, clientsEngine);
        if(client != null) {
            medusaHandlePUT(exchange, client);
            return;
        }
        // Empty response to prevent retransmissions and saturation
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    
    @Override
    public void handleDELETE(CoapExchange exchange) {
        // PUBLIC RESOURCE
        if(publicResource) {
            medusaHandleDELETE(exchange, null);
            return;
        }
        // NOT PUBLIC RESOURCE
        Client client = Medusa.checkClient(medusaServerAddress, exchange, clientsEngine);
        if(client != null) {
            medusaHandleDELETE(exchange, client);
            return;
        }
        // Empty response to prevent retransmissions and saturation
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    
    public void medusaHandleGET(CoapExchange exchange, Client client) {
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    public void medusaHandlePOST(CoapExchange exchange, Client client) {
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    public void medusaHandlePUT(CoapExchange exchange, Client client) {
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    public void medusaHandleDELETE(CoapExchange exchange, Client client) {
        exchange.respond(CoAP.ResponseCode.METHOD_NOT_ALLOWED);
    }
    
}