package org.valabs.tplParser;

import java.io.File;
import java.io.IOException;

/**
 * Утилита для генерации классов сообщений ODISP на основе шаблонов.
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
 * @author (С) 2004 НПП "Новел-ИЛ"
 * @version $Id: TplParser.java,v 1.12 2004/10/22 11:46:43 boris Exp $
 * 
 * Пример шаблонов:
 * 
 * NAME [пакет] [имя класса] [название ODISP action] IMPORT [пакет] (*)
 * 
 * AUTHOR [автор (для тега (a)author)] (*)
 * 
 * DESC [описание сообщения] (*) FIELD [имя поля (с заглавной буквы)] [тип поля]
 * 
 * FCHECK [имя поля] [выражение для проверки в boolean checkMessage()] (**)
 * 
 * FDESC [имя поля] [описание поля] (*) DEFORIGIN [точка отправления
 * по-умолчанию]
 * 
 * DEFDEST [точка назначения по-умолчанию]
 * 
 * DEFID [ReplyId сообщения по-умолчанию]
 * 
 * DEFROUTABLE [Routable по-умолчанию]
 * 
 * DEFREPLTO [номер сообщения на которое производится ответ по умолчанию]
 * 
 * DEFOOB [OOB по-умолчанию]
 * 
 * VERBATIM (***)
 * 
 * Версия для тега (a)version берется из CVS-тега Id.
 * 
 * (*) Поддерживаются multiline comments, все поля комментариев, должны
 * начинаться с нового ключевого слова. Например: AUTHOR 1 строка AUTHOR 2
 * строка
 * 
 * (**) Значение по-умолчанию get[имя поля](msg) != null
 * 
 * (***) VERBATIM включает режим переноса текста в результирующее сообщение.
 * Выключается повторным VERBATIM. Может встречаться несколько раз, но результат
 * будет выведен только в конце сообщения.
 */

public class TplParser {

    private static boolean cleanOnly = false;

    /**
     * Главный класс парсера
     * 
     * @param args
     *            параметры вызова
     */
    public static void main(String[] args) {
        TplParser newTplParser = new TplParser();
        System.out.println("TPL parser started");
        if (args.length > 0 && args[0] == "clean") {
            cleanOnly = true;
            System.out.println("Performing clean only");
        }
        File f = new File(".");
        newTplParser.listDir(f);
        System.out.println("TPL parser finished");
    }

    /**
     * Рекурсивный поиск файлов по директориям
     * 
     * @param f
     *            указатель на директорию где искать
     */
    private void listDir(File f) {
        File fileList[] = f.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                listDir(fileList[i]);
            } else {
                if (isFileMach(fileList[i].getName())) {
                    processFile(fileList[i]);
                }
            }
        }
    }

    /**
     * Проверка соответсвия найденного файла нашим критериям
     * 
     * @param fileName
     *            имя файла
     * 
     * @return возвращает true если файл подходит false если не подходит
     */
    private boolean isFileMach(String fileName) {
        return fileName.endsWith(".tpl");
    }

    /**
     * Обработка tpl файла и создание соответствующего файла .java
     * 
     * @param tplFile
     *            имя файла
     */
    private void processFile(File tplFile) {
        System.out.println("Parsing tpl: " + tplFile.getName());

        File javaFile = new File(tplFile.getPath().replaceAll(".tpl$", ".java"));
        try {
            javaFile.delete();
            if (cleanOnly) {
                System.out.println("Java file deleted " + javaFile.getPath());
            } else {
                if (javaFile.createNewFile()) {
                    tplProcessor tplProc = new tplProcessor(tplFile.getPath(),
                        javaFile.getPath());
                    if (tplProc.go()) {
                        System.out.println("Java file created "
                            + javaFile.getName());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}