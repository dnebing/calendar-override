# calendar-fragment Module

This is the actual fragment module that will override classes in the original Host bundle, in this case
the EmailNotificationSender class from the original calendar-service module.

This module doesn't contain any code or classes, instead it relies on BND magic to get the work done.

First, in the build.gradle file we have a module-level dependency on the project that has the class we want to pull in:

```
dependencies {
    compileOnly project(":modules:calendar-override")
}
```

Next, all of the heavy lifting is in the bnd.bnd file:

```
Bundle-Name: calendar-fragment
Bundle-SymbolicName: com.liferay.calendar.service.fragment
Bundle-Version: 1.0.0

# We're updating the calendar service module, accepting of range
Fragment-Host: com.liferay.calendar.service;bundle-version="[2.3.0,3.0.0)"

# Make sure the override dir is first in the classpath.
# The original bundle does not have an entry, so append .
Bundle-ClassPath: /override, .

# We want to pull in the class from the dependency jar, but we want it to be
# in the /override subdirectory so it conforms to the classpath override above.
# The @com.liferay.calendar.service.override-1.0.0.jar portion is the name of the
# built jar file from the dependent project.  When you do a build in that project
# dir, check the build/libs subdirectory to see the actual name of the file that
# you want to include.
-includeresource: /override/com/liferay/calendar/notification/impl/=@com.liferay.calendar.service.override-1.0.0.jar!/com/liferay/calendar/notification/impl/EmailNotificationSender.class

# Ignore the warning about bundle only containing resources, because that is all this fragment bundle has
-resourceonly: true

# BND will fail to build the module jar because it sees a class file in
# /override/com/liferay/calendar/notification/impl,
# but the class itself does not include override in the compiled class package info.
# This instruction basically forces BND to ignore these messages so it will
# complete building the module jar.
-fixupmessages: \
	"Classes found in the wrong directory...";is:=ignore,\
	"No sub JAR or directory...";is:=ignore
```


