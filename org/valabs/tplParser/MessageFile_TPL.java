package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;


/** Создание TPL файла по заданной структуре.
 * Набросок для tplclipse.
 * @author <a href="mailto:valeks@valabs.spb.ru">Алексеев Валентин А.</a>
 * @version $Id: MessageFile_TPL.java,v 1.1 2005/02/23 00:15:10 valeks Exp $
 */
class MessageFile_TPL implements MessageFile {
  public String getExtension() {
    return ".tpl";
  }

  public void writeFile(TplFile tplSource, OutputStream out) throws IOException {
    if (tplSource.getCvsId() != null) {
      write(out, tplSource.getCvsId() + "\n");
    }
    write(out, "NAME " + tplSource.getPackageName() + " " + tplSource.getMessageName() + " " + tplSource.getActionName() + "\n");

    if (tplSource.getDescription().size() > 0) {
      writePrefixedList("DESC", tplSource.getDescription(), out);
    }
    
    if (tplSource.getAuthors().size() > 0) {
      writePrefixedList("AUTHOR", tplSource.getAuthors(), out);
    }
  }
  
  /** Вывод списка с заданным префиксом.
   * @param toPrint
   * @param prefix
   * @param out
   * @throws IOException
   */
  private void writePrefixedList(String prefix, List toPrint, OutputStream out) throws IOException {
    Iterator it = toPrint.iterator();
    while (it.hasNext()) {
      String ath = (String) it.next();
      write(out, prefix + " " + ath + "\n");
    }
  }

  private void write(OutputStream out, String str) throws IOException {
    out.write(str.getBytes());
  }
}
