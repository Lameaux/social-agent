package com.euromoby.social.core.mail.command;

import org.springframework.stereotype.Component;

import com.euromoby.social.core.mail.MailSession;
import com.euromoby.social.core.mail.util.DSNStatus;
import com.euromoby.social.core.model.Tuple;

@Component
public class NoopSmtpCommand extends SmtpCommandBase implements SmtpCommand {

	public static final String COMMAND_NAME = "NOOP";
	public static final String RESPONSE_250_OK = "250 " + DSNStatus.getStatus(DSNStatus.SUCCESS, DSNStatus.UNDEFINED_STATUS) + " OK";

	@Override
	public String name() {
		return COMMAND_NAME;
	}

	@Override
	public String execute(MailSession mailSession, Tuple<String, String> request) {
		return RESPONSE_250_OK;
	}

}
