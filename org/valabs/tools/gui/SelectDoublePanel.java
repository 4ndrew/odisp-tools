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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * Панель, предлагающая выбрать из множества объектов некое подмножество.
 * Состоит из двух списков ({@link javax.swing.JList}), в одном из которых 
 * находятся не выбранные элементы, а в другом - выбранные.
 * Перемешение элементов из одного списка в другой осуществляется 
 * с помощью кнопок "назад" и "вперёд". 
 * 
 * @author <a href="loki@novel-il.ru">Кирилл Лиходедов</a>
 * @author (C) 2006 НПП "Новел-ИЛ"
 * @version $Id$
 */
public class SelectDoublePanel extends JPanel {
  /** Не выбранные элементы */
  private DefaultListModel unselectedModel;
  /** Выбранные элементы */
  private DefaultListModel selectedModel;
  
//== Графические компоненты ==//  
  /** Список невыбранных элементов */
  private JList unselectedList;
  /** Список выбранных элементов */
  private JList selectedList;
  /** Кнопка добавления элемента из списка невыбранных в список выбранных */
  private JButton addButton;
  /** Кнопка удаления элемента из списка выбранных в список невыбранных */
  private JButton removeButton;
  
//== Константы ==//
  /** Предпочитаемое количество видимых строк в списке */
  private static final int DEFAULT_VISIBLE_ROW_COUNT = 10;
  /** Предпочитаемая ширина ячеек (в пикселяз) */
  private static final int DEFAULT_CELL_WIDTH = 100;
  
  /**
   * Создаёт и размещает все компоненты на панели. 
   * Списки изначально пусты.
   */
  public SelectDoublePanel() {
    unselectedModel = new DefaultListModel();
    selectedModel = new DefaultListModel();
    
    // Инициализация компонентов
    unselectedList = new JList(unselectedModel);
    selectedList = new JList(selectedModel);
    final JScrollPane unselectedPane = new JScrollPane(unselectedList);
    final JScrollPane selectedPane = new JScrollPane(selectedList);
    unselectedList.setVisibleRowCount(DEFAULT_VISIBLE_ROW_COUNT);
    selectedList.setVisibleRowCount(DEFAULT_VISIBLE_ROW_COUNT);
    unselectedList.setFixedCellWidth(DEFAULT_CELL_WIDTH);
    selectedList.setFixedCellWidth(DEFAULT_CELL_WIDTH);
    
    addButton = new JButton("->");
    removeButton = new JButton("<-");
    
    // Размещение компонентов
    setLayout(new GridBagLayout());
    final GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(3, 3, 3, 3);
    
    
    WindowUtilities.addObjectToContainer(this, new JPanel(), con, 1, 0);
    WindowUtilities.addObjectToContainer(this, addButton, con, 1, 1);
    WindowUtilities.addObjectToContainer(this, removeButton, con, 1, 3);
    WindowUtilities.addObjectToContainer(this, new JPanel(), con, 1, 4);
    
    WindowUtilities.addObjectToContainer(this, unselectedPane, con, 0, 0);
    // WindowUtilities.addObjectToContainer(this, buttonPanel, con, 1, 0);
    WindowUtilities.addObjectToContainer(this, selectedPane, con, 2, 0);
    
    // Назначения кнопкам слушателей
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Object[] objects = unselectedList.getSelectedValues();
        for (int i = 0; i < objects.length; i++) {
          selectedModel.addElement(objects[i]);
          unselectedModel.removeElement(objects[i]);
        } 
      }
    });
    
    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Object[] objects = selectedList.getSelectedValues();
        for (int i = 0; i < objects.length; i++) {
          unselectedModel.addElement(objects[i]);
          selectedModel.removeElement(objects[i]);
        } 
      }
    });
  }
  
  /**
   * Создаёт и размещает все компоненты, вызывая {@link SelectDoublePanel#SelectDoublePanel()},
   * а затем добавляет данные списки объектов в отображаемые на панели списки. 
   * @param unselected Список невыбранных элементов.
   * @param selected Список выбранных элементов.
   */
  public SelectDoublePanel(final List unselected, final List selected) {
    this();
    Iterator it;
    for (it = unselected.iterator(); it.hasNext(); ) {
      unselectedModel.addElement(it.next());
    }
    for (it = selected.iterator(); it.hasNext(); ) {
      selectedModel.addElement(it.next());
    }
  }
  
  /**
   * Возвращает элементы в списке невыбранных. 
   * @return Ссылка на элементы списка невыбранных.
   * @see #getSelectedElements()
   */
  public Enumeration getUnselectedElements() {
    return unselectedModel.elements();
  }
  
  /**
   * Возвращает элементы в списке выбранных. 
   * @return Ссылка на элементы списка выбранных.
   * @see #getSelectedElements()
   */
  public Enumeration getSelectedElements() {
    return selectedModel.elements();
  }
  
  /** 
   * Возвращает список невыбранных элементов. 
   * @return Список невыбранных элементов.
   */
  public JList getUnselectedList() {
    return unselectedList;
  }
  
  /**
   * 
   * @param items
   */
  public void setUnselectedList(List items) {
    unselectedModel.removeAllElements();
    for (Iterator it = items.iterator(); it.hasNext(); ) {
      unselectedModel.addElement(it.next());
    }
  }
  
  /** 
   * Возвращает список выбранных элементов. 
   * @return Список выбранных элементов.
   */
  public JList getSelectedList() {
    return selectedList;
  }
  
  /**
   * 
   * @param items
   */
  public void setSelectedList(List items) {
    selectedModel.removeAllElements();
    for (Iterator it = items.iterator(); it.hasNext(); ) {
      selectedModel.addElement(it.next());
    }
  }
  
  /**
   * Возвращает кнопку добавления элемента из списка невыбранных в список выбранных.
   * @return Кнопка добавления элемента из списка невыбранных в список выбранных.
   */
  public JButton getAddButton() {
    return addButton;
  }

  /**
   * Возвращает кнопку удаления элемента из списка выбранных в список невыбранных.
   * @return Кнопка удаления элемента из списка выбранных в список невыбранных.
   */
  public JButton getRemoveButton() {
    return removeButton;
  }

  public static final void main(String args[]) {
    String[] unst = {"lalala", "mememe", "bebebe", "hehehe"};
    String[] st = {"muu"};
    List un = Arrays.asList(unst);
    List s = Arrays.asList(st);
    
    SelectDoublePanel sdp = new SelectDoublePanel(un, s);
    JFrame f = new JFrame();
    f.getContentPane().add(sdp);
    f.pack();
    f.setVisible(true);
  }
  
}
