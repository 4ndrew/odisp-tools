package com.novel.tools.log;

import java.util.logging.*;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;

/** �������������� ��������� � ���� ������
* @author �������� �. ��������
* @author ��� "�����-��"
* @version $Id: OneLineFormatter.java,v 1.1 2003/10/22 20:49:38 valeks Exp $
*/
public class OneLineFormatter extends Formatter {
    /** ��������� �������������� ������. �������� ������ � ����:
    * HH:MM:SS LOGLEVEL CLASSNAME.METHOD: MESSAGE. PARAMETERS+
    * @param record ������ ��� �������
    * @return ����������������� ������
    */
    public String format(LogRecord record){
	String result = "";
	result+=DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date(record.getMillis()))+" ";
	result+=record.getLevel()+"\t";
	result+=record.getSourceClassName().substring(record.getSourceClassName().lastIndexOf(".")+1)+".";
	result+=record.getSourceMethodName()+": ";
	result+=record.getMessage()+".";
	if(record.getParameters() != null &&record.getParameters().length > 0){
	    Object[] params = record.getParameters();
	    for(int i = 0; i < params.length-1; i++)
		result+=params[i].toString()+", ";
	    result+=params[params.length].toString();
	}
	result+="\n";
	return result;
    }
}