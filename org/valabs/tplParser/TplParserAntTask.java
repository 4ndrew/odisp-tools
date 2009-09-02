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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;

/**
 * Tpl parser ant task.
 * 
 * Parameters:
 * src (required)
 * dest (optional, it omitted equals to src)
 * 
 * 
 * @author (C) 2009 <a href="mailto:andrew.porokhin@gmail.com">Andrew Porokhin</a>  
 */
public class TplParserAntTask extends MatchingTask {
  private File srcDir;
  private File destDir;
  private int countProcessed;
  private int countSkipped;
  private int countError;
  private Boolean cleanFiles = new Boolean(false);
  
  public void execute() throws BuildException {
    log("Starting tpl parser");
    DirectoryScanner scanner = null;
    
    // Require srcDir
    if (srcDir == null) {
      throw new BuildException("The src attribute must be set.");
    }
    
    // If dest dir is not set use the same as srcDir.
    if (destDir == null) {
      destDir = srcDir;
    }
    
    log("Scanning: " + srcDir);
    scanner = getDirectoryScanner(srcDir);
    // scanner.
    
    String[] files = scanner.getIncludedFiles();
    //SourceFileScanner sfs = new SourceFileScanner(this);
    //files = sfs.restrict(files, srcDir, destDir, m);
    if (files.length != 0) {
      for (int i = 0; i < files.length; i++) {
        File fHandle = new File(srcDir + File.separator + files[i]);
        if (fHandle.exists()) {
          try {
            convert(new TplFile(fHandle), cleanFiles.booleanValue());
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          log("Error: " + files[i] + " not exists");
        }
      }
    }
    
    log(toString());
  }
  
  private void convert(final TplFile source, boolean clean) throws IOException {
    final MessageFile writer = new MessageFile_Java();
    final String extension = "(" + writer.getExtension() + ")";
    final String outputFileName = getDestinationFileName(source.getFileName(), writer.getExtension());
    if (outputFileName.equals(source.getFileName())) {
      throw new BuildException("Src == Dest");
    }
    final File javaFile = new File(outputFileName);
    if (clean) {
      javaFile.delete();
      
      log("File deleted: " + javaFile.getPath());
      if (source.getErrorMessage() != null) {
        new File(getDestinationFileName(source.getErrorMessage().getFileName(), writer.getExtension())).delete();
        log("File deleted: " + getDestinationFileName(source.getErrorMessage().getFileName(), writer.getExtension()));
      }
      if (source.getReplyMessage() != null) {
        new File(getDestinationFileName(source.getReplyMessage().getFileName(), writer.getExtension())).delete();
        log("File deleted: " + getDestinationFileName(source.getReplyMessage().getFileName(), writer.getExtension()));
      }
      if (source.getNotifyMessage() != null) {
        new File(getDestinationFileName(source.getNotifyMessage().getFileName(), writer.getExtension())).delete();
        log("File deleted: " + getDestinationFileName(source.getNotifyMessage().getFileName(), writer.getExtension()));
      }
      countProcessed++;
    } else if (javaFile.lastModified() <= source.lastModified()) {
      countProcessed++;
      StringBuffer l = new StringBuffer("TPL parsed: ").append(source.getFileName()).append(", main").append(extension);
      writer.writeFile(source, new FileOutputStream(outputFileName));
      if (source.getReplyMessage() != null) {
        l.append(", reply").append(extension);
        writer.writeFile(source.getReplyMessage(), new FileOutputStream(getDestinationFileName(source.getReplyMessage().getFileName(), writer.getExtension())));
      }
      if (source.getErrorMessage() != null) {
        l.append(", error").append(extension);
        writer.writeFile(source.getErrorMessage(), new FileOutputStream(getDestinationFileName(source.getErrorMessage().getFileName(), writer.getExtension())));
      }
      if (source.getNotifyMessage() != null) {
        l.append(", notify").append(extension);
        writer.writeFile(source.getNotifyMessage(), new FileOutputStream(getDestinationFileName(source.getNotifyMessage().getFileName(), writer.getExtension())));
      }
      log(l.toString());
    } else {
      countSkipped++;
    }
  }
  
  /**
   * @return статистика работы парсера
   */
  public String toString() {
    StringBuffer result = new StringBuffer(200);
    
    result.append(countProcessed + countSkipped + countError).append(" parsed, ")
      .append(countProcessed);
    
    if (cleanFiles.booleanValue()) {
      result.append(" deleted, ")
        .append(countError)
        .append(" errors.");
    } else {
      result.append(" generated, ")
        .append(countSkipped).append(" skipped due up-to-date, ")
        .append(countError).append(" errors."); 
    }
    return result.toString();
  }
  
  private String getDestinationFileName(String sourceFileName, String destinationExtension) {
    final int indexOfExtension = sourceFileName.lastIndexOf(".");
    if (indexOfExtension >= 0) {
      return sourceFileName.substring(0, indexOfExtension) + destinationExtension;
    }
    
    return sourceFileName.toString() + destinationExtension;
  }
  
  public void setSrc(File src) {
    this.srcDir = src;
  }
  
  public void setDest(File dest) {
    this.destDir = dest;
  }
  
  public void setClean(Boolean cleanFiles) {
    this.cleanFiles = cleanFiles;
  }
}
