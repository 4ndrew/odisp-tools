package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author valeks
 * @version $Id: MessageFile.java,v 1.2 2005/02/03 12:40:26 valeks Exp $
 */
interface MessageFile {
  public abstract String getExtension();
  public abstract void writeFile(TplFile tplSource, OutputStream out) throws IOException;
  public abstract String toString();
}