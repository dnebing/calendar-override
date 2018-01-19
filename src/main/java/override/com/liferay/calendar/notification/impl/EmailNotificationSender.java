/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package override.com.liferay.calendar.notification.impl;

import com.liferay.calendar.model.CalendarNotificationTemplate;
import com.liferay.calendar.model.CalendarNotificationTemplateConstants;
import com.liferay.calendar.notification.NotificationField;
import com.liferay.calendar.notification.impl.NotificationRecipient;
import com.liferay.calendar.notification.impl.NotificationSender;
import com.liferay.calendar.notification.impl.NotificationSenderException;
import com.liferay.calendar.notification.impl.NotificationTemplateContext;
import com.liferay.calendar.notification.impl.NotificationTemplateRenderer;
import com.liferay.calendar.notification.impl.NotificationUtil;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.mail.internet.InternetAddress;

/**
 * @author Eduardo Lundgren
 */
public class EmailNotificationSender implements NotificationSender {

	public EmailNotificationSender() {
		super();

		_log.warn("******************************************************");
		_log.warn("** Created override email notification sender flag. **");
		_log.warn("******************************************************");
	}

	@Override
	public void sendNotification(
			String fromAddress, String fromName,
			NotificationRecipient notificationRecipient,
			NotificationTemplateContext notificationTemplateContext)
		throws NotificationSenderException {

		_log.warn("************************************************");
		_log.warn("** Email Notification Sender Override Invoked **");
		_log.warn("************************************************");

		try {
			CalendarNotificationTemplate calendarNotificationTemplate =
				notificationTemplateContext.getCalendarNotificationTemplate();

			String fromAddressValue = NotificationUtil.getTemplatePropertyValue(
				calendarNotificationTemplate,
				CalendarNotificationTemplateConstants.PROPERTY_FROM_ADDRESS,
				fromAddress);
			String fromNameValue = NotificationUtil.getTemplatePropertyValue(
				calendarNotificationTemplate,
				CalendarNotificationTemplateConstants.PROPERTY_FROM_NAME,
				fromName);

			notificationTemplateContext.setFromAddress(fromAddressValue);
			notificationTemplateContext.setFromName(fromNameValue);
			notificationTemplateContext.setToAddress(
				notificationRecipient.getEmailAddress());
			notificationTemplateContext.setToName(
				notificationRecipient.getName());

			String subject = NotificationTemplateRenderer.render(
				notificationTemplateContext, NotificationField.SUBJECT);
			String body = NotificationTemplateRenderer.render(
				notificationTemplateContext, NotificationField.BODY);

			sendNotification(
				notificationTemplateContext.getFromAddress(),
				notificationTemplateContext.getFromName(),
				notificationRecipient, subject, body);
		}
		catch (Exception e) {
			throw new NotificationSenderException(e);
		}
	}

	@Override
	public void sendNotification(
			String fromAddress, String fromName,
			NotificationRecipient notificationRecipient, String subject,
			String notificationMessage)
		throws NotificationSenderException {

		_log.warn("************************************************");
		_log.warn("** Email Notification Sender Override Invoked **");
		_log.warn("************************************************");

		try {
			InternetAddress fromInternetAddress = new InternetAddress(
				fromAddress, fromName);

			MailMessage mailMessage = new MailMessage(
				fromInternetAddress, subject, notificationMessage, true);

			mailMessage.setHTMLFormat(notificationRecipient.isHTMLFormat());

			InternetAddress toInternetAddress = new InternetAddress(
				notificationRecipient.getEmailAddress());

			mailMessage.setTo(toInternetAddress);

			MailServiceUtil.sendEmail(mailMessage);
		}
		catch (Exception e) {
			throw new NotificationSenderException(
				"Unable to send mail message", e);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(EmailNotificationSender.class);
}