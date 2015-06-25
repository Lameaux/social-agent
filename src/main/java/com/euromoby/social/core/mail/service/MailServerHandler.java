package com.euromoby.social.core.mail.service;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.euromoby.social.core.Config;
import com.euromoby.social.core.mail.MailManager;
import com.euromoby.social.core.mail.MailSession;
import com.euromoby.social.core.mail.MailSizeException;
import com.euromoby.social.core.mail.SmtpCommandProcessor;
import com.euromoby.social.core.mail.command.QuitSmtpCommand;
import com.euromoby.social.core.mail.command.SmtpCommand;
import com.euromoby.social.core.mail.util.DSNStatus;
import com.euromoby.social.core.model.Tuple;
import com.euromoby.social.core.utils.StringUtils;


public class MailServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger LOG = LoggerFactory.getLogger(MailServerHandler.class);

	private static final String WELCOME_TITLE = "EUROMOBY v1.0";
	
	private Config config;
	private SmtpCommandProcessor mailCommandProcessor;
	private MailManager mailManager;

	private MailSession mailSession;

	public MailServerHandler(Config config, SmtpCommandProcessor mailCommandProcessor, MailManager mailManager) {
		this.config = config;
		this.mailCommandProcessor = mailCommandProcessor;
		this.mailManager = mailManager;
	}

	protected MailSession getMailSession() {
		return mailSession;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		mailSession = new MailSession();
		String greeting = "220 " + config.getMailHost() + " ESMTP " + WELCOME_TITLE;
		ctx.writeAndFlush(greeting + StringUtils.CRLF);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (mailSession != null) {
			mailSession.reset();
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String line) throws Exception {

		if (!mailSession.isCommandMode()) {
			try {
				boolean endOfTransfer = mailSession.processDataLine(line);
				if (endOfTransfer) {
					mailManager.saveMessage(mailSession);
					mailSession.reset();
					String response = "250 " + DSNStatus.getStatus(DSNStatus.SUCCESS, DSNStatus.CONTENT_OTHER) + " Message received";
					ctx.write(response + StringUtils.CRLF);
				}
			} catch (MailSizeException e) {
				mailSession.reset();
				String response = "552 " + DSNStatus.getStatus(DSNStatus.PERMANENT, DSNStatus.SYSTEM_MSG_TOO_BIG) + " Message is too big";
				ctx.write(response + StringUtils.CRLF);
			}
			return;
		}

		Tuple<String, String> request = Tuple.splitString(line, SmtpCommand.SEPARATOR);
		String command = request.getFirst().toUpperCase();

		String response = mailCommandProcessor.process(mailSession, request);
		ChannelFuture future = ctx.write(response + StringUtils.CRLF);

		if (QuitSmtpCommand.COMMAND_NAME.equals(command)) {
			future.addListener(ChannelFutureListener.CLOSE);
			mailSession = null;
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOG.debug("Exception", cause);
		ctx.close();
	}
}
