import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;


/** TODO: insert comment there
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В.</a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: tplProcessor.java,v 1.1 2004/10/18 13:15:41 boris Exp $
 */
public class tplProcessor {

  private String packageName = "";
  private String messageName = "";
  private String actionName = "";
  private String tagNAMEstrings = "";
  private String tagIMPORTstrings = "";
  private String tagAUTHORstrings = "";
  private String tagDESCstrings = "";
  private String tagFIELDstrings = "";
  private String tagFCHECKstrings = "";
  private String tagFDESCstrings = "";
  private String tagDEFORIGINstrings = "";
  private String tagDEFDESTstrings = "";
  private String tagDEFIDstrings = "";
  private String tagDEFROUTABLEstrings = "";
  private String tagDEFOOBstrings = "";
  private String tagNOTAGstrings = "";
  private String tagCVSidstrings = "";
  private BufferedReader tplReader = null; 
  private PrintStream javaWriter = null;
  
  /**
	 * 
	 * @param tplName
	 * @param javaName
	 */
	tplProcessor(String tplName, String javaName){
	  try {
      tplReader = new BufferedReader(new FileReader(tplName));
      javaWriter = new PrintStream(new FileOutputStream(javaName));
    } catch (FileNotFoundException e) {
      System.err.println("File not found: "+e.getMessage());
      return;
    }
	}
	
	private void show(String s){
	  System.out.println(s);
	}
	
	boolean go(){
    try {
      String line;
      while ((line = tplReader.readLine()) != null){
        parseTagLine(line);
      }
    } catch (IOException e) {
      System.err.println("IOException on reading TPL");
      return false;
    }
    writeJavaFile(javaWriter);
    return true;
	}

	private void writeJavaFile(PrintStream javaWriter) {
    javaWriter.println("java file created");
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

  private void tagNAME(String tagLine) {
    if (tagLine.startsWith("NAME")){
      String[] s = tagLine.split(" ");
      packageName = s[1];
      messageName = s[2];
      actionName = s[3];
    }    
  }
  
  private void tagCVSid(String tagLine) {
    if (tagLine.startsWith("$")){
      tagCVSidstrings = tagLine;
    }    
  }

  private void tagIMPORT(String tagLine) {
    if (tagLine.startsWith("IMPORT")){
      tagIMPORTstrings += tagLine.split(" ")[1];
    }    
  } 
  
  private void tagAUTHOR(String tagLine) {
    if (tagLine.startsWith("AUTHOR")){
      tagAUTHORstrings += " * @author " + tagLine.substring(8) + "\n";
    }    
  }

  private void tagDESC(String tagLine) {
    if (tagLine.startsWith("DESC")){
      tagDESCstrings += " * " + tagLine.substring(6) + "\n";
    }    
  }
  
  private void tagFIELD(String tagLine) {
    if (tagLine.startsWith("FIELD")){
      tagFIELDstrings += tagLine + "\n";
    }    
  }
  
  private void tagNOTAG(String tagLine) {
    if (!tagLine.startsWith("#")){//комментарии пропускаем
      tagNOTAGstrings += tagLine + "\n";
    }
  }

  private void tagDEFOOB(String tagLine) {
    if (tagLine.startsWith("DEFOOB")){
      tagDEFOOBstrings += tagLine + "\n";
    }    
  }

  private void tagDEFROUTABLE(String tagLine) {
    if (tagLine.startsWith("DEFROUTABLE")){
      tagDEFROUTABLEstrings += tagLine + "\n";
    }    
  }

  private void tagDEFID(String tagLine) {
    if (tagLine.startsWith("DEFID")){
      tagDEFIDstrings += tagLine + "\n";
    }    
  }

  private void tagDEFDEST(String tagLine) {
    if (tagLine.startsWith("DEFDEST")){
      tagDEFDESTstrings += tagLine + "\n";
    }    
  }

  private void tagDEFORIGIN(String tagLine) {
    if (tagLine.startsWith("DEFORIGIN")){
      tagDEFORIGINstrings += tagLine + "\n";
    }    
  }

  private void tagFDESC(String tagLine) {
    if (tagLine.startsWith("FDESC")){
      tagFDESCstrings += tagLine + "\n";
    }    
  }

  private void tagFCHECK(String tagLine) {
    if (tagLine.startsWith("FCHECK")){
      tagFCHECKstrings += tagLine + "\n";
    }    
  }

}
