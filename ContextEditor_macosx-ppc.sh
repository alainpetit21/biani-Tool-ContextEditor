#!/bin/sh

java -classpath 'dist:dist/lib:dist/ContextEditor.jar' -Djava.library.path='dist:dist/lib/macosx-ppc:dist/ContextEditor.jar' com.bianisoft.tools.contexteditor.ContextEditor
