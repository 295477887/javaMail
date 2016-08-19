package com.chen.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * 该类发送邮件是鉴权用
 * 继承Authenticator
 * 
 * */
public class MyAuthenticator extends Authenticator{   
    String userName=null;   
    String password=null;   
        
    public MyAuthenticator(){   
    }   
    public MyAuthenticator(String username, String password) {    
        this.userName = username;    
        this.password = password;    
    }    
    protected PasswordAuthentication getPasswordAuthentication(){   
        return new PasswordAuthentication(userName, password);   
    }   
}   

