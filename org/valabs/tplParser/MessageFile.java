package org.valabs.tplParser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author valeks
 * @version $Id: MessageFile.java,v 1.1 2005/02/02 20:22:28 valeks Exp $
 */
interface MessageFile {

  public abstract void writeFile(OutputStream out) throws IOException;
}