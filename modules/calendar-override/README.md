# calendar-override Module

This module contains a replacement class for com.liferay.calendar.notification.impl.EmailNotificationSender.

It doesn't do anything differently than the legacy class, but it does generate log messages when it is
instantiated and invoked, only to show that the override actually is being used by the original.

This module will not really be deployed, but it is a container project used to compile the classes that will
be overridden in the target fragment bundle.

