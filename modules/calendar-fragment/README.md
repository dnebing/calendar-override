# calendar-fragment Module

This is a fragment bundle to override the spring context bean defined in the calendar-service module.

This works only because the given class name is unique, it is not trying to replace an existing class from the
host module (since it always comes first in classpath evaluation).

Additionally there is a custom spring xml file included in META-INF/spring which will be processed when the
spring context is being initialized.  Note that this, too, must be a unique filename compared to the host bundle
otherwise this file would not get pulled in.

