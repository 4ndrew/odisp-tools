package org.valabs.tplParser;

import java.io.File;
import java.io.IOException;

/**
 * ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (�) 2004 ��� "�����-��"
 * @version $Id: TplParser.java,v 1.12 2004/10/22 11:46:43 boris Exp $
 * 
 * ������ ��������:
 * 
 * NAME [�����] [��� ������] [�������� ODISP action] IMPORT [�����] (*)
 * 
 * AUTHOR [����� (��� ���� (a)author)] (*)
 * 
 * DESC [�������� ���������] (*) FIELD [��� ���� (� ��������� �����)] [��� ����]
 * 
 * FCHECK [��� ����] [��������� ��� �������� � boolean checkMessage()] (**)
 * 
 * FDESC [��� ����] [�������� ����] (*) DEFORIGIN [����� �����������
 * ��-���������]
 * 
 * DEFDEST [����� ���������� ��-���������]
 * 
 * DEFID [ReplyId ��������� ��-���������]
 * 
 * DEFROUTABLE [Routable ��-���������]
 * 
 * DEFREPLTO [����� ��������� �� ������� ������������ ����� �� ���������]
 * 
 * DEFOOB [OOB ��-���������]
 * 
 * VERBATIM (***)
 * 
 * ������ ��� ���� (a)version ������� �� CVS-���� Id.
 * 
 * (*) �������������� multiline comments, ��� ���� ������������, ������
 * ���������� � ������ ��������� �����. ��������: AUTHOR 1 ������ AUTHOR 2
 * ������
 * 
 * (**) �������� ��-��������� get[��� ����](msg) != null
 * 
 * (***) VERBATIM �������� ����� �������� ������ � �������������� ���������.
 * ����������� ��������� VERBATIM. ����� ����������� ��������� ���, �� ���������
 * ����� ������� ������ � ����� ���������.
 */

public class TplParser {

    private static boolean cleanOnly = false;

    /**
     * ������� ����� �������
     * 
     * @param args
     *            ��������� ������
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
     * ����������� ����� ������ �� �����������
     * 
     * @param f
     *            ��������� �� ���������� ��� ������
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
     * �������� ����������� ���������� ����� ����� ���������
     * 
     * @param fileName
     *            ��� �����
     * 
     * @return ���������� true ���� ���� �������� false ���� �� ��������
     */
    private boolean isFileMach(String fileName) {
        return fileName.endsWith(".tpl");
    }

    /**
     * ��������� tpl ����� � �������� ���������������� ����� .java
     * 
     * @param tplFile
     *            ��� �����
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