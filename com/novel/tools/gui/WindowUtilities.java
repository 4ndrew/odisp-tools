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
 * Этот класс содержит ряд вспомогательных методов для работы с окнами
 * приложения, в основном с JFrame'ом.
 * 
 * @author <a href="dron@novel-il.ru">Андрей А. Порохин</a>
 * @author (C) 2003-2006 НПП "Новел-ИЛ"
 * @version $Id$
 */
public final class WindowUtilities {

  /** Данный конструктор никогда не должен быть вызван, так как
   * WindowUtilities - класс, который не должен иметь экземпляров, потому как
   * все методы класса являются статическими.
   */
  private WindowUtilities() { /* не используемый конструктор */ }
  
  /**
   * Получение параметров графического девайса по-умолчанию (ведомого).
   * @return Графические параметры.
   * @since 1.17
   */
  public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    return gd.getDefaultConfiguration();
  }

  /**
   * Изменяет координаты, перемещая окно на центр экрана. Начиная с 1.17 добавлена
   * поддержка многомониторных конфигураций. 
   * @param frame cсылка на окно (JFrame), которое необходимо переместить
   * в центр экрана.
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
   * Получение границ текста.
   * 
   * @param g Графический контекст.
   * @param text Текст.
   * @return Границы текста.
   */
  public static Rectangle2D getTextBounds(Graphics g, String text) {
    FontMetrics fm = g.getFontMetrics();
    return fm.getStringBounds(text, g);
  }
  
  /**
   * Получение ширины текста.
   * 
   * @param g Графический контекст.
   * @param text Текст.
   * @return Ширина текста в пикселях.
   */
  public static int getTextWidth(Graphics g, String text) {
    Rectangle2D r = getTextBounds(g, text);
    return (int) (r.getWidth() - r.getY());
  }
}
