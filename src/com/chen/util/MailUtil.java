package com.chen.util;

import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailUtil 
{
	//protected static final String serverHost=PropertiesUtil.getPropertiesByKey("prop.properties", "ServerHost");
	protected static final String serverHost="smtp.qq.com";
	protected static final String serverPort = "25";
	protected static final String userName = "******@qq.com";
	protected static final String password = "********";
	
	/**
	 * 发送邮件给多个抄送人
	 * */
	public static boolean sendMailtoMultiCC(MailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        if (mailInfo.isValidate()) 
        {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        Session sendMailSession = Session.getInstance(mailInfo.getProperties(), authenticator);
        //sendMailSession.setDebug(true);
        try {
        	//创建邮件
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 获取抄送者信息
            String[] ccs = mailInfo.getCcs();
            if (ccs != null){
                // 为每个邮件接收者创建一个地址
                Address[] ccAdresses = new InternetAddress[ccs.length];
                for (int i=0; i<ccs.length; i++){
                    ccAdresses[i] = new InternetAddress(ccs[i]);
                }
                // 将抄送者信息设置到邮件信息中，注意类型为Message.RecipientType.CC
                mailMessage.setRecipients(Message.RecipientType.CC, ccAdresses);
            } 
            
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            // 设置邮件内容
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(mailInfo.getContent(), "text/html; charset=UTF-8");
            //添加附件
            String[] fileName = mailInfo.getAttachFileNames();
            if(fileName != null)
            {
            	for(int i=0;i<fileName.length;i++)
            	{
                    BodyPart bodyPart = new MimeBodyPart();
            		DataHandler dh = new DataHandler(new FileDataSource(fileName[i]));
            		bodyPart.setDataHandler(dh);
            		//防止附件名中文乱码
            		bodyPart.setFileName(MimeUtility.encodeText(dh.getName()));
                    mainPart.addBodyPart(bodyPart);
            	}
            }
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
	
	//发送简单邮件
	public static void main(String [] args) throws MessagingException
	{
		
		 MailSenderInfo mailInfo = new MailSenderInfo();
	        mailInfo.setMailServerHost(serverHost);
	        mailInfo.setMailServerPort(serverPort);
	        mailInfo.setValidate(true);
	        mailInfo.setUserName(userName);
	        mailInfo.setPassword(password);
	        mailInfo.setFromAddress(userName);
	        mailInfo.setToAddress("**************@qq.com");
	        mailInfo.setSubject("我是主题");
	        mailInfo.setContent("我是内容");
	        String tos = "*******,*********";
	        if(!tos.equals("")){
	        	String[] receivers = tos.split(",");
	            String[] ccs = receivers;
	            mailInfo.setReceivers(receivers);//多个接收人
	            mailInfo.setCcs(ccs);//多个抄送人
	        }
	        String fileName = "C:\\Users\\Administrator\\Desktop\\******.png,C:\\Users\\Administrator\\Desktop\\*********.sql";
	        if(!fileName.equals(""))
	        {
	        	String [] fileNames = fileName.split(",");
	        	mailInfo.setAttachFileNames(fileNames);
	        }
	        
	       sendMailtoMultiCC(mailInfo);//发送多个抄送人
		
	}
	
}
