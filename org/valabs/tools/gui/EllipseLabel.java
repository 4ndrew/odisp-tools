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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextField;

/**
 * JLabel, который имеет фиксированную ширину и отображает переданный
 * path файла с использованием эллипса, т.е. таким образом, чтобы 
 * последняя папка в адресе отображается полностью, а предыдущие
 * заменяются многоточиями, если JLabel не помещается полностью.
 * @author <a href="loki@novel-il.ru">Кирилл Лиходедов</a>
 * @author (C) 2003-2004 НПП "Новел-ИЛ"
 * @version $Id: EllipseLabel.java,v 1.4 2006/07/28 09:44:36 loki Exp $
 */
public class EllipseLabel extends JTextField {
  private String realText;
  private int width; //in pixels
  
  /**
   * Создаёт новый EllipseLabel с фиксированной шириной. 
   * @param _width Ширина лабля в пикселях.
   */
  public EllipseLabel(int _width) {
    width = _width;
    setEditable(false);
    setWidth();
  }
  
  /**
   * Устанавливает текст в лаблю, учитывая максимальную ширину.<br>
   * Если текст (путь к файлу) не помещается в лабле, то производится
   * замена части пути на "...". Последний компонент пути, однако,
   * остаётся неизменным. 
   * @param text Путь к файлу.
   */
  public void setText(String text) {
    realText = text;
    int w = getWidth(text);
    if (w <= width) {
      super.setText(text);
      return;
    }
    
    final String folders[] = text.split("\\" + File.separator);
    final List fList = new LinkedList(Arrays.asList(folders));
    
    if (fList.size() >= 2) {
      fList.set(fList.size()-2, "...");
    }
    
    String s = text;
    while(w > width && fList.size() > 3){
      fList.remove(fList.size()-3);
      s = getString(fList);
      w = getWidth(s);
    }
    
    super.setText(s);
    setWidth();
  }
  
  /**
   * Возвращает "настоящий" текст этого лабеля (т.е. тот, который отображался 
   * бы на лабле, если бы место позволяло). 
   * @return Настоящий текст лабеля.
   */
  public String getRealText(){
    return realText;
  }
  
  /**
   * Составляет строку из листа: пишет все элементы подряд, разделяя
   * их символами File.separator. 
   * @param list Список строк.
   * @return Строка из элементов списка, разделённых файл-сепараторами.
   */
  private static String getString(List list) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < list.size(); i++) {
      sb.append(list.get(i) + File.separator);
    }
    return sb.toString();
  }
  
  /**
   * Вызывает setPreferredSize(), setMinimumSize(), setMaximumSize()
   * для высоты getPreferredSize().heigth и ширины, заданной полем width.
   */
  private void setWidth(){
    int h = getPreferredSize().height;
    Dimension d = new Dimension(width, h);
    
    setPreferredSize(d);
    setMinimumSize(d);
    setMaximumSize(d);
  }
  
  /**
   * Вычисляет ширину данной строки. 
   * @param s Строка.
   * @return Ширина строки (в пикселях).
   */
  private int getWidth(String s){
    Font f = getFont();
    if (f == null) { return 0; }
    
    FontMetrics fm = getFontMetrics(f);
    Rectangle2D rect = fm.getStringBounds(s, getGraphics());
    return (int)rect.getWidth();
  }
  
}
