lexer grammar CustomizableLexer;

options { superClass=AbstractCustomizableLexer; }

@header{
package de.tudortmund.cs.iltis.utils.io.parser.customizable;
}

// fragment not possible due to action;
// ] and \ and - have to be escaped by \
// furthermore supported: \n, \r, \b, \t, and \f
TEXT : .+
       { tokenize(getText()); }
     ;
