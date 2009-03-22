package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author valeks
 * @version $Id: MessageFile.java,v 1.3 2005/04/27 09:25:40 valeks Exp $
 */
interface MessageFile {
  String getExtension();
  void writeFile(TplFile tplSource, OutputStream out) throws IOException;
  String toString();
}