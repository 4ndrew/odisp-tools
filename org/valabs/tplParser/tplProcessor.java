
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: insert comment there
 * 
 * @author <a href="boris@novel-il.ru">Волковыский Борис В. </a>
 * @author (C) 2004 НПП "Новел-ИЛ"
 * @version $Id: tplProcessor.java,v 1.4 2004/10/20 10:04:38 boris Exp $
 */
public class tplProcessor {

  private String packageName = "";

  private String messageName = "";

  private String actionName = "";

  private String tagNAMEstrings = "";

  private String tagIMPORTstrings = "";

  private String tagAUTHORstrings = "";

  private String tagDESCstrings = "";

  private List tagFIELDstrings = new ArrayList();

  private List tagFCHECKstrings = new ArrayList();

  private List tagFDESCstrings = new ArrayList();

  private String tagDEFORIGINstrings = "";

  private String tagDEFDESTstrings = "";

  private String tagDEFIDstrings = "";

  private boolean tagDEFROUTABLE = false;

  private boolean tagDEFOOB = false;

  private String tagNOTAGstrings = "";

  private String tagCVSidstrings = "";

  private BufferedReader tplReader = null;

  private PrintStream javaWriter = null;

  /**
   * 
   * @param tplName
   * @param javaName
   */
  tplProcessor(String tplName, String javaName) {
    try {
      tplReader = new BufferedReader(new FileReader(tplName));
      javaWriter = new PrintStream(new FileOutputStream(javaName));
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
      return;
    }
  }

  private void show(String s) {
    System.out.println(s);
  }

  boolean go() {
    try {
      String line;
      while ((line = tplReader.readLine()) != null) {
        parseTagLine(line);
      }
    } catch (IOException e) {
      System.err.println("IOException on reading TPL");
      return false;
    }
    writeJavaFile(javaWriter);
    return true;
  }
  
  private void write(String s) {
    javaWriter.println(s);
  }

  private void writeJavaFile(PrintStream javaWriter) {
    write("package " + packageName + ";\n\n" +
        "import org.valabs.odisp.common.Message;\n");
    
    if (tagIMPORTstrings != "") {
      write(tagIMPORTstrings + "\n\n");
    }
    
    write( "/** " + tagDESCstrings + "\n" +
        	" *\n" +
        	tagAUTHORstrings +
        	tagCVSidstrings + "\n" +
        	" */\n");
    
    write("public final class " + messageName + " {\n" +
        	"  /** Строковое представление сообщения. */\n" +
        	"  public static final String NAME = \"" + actionName + "\";\n\n");
    
    write("");
  }

  /**
   * @param line
   */
  private void parseTagLine(String tagLine) {
    if (tagLine.startsWith("NAME")) {
      String[] s = tagLine.split(" ");
      packageName = s[1];
      messageName = s[2];
      actionName = s[3];
    }

    if (tagLine.startsWith("$")) {
      tagCVSidstrings = " * @version " + tagLine;
    }

    if (tagLine.startsWith("IMPORT")) {
      tagIMPORTstrings += "import " + tagLine.split(" ")[1] + ";\n";
    }

    if (tagLine.startsWith("AUTHOR")) {
      tagAUTHORstrings += " * @author " + tagLine.substring(8) + "\n";
    }

    if (tagLine.startsWith("DESC")) {
      tagDESCstrings += tagLine.substring(5);
    }

    if (tagLine.startsWith("FIELD")) {
      tagFIELDstrings.add(tagLine.substring(6));
    }

    if (tagLine.startsWith("FCHECK")) {
      tagFCHECKstrings.add(tagLine.substring(7));
    }

    if (tagLine.startsWith("FDESC")) {
      tagFDESCstrings.add(tagLine.substring(6));
    }

    if (tagLine.startsWith("DEFORIGIN")) {
      tagDEFORIGINstrings += tagLine.split(" ")[1];
    }

    if (tagLine.startsWith("DEFDEST")) {
      tagDEFDESTstrings += tagLine.split(" ")[1];
    }

    if (tagLine.startsWith("DEFID")) {
      tagDEFIDstrings += tagLine.split(" ")[1];
    }

    if (tagLine.startsWith("DEFROUTABLE")) {
      tagDEFROUTABLE = true;
    }

    if (tagLine.startsWith("DEFOOB")) {
      show("WARNING: Generating OOB message " + messageName);
      tagDEFOOB = true;
    }

    if (!tagLine.startsWith("#")) {
      //TODO may be it's good to skip empty lines
      //комментарии пропускаем
      tagNOTAGstrings += tagLine + "\n";
    }
  }

}