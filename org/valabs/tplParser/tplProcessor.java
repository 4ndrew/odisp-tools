
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * TODO: insert comment there
 * 
 * @author <a href="boris@novel-il.ru">����������� ����� �. </a>
 * @author (C) 2004 ��� "�����-��"
 * @version $Id: tplProcessor.java,v 1.7 2004/10/21 13:32:08 boris Exp $
 */
public class tplProcessor {

    private String packageName = "";

    private String messageName = "";

    private String actionName = "";

    private String tagNAMEstrings = "";

    private String tagIMPORTstrings = "";

    private String tagAUTHORstrings = "";

    private String tagDESCstrings = "";

    private String tagDEFORIGINstrings = "";

    private String tagDEFDESTstrings = "";

    private String tagDEFIDstrings = "";

    private boolean tagDEFROUTABLE = false;

    private boolean tagDEFOOB = false;

    private String tagDEFREPLTOstrings = "";

    private String tagNOTAGstrings = "";

    private String tagCVSidstrings = "";

    private BufferedReader tplReader = null;

    private PrintStream javaWriter = null;

    private HashMap fields;

    /**
     * 
     * @param tplName
     * @param javaName
     */
    tplProcessor(String tplName, String javaName) {
        fields = new HashMap();
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
        write("package " + packageName + ";\n");
        write("import org.valabs.odisp.common.Message;\n");

        if (tagIMPORTstrings != "") {
            write(tagIMPORTstrings);
        }

        write("/** " + tagDESCstrings);
        write(" *");
        write(tagAUTHORstrings);
        write(tagCVSidstrings);
        write(" */");

        write("public final class " + messageName + " {");
        write("  /** ��������� ������������� ���������. */");
        write("  public static final String NAME = \"" + actionName + "\";\n");

        Iterator it = fields.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            write("  /** ������ ��� ���� " + key + ". */");
            write("  private static String idx" + key.toUpperCase() + " = \""
                + key.toLowerCase() + "\";");
        }

        write("\n");
        write("  /** ������ �� �������� �������. */");
        write("  private " + messageName + "() { }\n");

        write("  /** �������� ��������� �� ������������.");
        write("   *");
        write("   * @param msg ���������");
        write("   */");
        write("  private static void checkMessage(final Message msg) {");

        if (fields.keySet().size() != 0) {
            write("    try {");
            Iterator fieldIterator = fields.keySet().iterator();
            while (fieldIterator.hasNext()) {
                String key = (String) fieldIterator.next();
                write("      assert get" + key
                    + "(msg) != null : \"Message field " + key + " is null.\";");
            }
            write("    } catch (AssertionError e) {");
            write("      System.err.println(\"Message assertion :\" + e.toString());");
            write("      e.printStackTrace();\n    }\n");
            write("    msg.setCorrect(");

            fieldIterator = fields.keySet().iterator();
            int flag = 0;
            while (fieldIterator.hasNext()) {
                String fieldName = (String) fieldIterator.next();
                String fieldCheck = ((FieldRecord) fields.get(fieldName))
                        .getCheck();

                if (fieldCheck.trim().length() == 0) {
                    fieldCheck = "get" + fieldName + "(msg) != null";
                }
                if (flag == 1) {
                    write("      && " + fieldCheck);
                } else {
                    write("      " + fieldCheck);
                    flag = 1;
                }
            }

        } else {
            write("    msg.setCorrect(\n      true");
        }
        write("    );");
        write("  }\n");

        write("  /** ������������� �������� ������� ���������.");
        write("   *");
        write("   * @param msg ���������.");
        if (tagDEFDESTstrings == "") {
            write("   * @param destination ����� ����������.");
        }
        if (tagDEFORIGINstrings == "") {
            write("   * @param origin ����� �����������.");
        }
        write("   * @param replyTo ������������� ���������, �� ������� ��� �������� �������.");
        write("   * @return ������ �� ������������������ ���������");
        write("   */");
        String str = "  public static Message setup(final Message msg";
        if (tagDEFDESTstrings == "") {
            str += ", final String destination";
        }
        if (tagDEFORIGINstrings == "") {
            str += ", final String origin";
        }
        if (tagDEFREPLTOstrings == "") {
            str += ", final int replyTo) {";
        }
        write(str);

        write("    msg.setAction(NAME);");
        if (tagDEFDESTstrings == "") {
            write("    msg.setDestination(destination);");
        } else {
            write("    msg.setDestination(\"" + tagDEFDESTstrings + "\");");
        }
        if (tagDEFORIGINstrings == "") {
            write("    msg.setOrigin(origin);");
        } else {
            write("    msg.setOrigin(\"" + tagDEFORIGINstrings + "\");");
        }
        if (tagDEFREPLTOstrings == "") {
            write("    msg.setReplyTo(replyTo);");
        } else {
            write("    msg.setReplyTo(" + tagDEFREPLTOstrings + ");");
        }
        if (tagDEFROUTABLE) {
            write("    msg.setRoutable(false);");
        }
        if (tagDEFOOB) {
            write("    msg.setOOB(true);");
        }
        write("    checkMessage(msg);");
        write("    return msg;");
        write("  }\n");
/*************************************************************************************************
    gsub(/\\n/, "\n", fields_desc[key]);
    printf  "  /** ���������� " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   *\n" \
            "   * @param msg ��������� ��� ������� ������������ �������.\n" \
            "   * @param newValue ����� �������� ��� ����.\n" \
	    "   * @return ������ �� ���������\n" \
            "   */\n" \
            "  public static Message set" key "(final Message msg, final " fields_type[key] " newValue) {\n" \
            "    msg.addField(idx" toupper(key) ", newValue);\n" \
            "    checkMessage(msg);\n" \
            "    return msg;\n" \
            "  }\n\n" \
            "  /** �������� " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   *\n" \
            "   * @param msg ��������� ��� ������� ������������ �������.\n" \
	    "   * @return �������� ����\n" \
            "   */\n" \
            "  public static " fields_type[key] " get" key "(final Message msg) {\n" \
            "    return (" fields_type[key] ") msg.getField(idx" toupper(key) ");\n" \
            "  }\n\n";
  }

  printf "  /** �������� �� ��������� ���������� ����� ����.\n" \
         "   *\n" \
         "   * @param msg ���������.\n" \
         "   * @return true - ���� ��������, false - �����.\n" \
         "   */\n" \
         "  public static boolean equals(final Message msg) {\n" \
         "    return msg.getAction().equals(NAME);\n" \
         "  }\n\n";
  printf "  /** ����������� ����� �� ������ ��������� � ������.\n" \
  		 "  *\n" \
  		 "  * @param dest ����������.\n" \
  		 "  * @param src ��������.\n" \
  		 "  */\n" \
  		 "  public static void copyFrom(final Message dest, final Message src) {\n";
   for (key in fields_type) {
     printf "    set" key "(dest, get" key"(src));\n";
   }
  printf "  }\n\n";
  printf "  /** ������������� ����������� hash-���� ���������.\n" \
         "   * ������ ����� 0.\n" \
	 "   * @return hash-��� ���������.\n" \
	 "   */\n" \
	 "  public int hashCode() {\n" \
	 "    return 0;\n" \
	 "  }\n\n";
  printf "  /** �������� ������ ���������� ���� ����� ��������� �����.\n" \
    "   * @return ������ �� ���������\n";
  for (key in fields_desc) {
    printf "   * @param " tolower(key) " " fields_desc[key] "\n";
  }
  printf "  */\n";
  printf "  public static Message initAll(final Message m";
  
  for (i = 0; i < paramc; i++) {
    key = fields_order[i];
    printf ",\n                               final " fields_type[key] " " tolower(key);
  }
  
  printf ") {\n";
  for (key in fields_type) {
    printf "    set" key "(m, " tolower(key) ");\n";
  }
  printf "    return m;\n  }\n\n";

  if (verbatimCode != "") {
    printf verbatimCode;
  }
  printf "\n\n}\n";
 };

 ********************************************************************************/
        
        
    }

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
            if (tagAUTHORstrings == "") {
                tagAUTHORstrings += " * @author " + tagLine.substring(7);
            } else {
                tagAUTHORstrings += "\n * @author " + tagLine.substring(7);
            }
        }

        if (tagLine.startsWith("DESC")) {
            tagDESCstrings += tagLine.substring(5);
        }

        if (tagLine.startsWith("FIELD")) {
            String[] tokens = tagLine.split(" ");
            if (!fields.containsKey(tokens[1])) {
                fields.put(tokens[1], new FieldRecord());
            }
            ((FieldRecord) fields.get(tokens[1])).setType(tokens[2]);
        }

        if (tagLine.startsWith("FCHECK")) {
            String[] tokens = tagLine.split(" ");
            if (!fields.containsKey(tokens[1])) {
                fields.put(tokens[1], new FieldRecord());
            }
            ((FieldRecord) fields.get(tokens[1])).setCheck(tagLine
                    .substring(7 + tokens[1].length()));
        }

        if (tagLine.startsWith("FDESC")) {
            String[] tokens = tagLine.split(" ");
            if (!fields.containsKey(tokens[1])) {
                fields.put(tokens[1], new FieldRecord());
            }
            ((FieldRecord) fields.get(tokens[1])).setDesc(tagLine
                    .substring(6 + tokens[1].length()));
        }

        if (tagLine.startsWith("DEFORIGIN")) {
            tagDEFORIGINstrings = tagLine.split(" ")[1];
        }

        if (tagLine.startsWith("DEFDEST")) {
            tagDEFDESTstrings = tagLine.split(" ")[1];
        }

        if (tagLine.startsWith("DEFID")) {
            tagDEFIDstrings = tagLine.split(" ")[1];
        }

        if (tagLine.startsWith("DEFROUTABLE")) {
            tagDEFROUTABLE = true;
        }

        if (tagLine.startsWith("DEFREPLTO")) {
            tagDEFREPLTOstrings = tagLine.split(" ")[1];
        }

        if (tagLine.startsWith("DEFOOB")) {
            show("WARNING: Generating OOB message " + messageName);
            tagDEFOOB = true;
        }

        if (!tagLine.startsWith("#")) {
            //����������� ����������
            //TODO may be it's good to skip empty lines
            tagNOTAGstrings += tagLine + "\n";
        }
    }

    class FieldRecord {

        private String type = "";

        private String check = "";

        private String desc = "";

        /**
         * @return Returns the check.
         */
        public String getCheck() {
            return check;
        }

        /**
         * @param check
         *            The check to set.
         */
        public void setCheck(String check) {
            this.check = check;
        }

        /**
         * @return Returns the desc.
         */
        public String getDesc() {
            return desc;
        }

        /**
         * @param desc
         *            The desc to set.
         */
        public void setDesc(String desc) {
            this.desc += "  *" + desc + "\n";
        }

        /**
         * @return Returns the type.
         */
        public String getType() {
            return type;
        }

        /**
         * @param type
         *            The type to set.
         */
        public void setType(String type) {
            this.type = type;
        }
    }

}