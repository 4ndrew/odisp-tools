import java.io.File;
import java.io.IOException;

/** Утилита для генерации классов сообщений ODISP на основе шаблонов.
 * @author <a href="boris@novel-il.ru">Волковыский Борис В.</a>
 * @author (С) 2004 НПП "Новел-ИЛ"
 * @version $Id: TplParser.java,v 1.6 2004/10/21 13:32:08 boris Exp $
 *
 * Пример шаблонов:
 *
 * NAME [пакет] [имя класса] [название ODISP action]
 * IMPORT [пакет] (*)
 * AUTHOR [автор (для тега @author)] (*)
 * DESC [описание сообщения] (*)
 * FIELD [имя поля (с заглавной буквы)] [тип поля]
 * FCHECK [имя поля] [выражение для проверки в checkMessage 
 * 		(должно возвращать boolean)] (**)
 * FDESC [имя поля] [описание поля] (*)
 * DEFORIGIN [точка отправления по-умолчанию]
 * DEFDEST [точка назначения по-умолчанию]
 * DEFID [ReplyId сообщения по-умолчанию]
 * DEFROUTABLE [Routable по-умолчанию]
 * DEFREPLTO [номер сообщения на которое производится ответ по умолчанию]
 * DEFOOB [OOB по-умолчанию]
 * VERBATIM (***)
 * Версия для тега @version берется из CVS-тега Id.
 * 
 * (*) Поддерживаются multiline comments, все поля комментариев, должны
 * начинаться с нового ключевого слова.
 * Например:
 * AUTHOR 1 строка
 * AUTHOR 2 строка
 * (**) Значение по-умолчанию get[имя поля](msg) != null
 * (***) VERBATIM включает режим переноса текста в результирующее сообщение. 
 * 			 Выключается повторным VERBATIM. Может встречаться несколько раз, 
 * 			 но результат будет выведен только в конце сообщения.
 */

public class TplParser {
  
  /**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	  TplParser newTplParser = new TplParser();
	  System.out.println("I'm started!");
		File f = new File(".");
		newTplParser.listDir(f);
	}
	
	/**
	 * 
	 * @param f
	 */
	private void listDir(File f){
		File fileList[] = f.listFiles();
		for (int i = 0; i < fileList.length; i++){
			if (fileList[i].isDirectory()){
				listDir(fileList[i]);
			}else{
				if (isFileMach(fileList[i].getName())){
					processFile(fileList[i]);
				}
			}
		}			
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isFileMach(String fileName){
		return fileName.endsWith(".tpl");
	}
	
	/**
	 * 
	 * @param tplFile
	 */
	private void processFile(File tplFile){
		System.out.println("tpl :"+tplFile.getPath());
		
		File javaFile = new File(tplFile.getPath().replaceAll(".tpl$",".java"));
		System.out.println("java: "+javaFile.getPath());
		try {
			javaFile.delete();
			if (javaFile.createNewFile()){
				tplProcessor tplProc = new tplProcessor(tplFile.getPath(), javaFile.getPath());
				if (tplProc.go()){
				  System.out.println("File " + tplFile.getPath() + " parsed java file created");
				}
			}
		} catch (IOException e) {
			System.err.println("IOException");
		}	
	}
}
