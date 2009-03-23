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

/**
 * @author valeks
 * @author (C) 2005 НПП "Новел-ИЛ"
 * @version $Id: MessageFile_HTML.java,v 1.1 2005/07/01 09:17:29 valeks Exp $
 */
public class MessageFile_HTML implements MessageFile {

	public String getExtension() {
		return ".html";
	}

	public void writeFile(TplFile tplSource, OutputStream out)
			throws IOException {
		write(out, "<html>\n  <head>\n    <title>" + tplSource.getMessageName() + "</title>\n  </head>\n  <body>");
		
	    writeClassJavadoc(tplSource, out);

	    writeMessageConstants(tplSource, out);

	    writeCheckMessage(tplSource, out);

	    writeSetup(tplSource, out);

	    write(out, "      <tr><td>Тип</td><td>Название</td><td>Описание</td></tr>\n");
	    final Iterator it = tplSource.getFields().keySet().iterator();
	    while (it.hasNext()) {
	      final String fieldName = (String) it.next();
	      writeField(tplSource, out, fieldName);
	    }

	    writeVerbatim(tplSource, out);
	    
	    switch (tplSource.getType()) {
	      case TplFile.TYPE_PLAIN:
	        System.out.print(", main");
	        break;
	      case TplFile.TYPE_ERROR:
	        System.out.print(", error");
	        break;
	      case TplFile.TYPE_REPLY:
	        System.out.print(", reply");
	        break;
	      case TplFile.TYPE_NOTIFY:
	        System.out.print(", notify");
	        break;
	      default:
	      	throw new IOException("Unknown message type used");
	    }
		
		write(out, "  </body>\n</html>\n");
		System.out.print("(html)");
	}
	
	/**
	 * @param tplSource
	 * @param out
	 * @throws IOException
	 */
	private void writeVerbatim(TplFile tplSource, OutputStream out) throws IOException {
		write(out, "      <tr><td><h3>Дополнительный код:</h3></td></tr>\n");
		write(out, "      <tr><td><pre>\n");
		Iterator it = tplSource.getVerbatim().iterator();
		while (it.hasNext()) {
			String element = (String) it.next();
			write(out, element + "\n");
		}
		write(out, "      </pre></td></tr>\n");
	}

	/**
	 * @param tplSource
	 * @param out
	 * @param fieldName
	 */
	private void writeField(TplFile tplSource, OutputStream out, String fieldName) throws IOException {
		final FieldRecord fr = (FieldRecord) tplSource.getFields().get(fieldName);
		write(out, "        <tr>\n");
		write(out, "          <td>" +  fr.getType() + "</td>\n");
		write(out, "          <td>" + fr.getName() + "</td>\n");
		write(out, "          <td>" + fr.getDesc() + "</td>\n");
		write(out, "       </tr>\n");
	}

	/**
	 * @param tplSource
	 * @param out
	 */
	private void writeSetup(TplFile tplSource, OutputStream out) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param tplSource
	 * @param out
	 */
	private void writeCheckMessage(TplFile tplSource, OutputStream out) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param tplSource
	 * @param out
	 */
	private void writeMessageConstants(TplFile tplSource, OutputStream out) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param tplSource
	 * @param out
	 */
	private void writeClassJavadoc(TplFile tplSource, OutputStream out) {
		// TODO Auto-generated method stub
		
	}

	private void write(final OutputStream out, final String str)
			throws IOException {
		out.write(str.getBytes());
	}
}
