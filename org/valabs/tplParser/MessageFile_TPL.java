/* ODISP -- Message Oriented Middleware
 * Copyright (C) 2003-2005 Valentin A. Alekseev
 * Copyright (C) 2003-2005 Andrew A. Porohin 
 * 
 * ODISP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 2.1 of the License.
 * 
 * ODISP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ODISP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;


/** �������� TPL ����� �� �������� ���������.
 * �������� ��� tplclipse.
 * @author <a href="mailto:valeks@valabs.spb.ru">�������� �������� �.</a>
 * @version $Id: MessageFile_TPL.java,v 1.3 2006/01/23 11:11:30 valeks Exp $
 */
public class MessageFile_TPL implements MessageFile {
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
