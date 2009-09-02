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
package org.valabs.tools.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is util class to help with Window locationing and filling.
 * 
 * @author (C) 2003-2009 <a href="mailto:andrew.porokhin@gmail.com">Andrew Porokhin</a>
 * @version 1.19
 */
public final class WindowUtilities {
  /** Logging capabilities */
  private final static Logger log = Logger.getLogger(WindowUtilities.class.getName());

  /** 
   * Util class, there is no constructor available.
   */
  private WindowUtilities() { /* Creation of this object is prohibited. */ }
  
  /**
   * Returns default graphics device configuration.
   * @return Graphics configuration for default device.
   * @since 1.17
   */
  public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    return gd.getDefaultConfiguration();
  }

  /**
   * Center window relative to default graphics device. Since 1.17 added support
   * for multi-monitor configuration. 
   * 
   * @param frame Window that should be positioned.
   * @since 1.1
   */
  public static void centerWindow(Window frame) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    
    log.log(Level.INFO, "GS: {0}", gs);
    log.log(Level.INFO, "Graphics devices: {0}", new Integer(gs.length));
    
    // TODO: As I remember that was a bug -- on one-monitor configuration
    // GraphicsEnvironment.getDefaultScreenDevice works incorrectly. We should
    // check this on all supported platforms.
    if (gs.length > 1) {
      Rectangle screenSize = getDefaultGraphicsConfiguration().getBounds();
      frame.setLocation(
        screenSize.width / 2 - frame.getWidth() / 2,
        screenSize.height / 2 - frame.getHeight() / 2);
    } else {
      frame.setLocationRelativeTo(null);
    }
  }
  
  /**
   * Center window relative to specified graphics device.
   * 
   * @param frame 
   * @param device
   * @since 1.19
   */
  public static void centerWindow(Window frame, GraphicsDevice device) {
    Rectangle screenSize = device.getDefaultConfiguration().getBounds();
    frame.setLocation(
        screenSize.width / 2 - frame.getWidth() / 2,
        screenSize.height / 2 - frame.getHeight() / 2);
  }
  
  /**
   * Set window size relative to screen size.
   * 
   * TODO: check this on dual-monitor configuration.
   * 
   * @param frame
   * @param widthRatio
   * @param heightRatio
   * @since 1.19
   */
  public static void setWindowRelativeSize(Window frame, float widthRatio, float heightRatio) {
    Rectangle screenSize = getDefaultGraphicsConfiguration().getBounds();
    Dimension newSize = new Dimension(
      Math.round(screenSize.width * widthRatio),
      Math.round(screenSize.height * heightRatio));
    log.log(Level.FINEST, "Setting up size to {0}", newSize);
    frame.setSize(newSize);
  }
  
  /**
   * Set window size relative to screen size. Ignore widthRatio if width/height greater
   * than maxWHRatio. 
   * 
   * @param frame Target window.
   * @param widthRatio Width ratio.
   * @param heightRatio Height ratio.
   * @param maxWHRatio Maximum width/height ratio.
   */
  public static void setWindowRelativeSize(Window frame, float widthRatio, float heightRatio, float maxWHRatio) {
    Rectangle screenSize = getDefaultGraphicsConfiguration().getBounds();
    int newWidth = Math.round(screenSize.width * widthRatio);
    int newHeight = Math.round(screenSize.height * heightRatio);
    
    if ((newWidth / newHeight) > maxWHRatio) {
      newWidth = (int) (newHeight * maxWHRatio);
    }
    
    Dimension newSize = new Dimension(
      newWidth,
      newHeight);
    log.log(Level.FINEST, "Setting up size to {0}", newSize);
    frame.setSize(newSize);
  }
   
  /**
   * Добавляет компонент в контейнер (обёртка для GridBagLayout) при этом
   * указываются опции GridBagLayout (gridx, gridy, gridwidth).
   *
   * @param container Ссылка на контейнер.
   * @param component Компонент, котрый вставляется.
   * @param c Параметры GridBagLayout.
   * @param gridx X-составляющая.
   * @param gridy Y-составляющая.
   * @param gridwidth Занимаемое место
   */
  public static void addObjectToContainer(Container container,
          Component component, GridBagConstraints c, int gridx, int gridy,
          int gridwidth) {
    c.gridwidth = gridwidth;
    c.gridx = gridx;
    c.gridy = gridy;
    container.add(component, c);
  }
   
  /**
   * Добавляет компонент в контейнер (обёртка для GridBagLayout) при этом указываются
   * опции GridBagLayout (gridx, gridy, gridwidth = 1).
   * 
   * @param container Ссылка на контейнер.
   * @param component Компонент, котрый вставляется.
   * @param c Параметры GridBagLayout.
   * @param gridx X-составляющая.
   * @param gridy Y-составляющая.
   */
  public static void addObjectToContainer(Container container,
          Component component, GridBagConstraints c, int gridx, int gridy) {
    addObjectToContainer(container, component, c, gridx, gridy, 1);
  }
  
  /**
   * @param container Ссылка на контейнер.
   * @param component Компонент, котрый вставляется.
   * @param c Параметры GridBagLayout.
   * @param gridwidth Ширина.
   * @param gridheight Длинна.
   */
  public static void addObjectToContainerRelative(Container container,
          Container component, GridBagConstraints c, int gridwidth,
          int gridheight) {
    c.gridwidth = gridwidth;
    c.gridheight = gridheight;
    container.add(component, c);
  }

  /**
   * Returns text bounds for specified graphics context.
   * 
   * @param g Graphics context.
   * @param text Text to be drawn.
   * @return Bounds of the text.
   */
  public static Rectangle2D getTextBounds(Graphics g, String text) {
    FontMetrics fm = g.getFontMetrics();
    return fm.getStringBounds(text, g);
  }
  
  /**
   * Returns width of the text.
   * 
   * @param g Graphics context.
   * @param text Text to be drawn.
   * @return Width of the text in the pixels.
   */
  public static int getTextWidth(Graphics g, String text) {
    Rectangle2D r = getTextBounds(g, text);
    return (int) (r.getWidth() - r.getY());
  }
}
