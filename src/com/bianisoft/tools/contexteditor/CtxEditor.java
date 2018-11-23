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
import java.net.URL;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//JOGL library imports
import com.sun.opengl.util.Screenshot;
import com.sun.opengl.util.texture.TextureIO;

//Bianisoft imports
import com.bianisoft.engine.App;
import com.bianisoft.engine.Obj;
import com.bianisoft.engine.backgrounds.Background;
import com.bianisoft.engine.Button;
import com.bianisoft.engine.Camera;
import com.bianisoft.engine.Context;
import com.bianisoft.engine.Drawable;
import com.bianisoft.engine.labels.Label;
import com.bianisoft.engine.PhysObj;
import com.bianisoft.engine.sprites.Sprite;
import com.bianisoft.engine.sprites.Sprite.State;
import com.bianisoft.engine.sprites.Sprite.Frame;


public class CtxEditor extends Context{
	public double	m_zoomFactor= 1.0;
	public double	m_posX= 0.0;
	public double	m_posY= 0.0;
	public File		m_pendingScreenshotFile= null;

	PhysObj	m_physObjSelected= null;
	private int m_nBasePosX;
	private int m_nBasePosY;
	private String m_curDirectory;


	//Extensive info to save

	public CtxEditor(){
		super();

		m_curDirectory= (new File (".")).getAbsolutePath();
		int nIndexCodeFolder= m_curDirectory.indexOf("/Code");
		if(nIndexCodeFolder == -1){
			nIndexCodeFolder= m_curDirectory.indexOf("\\Code");
			if(nIndexCodeFolder == -1){
				System.out.print("Run This application from a Code Subfolder");
				App.exit();
			}
		}

		m_curDirectory= m_curDirectory.substring(0, nIndexCodeFolder);
	}

	public void checkForLoadingPending(){
		ArrayList<PhysObj> vecChilds= getVectorChilds();

		for(Obj obj : vecChilds){
			if(obj.isKindOf(IDCLASS_Background)){
				Background backObj= (Background)obj;

				if(backObj.m_image != null)
					continue;

				if(backObj.m_stResImage.charAt(0) == '/')
					backObj.m_stResImage= "file:" + backObj.m_stResImage;
				else if(!backObj.m_stResImage.startsWith("file:/"))
					backObj.m_stResImage= "file:/" + backObj.m_stResImage;

				try{
					backObj.m_image = TextureIO.newTexture(new URL(backObj.m_stResImage), false, null);
					backObj.m_nWidth = backObj.m_image.getImageWidth();
					backObj.m_nHeight = backObj.m_image.getImageHeight();
				}catch(Exception ex){
					System.out.printf("Error while loading: %s", backObj.m_stResImage);
					System.exit(1);
				}
			}else if(obj.isKindOf(IDCLASS_Sprite)){
				Sprite spr= (Sprite)obj;

				if(spr.m_image != null)
					continue;

				if(spr.m_stResImage.charAt(0) == '/')
					spr.m_stResImage= "file:" + spr.m_stResImage;
				else
					spr.m_stResImage= "file:/" + spr.m_stResImage;

				try{
					spr.m_image = TextureIO.newTexture(new URL(spr.m_stResImage), false, null);
				}catch(Exception ex){
					System.out.printf("Error while loading:%s", spr.m_stResImage);
					System.exit(1);
				}

				spr.m_nWidthImage	= spr.m_image.getImageWidth();
				spr.m_nHeightImage	= spr.m_image.getImageHeight();

				//Do calculation
				//Find Highest Frame Count within all states
				int nHighestFrameCount= 0;
				for(State stateCur : spr.m_vecStates){
					if(stateCur.m_nMaxFrames > nHighestFrameCount){
						nHighestFrameCount= stateCur.m_nMaxFrames;
					}
				}

				//Using Widht of image, and highest mNbFrame find each frame width
				spr.m_nWidthFrame= spr.m_nWidthImage / nHighestFrameCount;
				spr.m_nHeightFrame= spr.m_nHeightImage / spr.m_vecStates.size();

				//Set All rectangles of all frames of all states
				for(int i= 0; i < spr.m_vecStates.size(); ++i){
					State stateCur= spr.m_vecStates.get(i);
					for(int j= 0; j < stateCur.m_nMaxFrames; ++j){
						Frame frame= spr.new Frame((int)(j*spr.m_nWidthFrame), (int)(i*spr.m_nHeightFrame), (int)spr.m_nWidthFrame, (int)spr.m_nHeightFrame);
						stateCur.m_vecFrames.add(frame);
					}
				}
			}else if(obj.isKindOf(IDCLASS_Label)){
				Label lbl= (Label)obj;

				if(lbl.m_font != null)
					continue;

				try{
					File fileFont= new File(lbl.m_stFontName);

					Font tempFont	= Font.createFont(Font.TRUETYPE_FONT, fileFont);
					lbl.m_font		= tempFont.deriveFont((float)lbl.m_nFontSize);
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	public void onSaveBackgroundJava(int p_nIdx, Background p_back, PrintWriter out) throws IOException{
		String stResName= p_back.m_stResImage.substring(p_back.m_stResImage.indexOf("/res"));
		stResName= stResName.substring(4);

		out.print("\t\t/*DATA_BACKGROUND_TEMPLATE:#|CLASS_ID|NAME|RESSOURCE_NAME|POS_X|POS_Y|POS_Z|DEEPNESS*/\n");
		out.print("\t\t/*DATA:"+p_nIdx+"|Background|"+p_back.getTextID()+"|"+stResName+"|"+(int)p_back.m_vPos[0]+"|"+(int)p_back.m_vPos[1]+"|"+(int)p_back.m_vPos[2]+"|"+(int)p_back.m_nDeepnessLevel+"|*/\n");

		out.print("\t\tBackground back"+p_back.getTextID()+"= new Background(\""+stResName+"\");\n");
		out.print("\t\tback"+p_back.getTextID()+".setTextID(\""+p_back.getTextID()+"\");\n");
		out.print("\t\tback"+p_back.getTextID()+".setPos(" +(int)p_back.getPosX()+ ", " +(int)p_back.getPosY()+ ", " +(int)p_back.getPosZ()+");\n");
		out.print("\t\tback"+p_back.getTextID()+".load();\n");

		if(p_back.m_nDeepnessLevel == 0)	//Infinity
			out.print("\t\tp_ctxUnder.addChild(back"+p_back.getTextID()+", true, true);\n");
		else if(p_back.m_nDeepnessLevel == 1)	//World
			out.print("\t\tp_ctxUnder.addChild(back"+p_back.getTextID()+", false, false);\n");
		else if(p_back.m_nDeepnessLevel == 2)	//Camera
			out.print("\t\tp_ctxUnder.addChild(back"+p_back.getTextID()+", true, false);\n");

		out.print("\n");
	}

	public void onLoadBackgroundJava(String p_stPathProject, String p_stData) throws IOException{
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Background
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For idText
		String stIdText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stFilename
		String stFilename= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosX
		String stPosX= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosY
		String stPosY= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosZ
		String stPosZ= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Deepness Level
		String stDeepness= p_stData.substring(0, p_stData.indexOf("|"));

		Background p_objBack= Background.create(Background.TYPE_NORMAL, p_stPathProject+"/res"+stFilename, null);
		p_objBack.setTextID(stIdText);
		p_objBack.setPosX(Integer.parseInt(stPosX));
		p_objBack.setPosY(Integer.parseInt(stPosY));
		p_objBack.setPosZ(Integer.parseInt(stPosZ));
		p_objBack.m_nDeepnessLevel= Integer.parseInt(stDeepness);

		addChild(p_objBack);
	}

	public void onSaveLabelJava(int p_nIdx, Label p_lbl, PrintWriter out) throws IOException{
		String stFontName= p_lbl.m_stFontName.substring(p_lbl.m_stFontName.indexOf("/res"));
		stFontName= stFontName.substring(4);

		//Format Multiline text
		String stText= p_lbl.m_stText.replace("\r\n", "\\n");
		stText= stText.replace("\n\r", "\\n");
		stText= stText.replace("\n", "\\n");
		stText= stText.replace("\"", "\\\"");

		out.print("\t\t/*DATA_LABEL_TEMPLATE:#|CLASS_ID|NAME|RESSOURCE_NAME|POS_X|POS_Y|POS_Z|DEEPNESS|FONT_SIZE|TEXT|ALIGNMENT|MULTILINE|REC_LEFT|REC_TOP|REC_WIDTH|RECT_HEIGHT*/\n");
		out.print("\t\t/*DATA:"+p_nIdx+"|Label|"+p_lbl.getTextID()+"|"+stFontName+"|"+(int)p_lbl.m_vPos[0]+"|"+(int)p_lbl.m_vPos[1]+"|"+(int)p_lbl.m_vPos[2]+"|"+(int)p_lbl.m_nDeepnessLevel+"|"+p_lbl.m_nFontSize+"|"+stText+"|"+p_lbl.m_nMode+"|"+p_lbl.m_isMultiline+"|"+p_lbl.m_recLimit.x+"|"+p_lbl.m_recLimit.y+"|"+p_lbl.m_recLimit.width+"|"+p_lbl.m_recLimit.height+"|*/\n");
		out.print("\t\tLabel lbl"+p_lbl.getTextID()+"= new Label(\""+stFontName+"\", "+p_lbl.m_nFontSize+", \""+stText+"\", "+p_lbl.m_nMode+", "+p_lbl.m_isMultiline+", new Rectangle("+p_lbl.m_recLimit.x+", "+p_lbl.m_recLimit.y+", "+p_lbl.m_recLimit.width+", "+p_lbl.m_recLimit.height+"));\n");
		out.print("\t\tlbl"+p_lbl.getTextID()+".setTextID(\""+p_lbl.getTextID()+"\");\n");
		out.print("\t\tlbl"+p_lbl.getTextID()+".setPos(" +(int)p_lbl.getPosX()+ ", " +(int)p_lbl.getPosY()+ ", " +(int)p_lbl.getPosZ()+");\n");
		out.print("\t\tlbl"+p_lbl.getTextID()+".load();\n");

		if(p_lbl.m_nDeepnessLevel == 0)	//Infinity
			out.print("\t\tp_ctxUnder.addChild(lbl"+p_lbl.getTextID()+", true, true);\n");
		else if(p_lbl.m_nDeepnessLevel == 1)	//World
			out.print("\t\tp_ctxUnder.addChild(lbl"+p_lbl.getTextID()+", false, false);\n");
		else if(p_lbl.m_nDeepnessLevel == 2)	//Camera
			out.print("\t\tp_ctxUnder.addChild(lbl"+p_lbl.getTextID()+", true, false);\n");

		out.print("\n");
	}

	public void onSaveLabelTextFieldJava(int p_nIdx, Label p_lbl, PrintWriter out) throws IOException{
		String stFontName= p_lbl.m_stFontName.substring(p_lbl.m_stFontName.indexOf("/res"));
		stFontName= stFontName.substring(4);

		//Format Multiline text
		String stText= p_lbl.m_stText.replace("\r\n", "\\n");
		stText= stText.replace("\n\r", "\\n");
		stText= stText.replace("\n", "\\n");
		stText= stText.replace("\"", "\\\"");

		out.print("\t\t/*DATA_LABEL_TEMPLATE:#|CLASS_ID|NAME|RESSOURCE_NAME|POS_X|POS_Y|POS_Z|DEEPNESS|FONT_SIZE|TEXT|ALIGNMENT|MULTILINE|REC_LEFT|REC_TOP|REC_WIDTH|RECT_HEIGHT*/\n");
		out.print("\t\t/*DATA:"+p_nIdx+"|LabelTextField|"+p_lbl.getTextID()+"|"+stFontName+"|"+(int)p_lbl.m_vPos[0]+"|"+(int)p_lbl.m_vPos[1]+"|"+(int)p_lbl.m_vPos[2]+"|"+(int)p_lbl.m_nDeepnessLevel+"|"+p_lbl.m_nFontSize+"|"+stText+"|"+p_lbl.m_nMode+"|"+p_lbl.m_isMultiline+"|"+p_lbl.m_recLimit.x+"|"+p_lbl.m_recLimit.y+"|"+p_lbl.m_recLimit.width+"|"+p_lbl.m_recLimit.height+"|*/\n");
		out.print("\t\tLabel lbl"+p_lbl.getTextID()+"= new LabelTextField(\""+stFontName+"\", "+p_lbl.m_nFontSize+", \""+stText+"\", "+p_lbl.m_nMode+", "+p_lbl.m_isMultiline+", new Rectangle("+p_lbl.m_recLimit.x+", "+p_lbl.m_recLimit.y+", "+p_lbl.m_recLimit.width+", "+p_lbl.m_recLimit.height+"));\n");
		out.print("\t\tlbl"+p_lbl.getTextID()+".setTextID(\""+p_lbl.getTextID()+"\");\n");
		out.print("\t\tlbl"+p_lbl.getTextID()+".setPos(" +(int)p_lbl.getPosX()+ ", " +(int)p_lbl.getPosY()+ ", " +(int)p_lbl.getPosZ()+");\n");
		out.print("\t\tlbl"+p_lbl.getTextID()+".load();\n");

		if(p_lbl.m_nDeepnessLevel == 0)	//Infinity
			out.print("\t\tp_ctxUnder.addChild(lbl"+p_lbl.getTextID()+", true, true);\n");
		else if(p_lbl.m_nDeepnessLevel == 1)	//World
			out.print("\t\tp_ctxUnder.addChild(lbl"+p_lbl.getTextID()+", false, false);\n");
		else if(p_lbl.m_nDeepnessLevel == 2)	//Camera
			out.print("\t\tp_ctxUnder.addChild(lbl"+p_lbl.getTextID()+", true, false);\n");

		out.print("\n");
	}

	public void onLoadLabelJava(String p_stPathProject, String p_stData) throws IOException{
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Label
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For idText
		String stIdText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stFontname
		String stFontname= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosX
		String stPosX= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosY
		String stPosY= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosZ
		String stPosZ= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Deepness Level
		String stDeepness= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For nFontSize
		String stFontSize= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For m_stText
		String stText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For m_nMode
		String stMode= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For m_isMultine
		String stMultiline= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecLeft
		String stRecLeft= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecTop
		String stRecTop= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecWidth
		String stRecWidth= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecHeight
		String stRecHeight= p_stData.substring(0, p_stData.indexOf("|"));

		Label p_objLbl= Label.create(Label.TYPE_NORMAL, p_stPathProject+"/res"+stFontname, Integer.parseInt(stFontSize), stText, Integer.parseInt(stMode), Boolean.parseBoolean(stMultiline), new Rectangle(Integer.parseInt(stRecLeft), Integer.parseInt(stRecTop), Integer.parseInt(stRecWidth), Integer.parseInt(stRecHeight)));
		p_objLbl.setTextID(stIdText);
		p_objLbl.setPosX(Integer.parseInt(stPosX));
		p_objLbl.setPosY(Integer.parseInt(stPosY));
		p_objLbl.setPosZ(Integer.parseInt(stPosZ));
		p_objLbl.m_nDeepnessLevel= Integer.parseInt(stDeepness);

		addChild(p_objLbl);
	}

	public void onLoadLabelTextFieldJava(String p_stPathProject, String p_stData) throws IOException{
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Label
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For idText
		String stIdText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stFontname
		String stFontname= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosX
		String stPosX= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosY
		String stPosY= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosZ
		String stPosZ= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Deepness Level
		String stDeepness= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For nFontSize
		String stFontSize= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For m_stText
		String stText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For m_nMode
		String stMode= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For m_isMultine
		String stMultiline= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecLeft
		String stRecLeft= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecTop
		String stRecTop= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecWidth
		String stRecWidth= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For RecHeight
		String stRecHeight= p_stData.substring(0, p_stData.indexOf("|"));

		Label p_objLbl= Label.create(Label.TYPE_TEXTFIELD, p_stPathProject+"/res"+stFontname, Integer.parseInt(stFontSize), stText, Integer.parseInt(stMode), Boolean.parseBoolean(stMultiline), new Rectangle(Integer.parseInt(stRecLeft), Integer.parseInt(stRecTop), Integer.parseInt(stRecWidth), Integer.parseInt(stRecHeight)));
		p_objLbl.setTextID(stIdText);
		p_objLbl.setPosX(Integer.parseInt(stPosX));
		p_objLbl.setPosY(Integer.parseInt(stPosY));
		p_objLbl.setPosZ(Integer.parseInt(stPosZ));
		p_objLbl.m_nDeepnessLevel= Integer.parseInt(stDeepness);

		addChild(p_objLbl);
	}

	public void onSaveCustomSpriteJava(int p_nIdx, Sprite p_spr, PrintWriter out) throws IOException{
		String stResName= p_spr.m_stResImage.substring(p_spr.m_stResImage.indexOf("/res"));
		String stClassID= p_spr.getTextClassID();
		stResName= stResName.substring(4);

		out.print("\t\t/*DATA_CUSTOM_SPRITE_TEMPLATE:#|CLASS_ID|NAME|RESSOURCE_NAME|POS_X|POS_Y|POS_Z|DEEPNESS|DEFAULT_STATE|DEFAULT_FRAME|NB_STATES|STATE_NAME|STATE_NB_FRAMES|STATE_SPEED*/\n");
		out.print("\t\t/*DATA:"+p_nIdx+"|"+stClassID+"|"+p_spr.getTextID()+"|"+stResName+"|"+(int)p_spr.m_vPos[0]+"|"+(int)p_spr.m_vPos[1]+"|"+(int)p_spr.m_vPos[2]+"|"+(int)p_spr.m_nDeepnessLevel+"|"+p_spr.getCurState()+"|"+p_spr.getCurFrame()+"|"+p_spr.m_vecStates.size()+"|");
			for(State stateCur : p_spr.m_vecStates){
				out.print(stateCur.m_stName+"|");
				out.print(stateCur.m_nMaxFrames+"|");
				out.print((stateCur.m_nSpeed / 32.0)+"|");
			}
		out.print("*/\n");

		out.print("\t\t"+stClassID+" spr"+p_spr.getTextID()+"= new "+stClassID+"(\""+stResName+"\");\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setTextID(\""+p_spr.getTextID()+"\");\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setPos(" +(int)p_spr.getPosX()+ ", " +(int)p_spr.getPosY()+ ", " +(int)p_spr.getPosZ()+");\n");

			for(State stateCur : p_spr.m_vecStates)
				out.print("\t\tspr"+p_spr.getTextID()+".addState(spr"+p_spr.getTextID()+".new State(\""+stateCur.m_stName+"\", "+stateCur.m_nMaxFrames+", "+(stateCur.m_nSpeed / 32.0)+"f));\n");

		out.print("\t\tspr"+p_spr.getTextID()+".load();\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setCurState("+p_spr.getCurState()+");\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setCurFrame("+p_spr.getCurFrame()+");\n");

		if(p_spr.m_nDeepnessLevel == 0)	//Infinity
			out.print("\t\tp_ctxUnder.addChild(spr"+p_spr.getTextID()+", true, true);\n");
		else if(p_spr.m_nDeepnessLevel == 1)	//World
			out.print("\t\tp_ctxUnder.addChild(spr"+p_spr.getTextID()+", false, false);\n");
		else if(p_spr.m_nDeepnessLevel == 2)	//Camera
			out.print("\t\tp_ctxUnder.addChild(spr"+p_spr.getTextID()+", true, false);\n");

		out.print("\n");
	}

	public void onSaveSpriteJava(int p_nIdx, Sprite p_spr, PrintWriter out) throws IOException{
		String stResName= p_spr.m_stResImage.substring(p_spr.m_stResImage.indexOf("/res"));
		stResName= stResName.substring(4);

		out.print("\t\t/*DATA_SPRITE_TEMPLATE:#|CLASS_ID|NAME|RESSOURCE_NAME|POS_X|POS_Y|POS_Z|DEEPNESS|DEFAULT_STATE|DEFAULT_FRAME|NB_STATES|STATE_NAME|STATE_NB_FRAMES|STATE_SPEED*/\n");
		out.print("\t\t/*DATA:"+p_nIdx+"|Sprite|"+p_spr.getTextID()+"|"+stResName+"|"+(int)p_spr.m_vPos[0]+"|"+(int)p_spr.m_vPos[1]+"|"+(int)p_spr.m_vPos[2]+"|"+(int)p_spr.m_nDeepnessLevel+"|"+p_spr.getCurState()+"|"+p_spr.getCurFrame()+"|"+p_spr.m_vecStates.size()+"|");
			for(State stateCur : p_spr.m_vecStates){
				out.print(stateCur.m_stName+"|");
				out.print(stateCur.m_nMaxFrames+"|");
				out.print((stateCur.m_nSpeed / 32.0)+"|");
			}
		out.print("*/\n");

		out.print("\t\tSprite spr"+p_spr.getTextID()+"= new Sprite(\""+stResName+"\");\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setTextID(\""+p_spr.getTextID()+"\");\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setPos(" +(int)p_spr.getPosX()+ ", " +(int)p_spr.getPosY()+ ", " +(int)p_spr.getPosZ()+");\n");

			for(State stateCur : p_spr.m_vecStates)
				out.print("\t\tspr"+p_spr.getTextID()+".addState(spr"+p_spr.getTextID()+".new State(\""+stateCur.m_stName+"\", "+stateCur.m_nMaxFrames+", "+(stateCur.m_nSpeed / 32.0)+"f));\n");

		out.print("\t\tspr"+p_spr.getTextID()+".load();\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setCurState("+p_spr.getCurState()+");\n");
		out.print("\t\tspr"+p_spr.getTextID()+".setCurFrame("+p_spr.getCurFrame()+");\n");

		if(p_spr.m_nDeepnessLevel == 0)	//Infinity
			out.print("\t\tp_ctxUnder.addChild(spr"+p_spr.getTextID()+", true, true);\n");
		else if(p_spr.m_nDeepnessLevel == 1)	//World
			out.print("\t\tp_ctxUnder.addChild(spr"+p_spr.getTextID()+", false, false);\n");
		else if(p_spr.m_nDeepnessLevel == 2)	//Camera
			out.print("\t\tp_ctxUnder.addChild(spr"+p_spr.getTextID()+", true, false);\n");

		out.print("\n");
	}

	public void onLoadSpriteJava(String p_stPathProject, String p_stData) throws IOException{
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Sprite or Custom Sprite
		String stTextClassId= p_stData.substring(0, p_stData.indexOf("|"));

		if(stTextClassId.equals("Sprite")){
			stTextClassId= "com.bianisoft.engine.sprites.Sprite";
		}

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For idText
		String stIdText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stFilename
		String stFilename= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosX
		String stPosX= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosY
		String stPosY= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosZ
		String stPosZ= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Deepness Level
		String stDeepness= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stDefautState
		String stDefautState= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stDefautFrame
		String stDefautFrame= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stNbState
		String stNbState= p_stData.substring(0, p_stData.indexOf("|"));
		int nNbState= Integer.parseInt(stNbState);

		String[] stStateName= new String[nNbState];
		String[] stStateNbFrame= new String[nNbState];
		String[] stStateSpeed= new String[nNbState];

		for(int i= 0; i < nNbState; ++i){
			p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stSateName[i]
			stStateName[i]= p_stData.substring(0, p_stData.indexOf("|"));

			p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stStateNbFrame[i]
			stStateNbFrame[i]= p_stData.substring(0, p_stData.indexOf("|"));

			p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stStateSpeed[i]
			stStateSpeed[i]= p_stData.substring(0, p_stData.indexOf("|"));
		}

		Sprite p_objSprite= Sprite.create(Sprite.TYPE_STANDARD, p_stPathProject+"/res"+stFilename, 0, 0);
		p_objSprite.setTextID(stIdText);
		p_objSprite.setPosX(Integer.parseInt(stPosX));
		p_objSprite.setPosY(Integer.parseInt(stPosY));
		p_objSprite.setPosZ(Integer.parseInt(stPosZ));
		p_objSprite.m_nDeepnessLevel= Integer.parseInt(stDeepness);

		p_objSprite.setClassID(stTextClassId);

		for(int i= 0; i < nNbState; ++i)
			p_objSprite.addState(p_objSprite.new State(stStateName[i], Integer.parseInt(stStateNbFrame[i]), Double.parseDouble(stStateSpeed[i])));

		p_objSprite.setCurState(Integer.parseInt(stDefautState));
		p_objSprite.setCurFrame(Integer.parseInt(stDefautFrame));
		addChild(p_objSprite);
	}

	public void onSaveButtonJava(int p_nIdx, Button p_bt, PrintWriter out) throws IOException{
		String stResName= p_bt.m_stResImage.substring(p_bt.m_stResImage.indexOf("/res"));
		stResName= stResName.substring(4);

		out.print("\t\t/*DATA_BUTTON_TEMPLATE:#|CLASS_ID|NAME|RESSOURCE_NAME|POS_X|POS_Y|POS_Z|DEEPNESS|STATEIDLE_NB_FRAMES|IDLE_SPEED|STATEOVER_NB_FRAMES|STATEOVER_SPEED|STATEDOWN_NB_FRAMES|STATEDOWN_SPEED|STATESELECTED_NB_FRAMES|STATESELECTED_SPEED*/\n");
		out.print("\t\t/*DATA:"+p_nIdx+"|Button|"+p_bt.getTextID()+"|"+stResName+"|"+(int)p_bt.m_vPos[0]+"|"+(int)p_bt.m_vPos[1]+"|"+(int)p_bt.m_vPos[2]+"|"+(int)p_bt.m_nDeepnessLevel+"|");
			for(State stateCur : p_bt.m_vecStates){
				out.print(stateCur.m_nMaxFrames+"|");
				out.print((stateCur.m_nSpeed / 32.0) +"|");
			}
		out.print("*/\n");

		out.print("\t\tButton bt"+p_bt.getTextID()+"= new Button(\""+stResName+"\"");
			for(State stateCur : p_bt.m_vecStates){
				out.print(", " + stateCur.m_nMaxFrames);
				out.print(", " + (stateCur.m_nSpeed / 32.0) + "f");
			}
		out.print(");\n");

		out.print("\t\tbt"+p_bt.getTextID()+".setTextID(\""+p_bt.getTextID()+"\");\n");
		out.print("\t\tbt"+p_bt.getTextID()+".setPos(" +(int)p_bt.getPosX()+ ", " +(int)p_bt.getPosY()+ ", " +(int)p_bt.getPosZ()+");\n");
		out.print("\t\tbt"+p_bt.getTextID()+".load();\n");

		if(p_bt.m_nDeepnessLevel == 0)	//Infinity
			out.print("\t\tp_ctxUnder.addChild(bt"+p_bt.getTextID()+", true, true);\n");
		else if(p_bt.m_nDeepnessLevel == 1)	//World
			out.print("\t\tp_ctxUnder.addChild(bt"+p_bt.getTextID()+", false, false);\n");
		else if(p_bt.m_nDeepnessLevel == 2)	//Camera
			out.print("\t\tp_ctxUnder.addChild(bt"+p_bt.getTextID()+", true, false);\n");

		out.print("\n");
	}

	public void onLoadButtonJava(String p_stPathProject, String p_stData) throws IOException{
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Sprite
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For idText
		String stIdText= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stFilename
		String stFilename= p_stData.substring(0, p_stData.indexOf("|"));

		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosX
		String stPosX= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosY
		String stPosY= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For PosZ
		String stPosZ= p_stData.substring(0, p_stData.indexOf("|"));
		p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For Deepness Level
		String stDeepness= p_stData.substring(0, p_stData.indexOf("|"));

		String[] stStateNbFrame= new String[4];
		String[] stStateSpeed= new String[4];

		for(int i= 0; i < 4; ++i){
			p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stStateNbFrame[i]
			stStateNbFrame[i]= p_stData.substring(0, p_stData.indexOf("|"));

			p_stData= p_stData.substring(p_stData.indexOf("|") + 1);	//For stStateSpeed[i]
			stStateSpeed[i]= p_stData.substring(0, p_stData.indexOf("|"));
		}

		Button p_objButton= new Button(p_stPathProject+"/res"+stFilename, Integer.parseInt(stStateNbFrame[0]), Double.parseDouble(stStateSpeed[0]), Integer.parseInt(stStateNbFrame[1]), Double.parseDouble(stStateSpeed[1]), Integer.parseInt(stStateNbFrame[2]), Double.parseDouble(stStateSpeed[2]), Integer.parseInt(stStateNbFrame[3]), Double.parseDouble(stStateSpeed[3]));
		p_objButton.setTextID(stIdText);
		p_objButton.setPosX(Integer.parseInt(stPosX));
		p_objButton.setPosY(Integer.parseInt(stPosY));
		p_objButton.setPosZ(Integer.parseInt(stPosZ));
		p_objButton.m_nDeepnessLevel= Integer.parseInt(stDeepness);

		addChild(p_objButton);
	}

	public void onSaveJava(String p_stFilename){
		String stFilename= p_stFilename.replace(File.separator, "/");

		String stPackageName= (stFilename.substring(stFilename.indexOf("com/bianisoft"), stFilename.indexOf("/DesignCtx"))).replace('/', '.');
		String stClassName= stFilename.substring(stFilename.indexOf("DesignCtx"), stFilename.indexOf(".java"));

		try{
			FileWriter outFile = new FileWriter(p_stFilename);
			PrintWriter out = new PrintWriter(outFile);

			out.print("package " + stPackageName + ";\n");
			out.print("\n");
			out.print("\n");
			out.print("//LWJGL library imports\n");
			out.print("import org.lwjgl.util.Rectangle;\n");
			out.print("\n");
			out.print("//Bianisoft imports\n");
			out.print("import com.bianisoft.engine.Context;\n");
			out.print("import com.bianisoft.engine.backgrounds.Background;\n");
			out.print("import com.bianisoft.engine.labels.Label;\n");
			out.print("import com.bianisoft.engine.labels.LabelTextField;\n");
			out.print("import com.bianisoft.engine.sprites.Sprite;\n");
			out.print("import com.bianisoft.engine.sprites.Sprite.State;\n");
			out.print("import com.bianisoft.engine.sprites.Button;\n");
			out.print("\n");
			out.print("\n");
			out.print("public class "+ stClassName + "{\n");
			out.print("\tpublic static void load(Context p_ctxUnder){\n");

			for(int i= 0; i < m_vecPhysObj.size(); ++i){
				PhysObj physObj= m_vecPhysObj.get(i);

				if(physObj.isKindOf(Obj.IDCLASS_Background)){
					onSaveBackgroundJava(i, (Background)physObj, out);
				}else if(physObj.isKindOf(Obj.IDCLASS_Label)){
					if(physObj.getSubClassID() == Label.TYPE_TEXTFIELD)
						onSaveLabelTextFieldJava(i, (Label)physObj, out);
					else
						onSaveLabelJava(i, (Label)physObj, out);
				}else if(physObj.isKindOf(Obj.IDCLASS_Button)){
					onSaveButtonJava(i, (Button)physObj, out);
				}else if(physObj.isKindOf(Obj.IDCLASS_Sprite)){
					if(physObj.getTextClassID().equals("com.bianisoft.engine.sprites.Sprite"))
						onSaveSpriteJava(i, (Sprite)physObj, out);
					else
						onSaveCustomSpriteJava(i, (Sprite)physObj, out);
				}
			}
			
			out.print("\t}\n");
			out.print("}\n");

			out.close();
			outFile.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void onLoadJava(String p_stFilename){
		try{
			FileInputStream file= new FileInputStream(p_stFilename);
			DataInputStream dis= new DataInputStream(file);
			BufferedReader	br= new BufferedReader(new InputStreamReader(dis));

			String stFilename= p_stFilename.replace(File.separator, "/");
			String stProjectPath= stFilename.substring(0, stFilename.indexOf("/src/com/bianisoft"));

			String stLine;
			while((stLine= br.readLine()) != null){
				if(stLine.contains("|Background|")){
					onLoadBackgroundJava(stProjectPath, stLine);
				}else if(stLine.contains("|Label|")){
					onLoadLabelJava(stProjectPath, stLine);
				}else if(stLine.contains("|LabelTextField|")){
					onLoadLabelTextFieldJava(stProjectPath, stLine);
				}else if(stLine.contains("|Button|")){
					onLoadButtonJava(stProjectPath, stLine);
				}else if(stLine.contains("|Sprite|")){
					onLoadSpriteJava(stProjectPath, stLine);
				}else if(stLine.contains("|com.bianisoft.games.")){
					onLoadSpriteJava(stProjectPath, stLine);
				}
			}

			br.close();
			dis.close();
			file.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setSelectedPhysObj(PhysObj p_physObj){
		if(m_physObjSelected != null){
			if((m_physObjSelected.getClassID() == Obj.IDCLASS_Background)){
				((Background)m_physObjSelected).setFilterColor(1, 1, 1, 1);
			}else if((m_physObjSelected.getClassID() == Obj.IDCLASS_Label)){
				((Label)m_physObjSelected).setFilterColor(1, 1, 1, 1);
			}else if((m_physObjSelected.getClassID() == Obj.IDCLASS_Sprite)){
				((Drawable)m_physObjSelected).setFilterColor(1, 1, 1, 1);
			}else if((m_physObjSelected.getClassID() == Obj.IDCLASS_Button)){
				((Button)m_physObjSelected).setFilterColor(1, 1, 1, 1);
			}
		}

		m_physObjSelected= p_physObj;
	}

	public PhysObj getSelectedPhysObj(){
		return m_physObjSelected;
	}

	public void onPanStart(){
		Camera cam= Camera.getCur(Camera.TYPE_2D);
		m_nBasePosX= (int)cam.m_vPos[0];
		m_nBasePosY= (int)cam.m_vPos[1];
	}

	public void onPanDo(int p_nDifX, int p_nDifY){
		Camera cam= Camera.getCur(Camera.TYPE_2D);
		cam.setPos(m_nBasePosX + p_nDifX, m_nBasePosY + p_nDifY);
	}

	public void manage(double f){
		checkForLoadingPending();
		super.manage(f);

		if(m_physObjSelected == null)
			return;

		if((m_physObjSelected.getClassID() == Obj.IDCLASS_Background)){
			((Background)m_physObjSelected).setFilterColor(0.5, 0.5, 1, 1);
		}else if((m_physObjSelected.getClassID() == Obj.IDCLASS_Label)){
			((Label)m_physObjSelected).setFilterColor(0.5, 0.5, 1, 1);
		}else if((m_physObjSelected.getClassID() == Obj.IDCLASS_Sprite)){
			((Drawable)m_physObjSelected).setFilterColor(0.5, 0.5, 1, 1);
		}else if((m_physObjSelected.getClassID() == Obj.IDCLASS_Button)){
			((Button)m_physObjSelected).setFilterColor(0.5, 0.5, 1, 1);
		}
	}

	public void draw(){
		checkForLoadingPending();

		App.g_theApp.orthogonalStart(App.g_CurrentDrawable);

		for(PhysObj physObject : m_vecPhysObj){
			if(((physObject.isKindOf(Obj.IDCLASS_Sprite)) || (physObject.isKindOf(Obj.IDCLASS_Label)) ||
			    (physObject.isKindOf(Obj.IDCLASS_Background)) || (physObject.isKindOf(Obj.IDCLASS_Object3D))))
				((Drawable)physObject).draw();
		}


		if(App.PRINT_DEBUG)
			drawDebug();

		App.g_theApp.orthogonalEnd(App.g_CurrentDrawable);


		if(m_pendingScreenshotFile != null){
			try{
				Screenshot.writeToTargaFile(m_pendingScreenshotFile, 640, 480);
			}catch(Exception e){
				System.out.print("Screenshot Failed!");
			}

			m_pendingScreenshotFile= null;
		}
	}
}
