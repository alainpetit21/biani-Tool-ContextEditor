#!/bin/sh

java -classpath 'dist:dist/lib:dist/ContextEditor.jar' -Djava.library.path='dist:dist/lib/solaris-sparc:dist/ContextEditor.jar' com.bianisoft.tools.contexteditor.ContextEditor
