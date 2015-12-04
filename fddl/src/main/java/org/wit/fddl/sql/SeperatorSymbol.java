package org.wit.fddl.sql;

/**
 * 
 * @author fangfan
 * 列举各种常用的语句符号,比如空格,逗号,分号等等
 *
 */
public interface SeperatorSymbol {
    
    String EMPTY = " ";
    String POINT = ".";
    String STAR = "*";
    String POINTSTAR = ".*";
    String COMMA = ",";
    String SEMICOLON = ":";
    String LEFT_PARENTHESIS="(";
    String RIGHT_PARENTHESIS=")";

}
