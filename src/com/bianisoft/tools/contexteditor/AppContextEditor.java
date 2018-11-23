/* This file is part of the Bianisoft Context Editor Tool.
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *----------------------------------------------------------------------
 * Copyright (C) Alain Petit - alainpetit21@hotmail.com
 *
 * 18/12/10			0.1 First beta initial Version.
 * 12/09/11			0.1.1 Moved everything to a com.bianisoft
 *
 *-----------------------------------------------------------------------
 */
package com.bianisoft.tools.contexteditor;


//Standard Java imports
import java.awt.Frame;

//JOGL library imports
import javax.media.opengl.GLCanvas;

//Bianisoft imports
import com.bianisoft.engine.App;


public class AppContextEditor extends App{
	final public static int ID_CTX_EDITOR= 0x0;

	public AppContextEditor(GLCanvas p_canvas, Frame p_objFrame){
		super(p_canvas, p_objFrame, 640, 480);
		addContext(new CtxEditor());
		setCurContext(0);
	}
}
