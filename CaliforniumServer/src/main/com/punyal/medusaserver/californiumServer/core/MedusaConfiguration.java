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

public class MedusaConfiguration{
    private void MedusaConfiguration () {}
    
    // Medusa Server Configuration
    public static String SERVER_AUTHENTICATION_SERVICE_NAME = "Authentication";
    
    // Message Format
    public static String JSON_USER_NAME = "userName";
    public static String JSON_USER_PASSWORD = "userPass";
    public static String JSON_TIME_TO_EXPIRE = "ExpireTime";
    public static String JSON_AUTHENTICATOR = "Authenticator";
    public static String JSON_TICKET = "Ticket";
    
}