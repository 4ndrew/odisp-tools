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
package org.valabs.tools;

/** п≤я┌п╣п╪. =))))
 * 
 * @author <a href="mailto:andrew.porokhin@gmail.com">Andrew A. Porokhin</a>
 * @author <a href="mailto:valeks@valabs.spb.ru">Valentin A. Alekseev</a>
 * @version $Id: ObjectItem.java,v 1.2 2004/11/01 11:04:19 dron Exp $
 */
public class ObjectItem {
  /** п║п©п╦я│п╬п╨ п╥п╟п╡п╦я│п╣п╪п╬я│я┌п╣п╧ */
  public String depends[];
  /** п║п©п╦я│п╬п╨ п©я─п╣п╢п╬я│я┌п╟п╡п╩я▐п╣п╪я▀я┘ я│п╣я─п╡п╦я│п╬п╡ */
  public String providing[];
  /** пёп╢п╬п╩п╣я┌п╡п╬я─п╣п╫я▀ п╩п╦ п╡я│п╣ п╥п╟п╡п╦я│п╣п╪п╬я│я┌п╦? */
  boolean correct;
}
