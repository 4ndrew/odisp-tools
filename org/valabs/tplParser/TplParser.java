import java.io.File;
import java.io.IOException;

/** ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
 * @author <a href="boris@novel-il.ru">����������� ����� �.</a>
 * @author (�) 2004 ��� "�����-��"
 * @version $Id: TplParser.java,v 1.6 2004/10/21 13:32:08 boris Exp $
 *
 * ������ ��������:
 *
 * NAME [�����] [��� ������] [�������� ODISP action]
 * IMPORT [�����] (*)
 * AUTHOR [����� (��� ���� @author)] (*)
 * DESC [�������� ���������] (*)
 * FIELD [��� ���� (� ��������� �����)] [��� ����]
 * FCHECK [��� ����] [��������� ��� �������� � checkMessage 
 * 		(������ ���������� boolean)] (**)
 * FDESC [��� ����] [�������� ����] (*)
 * DEFORIGIN [����� ����������� ��-���������]
 * DEFDEST [����� ���������� ��-���������]
 * DEFID [ReplyId ��������� ��-���������]
 * DEFROUTABLE [Routable ��-���������]
 * DEFREPLTO [����� ��������� �� ������� ������������ ����� �� ���������]
 * DEFOOB [OOB ��-���������]
 * VERBATIM (***)
 * ������ ��� ���� @version ������� �� CVS-���� Id.
 * 
 * (*) �������������� multiline comments, ��� ���� ������������, ������
 * ���������� � ������ ��������� �����.
 * ��������:
 * AUTHOR 1 ������
 * AUTHOR 2 ������
 * (**) �������� ��-��������� get[��� ����](msg) != null
 * (***) VERBATIM �������� ����� �������� ������ � �������������� ���������. 
 * 			 ����������� ��������� VERBATIM. ����� ����������� ��������� ���, 
 * 			 �� ��������� ����� ������� ������ � ����� ���������.
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
