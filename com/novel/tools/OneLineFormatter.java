package com.novel.tools.log;

import java.util.logging.*;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Форматирование сообщения в одну строку
* @author Валентин А. Алексеев
* @author НПП "Новел-ИЛ"
* @version $Id: OneLineFormatter.java,v 1.2 2003/10/22 21:45:54 valeks Exp $
*/
public class OneLineFormatter extends Formatter {
    /** Выполнить форматирование записи. Выходная строка в виде:
    * HH:MM:SS LOGLEVEL CLASSNAME.METHOD: MESSAGE. PARAMETERS+
    * @param record запись для анализа
    * @return отформатированная строка
    */
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public String format(LogRecord record){
	String result = "";
	result+=sdf.format(new Date(record.getMillis()))+" ";
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