package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;


/** �������� TPL ����� �� �������� ���������.
 * �������� ��� tplclipse.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MessageFile_TPL.java,v 1.2 2005/04/27 09:25:39 valeks Exp $
 */
class MessageFile_TPL implements MessageFile {
  public String getExtension() {
    return ".tpl";
  }

  public void writeFile(final TplFile tplSource, final OutputStream out) throws IOException {
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
  
  /** ����� ������ � �������� ���������.
   * @param toPrint
   * @param prefix
   * @param out
   * @throws IOException
   */
  private void writePrefixedList(final String prefix, final List toPrint, final OutputStream out) throws IOException {
    final Iterator it = toPrint.iterator();
    while (it.hasNext()) {
      final String ath = (String) it.next();
      write(out, prefix + " " + ath + "\n");
    }
  }

  private void write(final OutputStream out, final String str) throws IOException {
    out.write(str.getBytes());
  }
}