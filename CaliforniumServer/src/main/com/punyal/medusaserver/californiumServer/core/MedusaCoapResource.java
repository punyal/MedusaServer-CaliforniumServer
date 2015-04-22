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

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MedusaCoapResource extends CoapResource{
    
    public MedusaCoapResource(String name) {
        super(name);
    }
    
    public MedusaCoapResource(String name, boolean visible) {
        super(name, visible);
    }
    
    @Override
    public void handleGET(CoapExchange exchange) {
        // Check the security
        OptionSet optList = exchange.getRequestOptions();
        System.out.println(optList);
        if(optList.hasOption(100)){
            System.out.println("Option 100 here");
            JSONObject json = (JSONObject)JSONValue.parse(optList.toString());
            
            System.out.println(json.get("Unknown (100)").toString());
        }
        else System.out.println("No option 100 here");
        medusaHandleGET(exchange);
    }
    
    public void medusaHandleGET(CoapExchange exchange) {
        exchange.respond("NOT medusaHandleGET defined");
    }
    
}