
import java.io.File;
import java.io.IOException;

/**
 * ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (�) 2004 ��� "�����-��"
 * @version $Id: TplParser.java,v 1.8 2004/10/22 06:27:55 boris Exp $
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

    /**
     * ������� ����� �������
     * 
     * @param args
     *            ��������� ������
     */
    public static void main(String[] args) {
        TplParser newTplParser = new TplParser();
        System.out.println("TPL parser started");
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
        System.out.println("Parsing tpl: " + tplFile.getPath());

        File javaFile = new File(tplFile.getPath().replaceAll(".tpl$", ".java"));
        try {
            javaFile.delete();
            if (javaFile.createNewFile()) {
                tplProcessor tplProc = new tplProcessor(tplFile.getPath(),
                    javaFile.getPath());
                if (tplProc.go()) {
                    System.out.println("Java file created "
                        + javaFile.getPath());
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}