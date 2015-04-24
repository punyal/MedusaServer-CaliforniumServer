
import static com.punyal.medusaserver.californiumServer.core.MedusaConfiguration.CoAP_TICKET_OPTION;

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


public class testOptions {
    public static void main(String[] args) {
        String real = "{\"Uri-Path\":\"helloWorld\", \"Unknown (100)\":0xc5f379b7b0a037a8}";
        System.out.println(real);
        System.out.println(getCoapTicket(real));
        
        
    }
    public static String getCoapTicket(String coap) {
        if(coap.contains("Unknown ("+CoAP_TICKET_OPTION+")")){
            String temp = coap.substring(coap.indexOf("Unknown ("+CoAP_TICKET_OPTION+")"));
            temp = temp.substring(temp.indexOf("0x")+2);
            temp = temp.split("}")[0];
            temp = temp.split(" ")[0];
            temp = temp.split(",")[0];
            if (temp.length() > 0 )
                return temp;
        }
        return null;
    }

}
    