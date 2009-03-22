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
package com.novel.tools.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.geom.Rectangle2D;


/**
 * ���� ����� �������� ��� ��������������� ������� ��� ������ � ������
 * ����������, � �������� � JFrame'��.
 * 
 * @author <a href="dron@novel-il.ru">������ �. �������</a>
 * @author (C) 2003-2006 ��� "�����-��"
 * @version $Id$
 */
public final class WindowUtilities {

  /** ������ ����������� ������� �� ������ ���� ������, ��� ���
   * WindowUtilities - �����, ������� �� ������ ����� �����������, ������ ���
   * ��� ������ ������ �������� ������������.
   */
  private WindowUtilities() { /* �� ������������ ����������� */ }
  
  /**
   * ��������� ���������� ������������ ������� ��-��������� (��������).
   * @return ����������� ���������.
   * @since 1.17
   */
  public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    return gd.getDefaultConfiguration();
  }

  /**
   * �������� ����������, ��������� ���� �� ����� ������. ������� � 1.17 ���������
   * ��������� ��������������� ������������. 
   * @param frame c����� �� ���� (JFrame), ������� ���������� �����������
   * � ����� ������.
   * @since v1.1
   */
  public static void centerWindow(Window frame) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
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
   * ��������� ��������� � ��������� (�£���� ��� GridBagLayout) ��� ����
   * ����������� ����� GridBagLayout (gridx, gridy, gridwidth).
   *
   * @param container ������ �� ���������.
   * @param component ���������, ������ �����������.
   * @param c ��������� GridBagLayout.
   * @param gridx X-������������.
   * @param gridy Y-������������.
   * @param gridwidth ���������� �����
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
   * ��������� ��������� � ��������� (�£���� ��� GridBagLayout) ��� ���� �����������
   * ����� GridBagLayout (gridx, gridy, gridwidth = 1).
   * 
   * @param container ������ �� ���������.
   * @param component ���������, ������ �����������.
   * @param c ��������� GridBagLayout.
   * @param gridx X-������������.
   * @param gridy Y-������������.
   */
  public static void addObjectToContainer(Container container,
          Component component, GridBagConstraints c, int gridx, int gridy) {
    addObjectToContainer(container, component, c, gridx, gridy, 1);
  }
  
  /**
   * @param container ������ �� ���������.
   * @param component ���������, ������ �����������.
   * @param c ��������� GridBagLayout.
   * @param gridwidth ������.
   * @param gridheight ������.
   */
  public static void addObjectToContainerRelative(Container container,
          Container component, GridBagConstraints c, int gridwidth,
          int gridheight) {
    c.gridwidth = gridwidth;
    c.gridheight = gridheight;
    container.add(component, c);
  }

  /**
   * ��������� ������ ������.
   * 
   * @param g ����������� ��������.
   * @param text �����.
   * @return ������� ������.
   */
  public static Rectangle2D getTextBounds(Graphics g, String text) {
    FontMetrics fm = g.getFontMetrics();
    return fm.getStringBounds(text, g);
  }
  
  /**
   * ��������� ������ ������.
   * 
   * @param g ����������� ��������.
   * @param text �����.
   * @return ������ ������ � ��������.
   */
  public static int getTextWidth(Graphics g, String text) {
    Rectangle2D r = getTextBounds(g, text);
    return (int) (r.getWidth() - r.getY());
  }
}
