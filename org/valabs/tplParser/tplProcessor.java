import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;


/**
 * TODO: insert comment there
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: tplProcessor.java,v 1.3 2004/10/18 14:03:51 boris Exp $
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
    if (tagLine.startsWith("NAME")){
      String[] s = tagLine.split(" ");
      packageName = s[1];
      messageName = s[2];
      actionName = s[3];
    }

    if (tagLine.startsWith("$")){
      tagCVSidstrings = " * @version " + tagLine + "\n";
    }    

    if (tagLine.startsWith("IMPORT")){
      tagIMPORTstrings +="imports " + tagLine.split(" ")[1] + "\n";
    }    

    if (tagLine.startsWith("AUTHOR")){
      tagAUTHORstrings += " * @author " + tagLine.substring(8) + "\n";
    }    

    if (tagLine.startsWith("DESC")){
      tagDESCstrings += " * " + tagLine.substring(6) + "\n";
    }    

    if (tagLine.startsWith("FIELD")){//TODO
      tagFIELDstrings += tagLine + "\n";
    }    

    if (tagLine.startsWith("FCHECK")){//TODO
      tagFCHECKstrings += tagLine + "\n";
    }   
    
    if (tagLine.startsWith("FDESC")){//TODO
      tagFDESCstrings += tagLine + "\n";
    } 

    if (tagLine.startsWith("DEFORIGIN")){//TODO
      tagDEFORIGINstrings += tagLine + "\n";
    } 

    if (tagLine.startsWith("DEFDEST")){//TODO
      tagDEFDESTstrings += tagLine + "\n";
    }    

    if (tagLine.startsWith("DEFID")){//TODO
      tagDEFIDstrings += tagLine + "\n";
    }  

    if (tagLine.startsWith("DEFROUTABLE")){//TODO
      tagDEFROUTABLEstrings += tagLine + "\n";
    }    

    if (tagLine.startsWith("DEFOOB")){//TODO
      tagDEFOOBstrings += tagLine + "\n";
    }    
    
    if (!tagLine.startsWith("#")){//TODO
      //комментарии пропускаем
      tagNOTAGstrings += tagLine + "\n";
    }
  }

}
