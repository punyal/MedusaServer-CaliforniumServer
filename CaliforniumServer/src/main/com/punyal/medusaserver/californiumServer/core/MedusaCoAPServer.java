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

import org.eclipse.californium.core.CoapServer;

public class MedusaCoAPServer extends CoapServer {
    MedusaAuthenticationThread mat;
    
    
    public MedusaCoAPServer() {
        mat = new MedusaAuthenticationThread();
    }
    
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
} 