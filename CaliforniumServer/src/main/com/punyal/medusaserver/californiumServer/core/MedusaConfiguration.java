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
    public static final String SERVER_AUTHENTICATION_SERVICE_NAME = "Authentication";
    
    // Performance Configuration
    public static final int MAX_NO_AUTHENTICATION_RESPONSES = 5;
    public static final long NO_AUTHENTICATION_RESPONSES_DELAY = 240000; // 1000*60*4 = 240000 (4min)
    public static final int MAX_NO_TICKET_RESPONSES = 5;
    public static final long NO_TICKET_RESPONSES_DELAY = 240000; // 1000*60*4 = 240000 (4min)
    
    // Message Format
    public static final String JSON_USER_NAME = "userName";
    public static final String JSON_USER_PASSWORD = "userPass";
    public static final String JSON_TIME_TO_EXPIRE = "ExpireTime";
    public static final String JSON_AUTHENTICATOR = "Authenticator";
    public static final String JSON_TICKET = "Ticket";
    
    // UI Messages
    public static final String SMS_NO_AUTHENTICATION_RESPONSE = "No Authenticator received. Trying again in " + NO_AUTHENTICATION_RESPONSES_DELAY/60000 + " minutes";
    public static final String SMS_NO_TICKET_RESPONSE = "No Ticket received. Trying again in " + NO_TICKET_RESPONSES_DELAY/60000 + " minutes";
    public static final String SMS_AUTHENTICATED = "System is now Authenticated.";
    public static final String SMS_NO_AUTHENTICATED = "System is now NOT Authenticated.";
    
}