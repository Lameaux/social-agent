package com.euromoby.social.core.mail.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.euromoby.social.core.Config;
import com.euromoby.social.core.mail.MailSession;
import com.euromoby.social.core.mail.util.DSNStatus;
import com.euromoby.social.core.model.Tuple;
import com.euromoby.social.core.utils.StringUtils;

@Component
public class EhloSmtpCommand extends SmtpCommandBase implements SmtpCommand {

	public static final String COMMAND_NAME = "EHLO";
	public static final String RESPONSE_501_INVALID_DOMAIN = "501 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.DELIVERY_INVALID_ARG) + " Invalid domain name";
	public static final String RESPONSE_250_DASH = "250-";
	private Config config;
	
	@Autowired
	public EhloSmtpCommand(Config config) {
		super();
		this.config = config;
	}

	@Override
	public String name() {
		return COMMAND_NAME;
	}

	@Override
	public String execute(MailSession mailSession, Tuple<String, String> request) {

		String domain = request.getSecond();
		if (StringUtils.nullOrEmpty(domain)) {
			return RESPONSE_501_INVALID_DOMAIN;
		}
		mailSession.setDomain(domain);

		StringBuffer sb = new StringBuffer();
		sb.append(RESPONSE_250_DASH).append(config.getMailHost()).append(StringUtils.CRLF);
		sb.append("250-SIZE " + MailSession.MAX_MESSAGE_SIZE).append(StringUtils.CRLF);
		sb.append("250 ENHANCEDSTATUSCODES");
		return sb.toString();
	}

}
