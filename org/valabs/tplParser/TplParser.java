import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/** ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
 * @author ����������� ����� �.
 * @author (�) 2004 ��� "�����-��"
 * @version $Id: TplParser.java,v 1.2 2004/10/18 11:15:50 boris Exp $
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
  
  String tagNAMEstrings = "";
  String tagIMPORTstrings = "";
  String tagAUTHORstrings = "";
  String tagDESCstrings = "";
  String tagFIELDstrings = "";
  String tagFCHECKstrings = "";
  String tagFDESCstrings = "";
  String tagDEFORIGINstrings = "";
  String tagDEFDESTstrings = "";
  String tagDEFIDstrings = "";
  String tagDEFROUTABLEstrings = "";
  String tagDEFOOBstrings = "";
  String tagNOTAGstrings = "";
  String tagCVSidstrings = "";
  
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
				parseTpl(tplFile.getPath(), javaFile.getPath());
			}
		} catch (IOException e) {
			System.err.println("IOException");
		}	
	}
	
	/**
	 * 
	 * @param tplName
	 * @param javaName
	 */
	private void parseTpl(String tplName, String javaName){
	  BufferedReader tplReader = null; 
	  PrintStream javaWriter = null;
	  
	  try {
      tplReader = new BufferedReader(new FileReader(tplName));
      javaWriter = new PrintStream(new FileOutputStream(javaName));
    } catch (FileNotFoundException e) {
      System.err.println("File not found: "+e.getMessage());
      return;
    }

    try {
      String line;
      while ((line = tplReader.readLine()) != null){
        parseTagLine(line);
      }
    } catch (IOException e) {
      System.err.println("IOException on reading TPL");
    }
    writeJavaFile(javaWriter);
	}

  /**
   * @param javaWriter
   */
	private void writeJavaFile(PrintStream javaWriter) {
    javaWriter.println("java file created");
    System.out.println(tagAUTHORstrings);
  }

  /**
   * @param line
   */
  private void parseTagLine(String tagLine) {
    tagNAME(tagLine);
    tagIMPORT(tagLine);
    tagAUTHOR(tagLine);
    tagDESC(tagLine);
    tagFIELD(tagLine);
    tagFCHECK(tagLine);
    tagFDESC(tagLine);
    tagDEFORIGIN(tagLine);
    tagDEFDEST(tagLine);
    tagDEFID(tagLine);
    tagDEFROUTABLE(tagLine);
    tagDEFOOB(tagLine);
    tagNOTAG(tagLine);
    tagCVSid(tagLine);
    
  }

  /**
   * @param tagLine
   */
  private void tagNAME(String tagLine) {
    if (tagLine.startsWith("NAME")){
      tagNAMEstrings += tagLine + "\n";
    }    
  }
  
  /**
   * @param tagLine
   */
  private void tagCVSid(String tagLine) {
    if (tagLine.startsWith("$")){
      tagCVSidstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagNOTAG(String tagLine) {
    if (!tagLine.startsWith("#")){//����������� ����������
      tagNOTAGstrings += tagLine + "\n";
    }
  }

  /**
   * @param tagLine
   */
  private void tagDEFOOB(String tagLine) {
    if (tagLine.startsWith("DEFOOB")){
      tagDEFOOBstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagDEFROUTABLE(String tagLine) {
    if (tagLine.startsWith("DEFROUTABLE")){
      tagDEFROUTABLEstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagDEFID(String tagLine) {
    if (tagLine.startsWith("DEFID")){
      tagDEFIDstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagDEFDEST(String tagLine) {
    if (tagLine.startsWith("DEFDEST")){
      tagDEFDESTstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagDEFORIGIN(String tagLine) {
    if (tagLine.startsWith("DEFORIGIN")){
      tagDEFORIGINstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagFDESC(String tagLine) {
    if (tagLine.startsWith("FDESC")){
      tagFDESCstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagFCHECK(String tagLine) {
    if (tagLine.startsWith("FCHECK")){
      tagFCHECKstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagFIELD(String tagLine) {
    if (tagLine.startsWith("FIELD")){
      tagFIELDstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagDESC(String tagLine) {
    if (tagLine.startsWith("DESC")){
      tagDESCstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagAUTHOR(String tagLine) {
    if (tagLine.startsWith("AUTHOR")){
      tagAUTHORstrings += tagLine + "\n";
    }    
  }

  /**
   * @param tagLine
   */
  private void tagIMPORT(String tagLine) {
    if (tagLine.startsWith("IMPORT")){
      tagIMPORTstrings += tagLine + "\n";
    }    
  }

}
