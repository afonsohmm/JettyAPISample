#!/bin/bash

#Copy library to Android project
cd target
if [ -d ~/AndroidStudioProjects/JettyAPIAndroid/ ]; then
	cp JettyAPICommons-*.jar ~/AndroidStudioProjects/JettyAPIAndroid/app/libs/JettyAPICommons.jar
fi
