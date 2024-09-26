#!/bin/bash
java -cp "$ANTLRTOOLJAR:$CLASSPATH" org.antlr.v4.Tool -no-listener -lib $DIR $DIR/CustomizableLexer.g4
