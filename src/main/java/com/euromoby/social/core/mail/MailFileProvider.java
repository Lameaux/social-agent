package com.euromoby.social.core.mail;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.euromoby.social.core.Config;
import com.euromoby.social.core.model.Tuple;

@Component
public class MailFileProvider {

	private Config config;

	@Autowired
	public MailFileProvider(Config config) {
		this.config = config;
	}

	public File getInboxDirectory(Tuple<String, String> recipient) throws Exception {
		String location = recipient.getSecond() + File.separator + recipient.getFirst();
		File targetDir = new File(new File(config.getMailStoragePath()), location);
		if (!targetDir.exists() && !targetDir.mkdirs() && !targetDir.mkdir()) {
			throw new Exception("Error saving file to " + targetDir.getAbsolutePath());
		}
		return targetDir;
	}
	
	public File getMessageFile(Tuple<String, String> loginDomain, int messageId) throws Exception {
		File inboxDirectory = getInboxDirectory(loginDomain);
		return new File(inboxDirectory, messageId + ".msg");
	}
	
}
