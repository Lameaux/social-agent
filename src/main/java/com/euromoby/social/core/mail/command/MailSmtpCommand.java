package com.euromoby.social.core.mail.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.euromoby.social.core.mail.MailSession;
import com.euromoby.social.core.mail.util.DSNStatus;
import com.euromoby.social.core.model.Tuple;
import com.euromoby.social.core.utils.StringUtils;

@Component
public class MailSmtpCommand extends SmtpCommandBase implements SmtpCommand {

	public static final String COMMAND_NAME = "MAIL";
	public static final Pattern FROM_SIZE = Pattern.compile("FROM:\\s?<([^>]+)>\\s?(SIZE=([0-9]+))?");

	public static final String RESPONSE_503_NEED_HELO = "503 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.DELIVERY_OTHER) + " Need HELO or EHLO before MAIL"; 
	public static final String RESPONSE_501_SYNTAX_ERROR_MAIL = "501 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.ADDRESS_SYNTAX_SENDER) + " Syntax error in MAIL command"; 
	public static final String RESPONSE_501_SYNTAX_ERROR_SENDER = "501 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.ADDRESS_SYNTAX_SENDER) + " Syntax error in sender address"; 
	public static final String RESPONSE_501_SENDER_NOT_ALLOWED = "501 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.ADDRESS_SYNTAX_SENDER) + " Sender is not allowed"; 
	public static final String RESPONSE_552_MESSAGE_SIZE = "552 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.SYSTEM_MSG_TOO_BIG) + " Message size exceeds fixed maximum message size"; 
	public static final String RESPONSE_250_SENDER_OK = "250 " + DSNStatus.getStatus(DSNStatus.SUCCESS, DSNStatus.ADDRESS_OTHER) + " Sender OK"; 
	
	@Override
	public String name() {
		return COMMAND_NAME;
	}

	@Override
	public String execute(MailSession mailSession, Tuple<String, String> request) {

		if (StringUtils.nullOrEmpty(mailSession.getDomain())) {
			return RESPONSE_503_NEED_HELO;
		}

		String fromSize = request.getSecond();
		if (StringUtils.nullOrEmpty(fromSize)) {
			return RESPONSE_501_SYNTAX_ERROR_MAIL;
		}
		fromSize = fromSize.trim();
		Matcher m = FROM_SIZE.matcher(fromSize);
		if (!m.matches()) {
			return RESPONSE_501_SYNTAX_ERROR_MAIL;
		}

		String senderEmail = m.group(1);
		int mailSize = 0;
		if (m.group(3) != null) {
			mailSize = Integer.parseInt(m.group(3));
		}

		Tuple<String, String> sender = Tuple.splitString(senderEmail, "@");
		if (StringUtils.nullOrEmpty(sender.getFirst()) || StringUtils.nullOrEmpty(sender.getSecond())) {
			return RESPONSE_501_SYNTAX_ERROR_SENDER;
		}

		if (!isAllowedSender(sender)) {
			return RESPONSE_501_SENDER_NOT_ALLOWED;
		}

		mailSession.setSender(sender);

		if (mailSize > 0 && mailSize > MailSession.MAX_MESSAGE_SIZE) {
			return RESPONSE_552_MESSAGE_SIZE;
		}
		mailSession.setDeclaredMailSize(mailSize);

		return RESPONSE_250_SENDER_OK;
	}

	protected boolean isAllowedSender(Tuple<String, String> sender) {
		return true;
	}

}
