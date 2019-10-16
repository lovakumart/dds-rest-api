package com.org.walmart.dds.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.org.walmart.dds.config.EmailUtilConfig;
import com.org.walmart.dds.model.OrderDetail;

public class DdsEmailUtil {

	static final Logger _logger = LoggerFactory.getLogger(DdsEmailUtil.class);
	
	public static boolean sendEmail(String customerEmail, OrderDetail order) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(EmailUtilConfig.GMAIL_HOST);
			email.setSmtpPort(EmailUtilConfig.SMTP_PORT);
			email.setAuthenticator(
					new DefaultAuthenticator(EmailUtilConfig.DEFAULT_USERNAME, EmailUtilConfig.DEFAULT_PASSWORD));
			email.setSSL(EmailUtilConfig.SSL_FLAG);
			email.setFrom(EmailUtilConfig.WM_CUST_SUPP_EMAIL_ID);
			email.setSubject("Thank you for choosing Walmart. Status of your Order #" + order.getOrderId());
			email.setMsg(order.getComments());
			email.addTo(EmailUtilConfig.CUST_EMAIL_ID);
			email.send();
			
			return true;
		} catch (Exception ex) {
			_logger.info(
					"Failed to send email to " + EmailUtilConfig.CUST_EMAIL_ID + " for order " + order.getOrderId());
		} 
		
		return false;
	}
}
