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
package com.punyal.medusaserver.californiumServer.utils;

import static com.punyal.medusaserver.californiumServer.core.MedusaConfiguration.CoAP_TICKET_OPTION;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

/**
 * Class to Convert between different Unit/Object types
 */
public class UnitConversion {
    // Prevent Initialization
    private UnitConversion() {}
    
    public static String ByteArray2Hex(byte[] bytes) {
        if(bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(byte b:bytes)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }
    
    public static String ByteArray2String(byte[] bytes) {
        String string;
        try {
            string = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.err.println("ByteArray2String UnsupportedEncodingException "+ ex);
            string = "";
        }
        return string;
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+=2)
            data[i/2] = (byte) ((Character.digit(s.charAt(i),16) << 4) +
                    Character.digit(s.charAt(i+1), 16));
        return data;
    }
    
    public static byte[] stringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        data = s.getBytes(Charset.forName("UTF-8"));
        return data;
    }
    
    public static String Timestamp2String(long timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S").format(timestamp);
    }
    
    public static String getCoapTicket(String coap) {
        if(coap.contains("Unknown ("+CoAP_TICKET_OPTION+")")){
            String temp = coap.substring(coap.indexOf("Unknown ("+CoAP_TICKET_OPTION+")"));
            temp = temp.substring(temp.indexOf("0x")+2);
            if (temp.length() < 3 )
                return null;
            temp = temp.split("}")[0];
            temp = temp.split(" ")[0];
            temp = temp.split(",")[0];
            if (temp.length() > 0 )
                return temp;
        }
        return null;
    }
}
