package com.euromoby.social.core.mail.command;

import com.euromoby.social.core.mail.MailSession;
import com.euromoby.social.core.model.Tuple;

public interface SmtpCommand {
	
	public static final String SEPARATOR = " ";

	String name();
	
	String execute(MailSession mailSession, Tuple<String, String> request);

	boolean match(Tuple<String, String> request);


}
