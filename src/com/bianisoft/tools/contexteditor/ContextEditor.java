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


//Standard Java import
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

//JOGL library imports
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;

//Bianisoft Import
import com.bianisoft.engine.Obj;
import com.bianisoft.engine.backgrounds.Background;
import com.bianisoft.engine.Button;
import com.bianisoft.engine.Camera;
import com.bianisoft.engine.labels.Label;
import com.bianisoft.engine.PhysObj;
import com.bianisoft.engine.sprites.Sprite;
import com.bianisoft.engine.sprites.Sprite.State;


class MyTableModel extends AbstractTableModel{
	public static MyTableModel gThis;

    public String[]			m_stColumnNames = {"StateName", "NbFrame", "AnimSpeed"};
	public ArrayList<String>	m_arStateNames= new ArrayList<String>();
	public ArrayList<Integer>	m_arNbFrames= new ArrayList<Integer>();
	public ArrayList<Double>	m_arAnimSpeed= new ArrayList<Double>();

	public MyTableModel(){
		super();
		gThis= this;
	}

    public int getColumnCount(){
        return 3;
    }

    public int getRowCount(){
        return m_arStateNames.size();
    }

    public String getColumnName(int col){
        return m_stColumnNames[col];
    }

    public Object getValueAt(int row, int col){
		if(col == 0)
			return m_arStateNames.get(row);
		else if(col == 1)
			return m_arNbFrames.get(row);
		else if(col == 2)
			return m_arAnimSpeed.get(row);

		return null;
    }

    public Class getColumnClass(int c){
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
		return true;
    }

    public void setValueAt(Object value, int row, int col) {
		if(col == 0)
			m_arStateNames.set(row, (String)value);
		else if(col == 1)
			m_arNbFrames.set(row, (Integer)value);
		else if(col == 2)
			m_arAnimSpeed.set(row, (Double)value);

        fireTableCellUpdated(row, col);
    }
}


public class ContextEditor extends JFrame {

    static {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }

	AppContextEditor	m_appBiani;
	CtxEditor			m_curContext;
	boolean				m_isPanningActive= false;
	boolean				m_isDeleteActive= false;


	int	m_nPosXBase;
	int	m_nPosYBase;
	String m_curDirectory= ".";


	public ContextEditor(){
		initComponents();
		setTitle("Bianitool - Context Editor");

		m_appBiani= new AppContextEditor(m_GLCanvas, this);
		m_curContext= (CtxEditor)m_appBiani.m_arObj.get(0);

        m_GLCanvas.setMinimumSize(new Dimension());

		//Create the label table
		Hashtable labelTable= new Hashtable();
		labelTable.put(new Integer(0), new JLabel("Left"));
		labelTable.put(new Integer(1), new JLabel("Center"));
		labelTable.put(new Integer(2), new JLabel("Right"));
		jSldr_LabelAlignment.setLabelTable(labelTable);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_GLCanvas = new GLCanvas(createGLCapabilites());
        jToolBar = new JToolBar();
        jBtToolbar_New = new JButton();
        jBtToolbar_OpenJava = new JButton();
        jBtToolbar_SaveJava = new JButton();
        jSeparator1 = new Separator();
        jBt_SaveScreenshot = new JButton();
        jSeparator3 = new Separator();
        jBtRemove = new JButton();
        jBtMove = new JButton();
        jBtZoomIn = new JButton();
        jBtZoomOut = new JButton();
        jTabPane_ObjectProperties = new JTabbedPane();
        jPnl_Background = new JPanel();
        jLabel14 = new JLabel();
        jLabel15 = new JLabel();
        jLabel16 = new JLabel();
        jTxtFld_BackgroundID = new JTextField();
        jTxtFld_BackgroundImage = new JTextField();
        jTxtFld_BackgroundPosX = new JTextField();
        jTxtFld_BackgroundPosY = new JTextField();
        jTxtFld_BackgroundPosZ = new JTextField();
        jBt_BackgroundBrowse = new JButton();
        jBt_BackgroundCreate = new JButton();
        jBt_BackgroundModify = new JButton();
        jSldr_BackgroundDeepness = new JSlider();
        jLabel24 = new JLabel();
        jPnl_Button = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel17 = new JLabel();
        jTxtFld_ButtonID = new JTextField();
        jTxtFld_ButtonImage = new JTextField();
        jTxtFld_ButtonNbFramesIdle = new JTextField();
        jTxtFld_ButtonNbFramesOver = new JTextField();
        jTxtFld_ButtonNbFramesSelected = new JTextField();
        jTxtFld_ButtonNbFramesClicked = new JTextField();
        jTxtFld_ButtonAnimSpeedIdle = new JTextField();
        jTxtFld_ButtonAnimSpeedOver = new JTextField();
        jTxtFld_ButtonAnimSpeedSelected = new JTextField();
        jTxtFld_ButtonAnimSpeedClicked = new JTextField();
        jTxtFld_ButtonPosX = new JTextField();
        jTxtFld_ButtonPosY = new JTextField();
        jTxtFld_ButtonPosZ = new JTextField();
        jBt_ButtonBrowse = new JButton();
        jBt_ButtonCreate = new JButton();
        jBt_ButtonModify = new JButton();
        jSldr_ButtonDeepness = new JSlider();
        jLabel25 = new JLabel();
        jPnl_Label = new JPanel();
        jLabel3 = new JLabel();
        jLabel5 = new JLabel();
        jLabel13 = new JLabel();
        jLabel19 = new JLabel();
        jLabel6 = new JLabel();
        jLabel18 = new JLabel();
        jTxtFld_LabelID = new JTextField();
        jTxtFld_LabelFont = new JTextField();
        jTxtFld_LabelFontSize = new JTextField();
        jTxtFld_LabelPosX = new JTextField();
        jTxtFld_LabelPosY = new JTextField();
        jTxtFld_LabelPosZ = new JTextField();
        jTxtFld_LabelWidth = new JTextField();
        jTxtFld_LabelHeight = new JTextField();
        jScrollPane1 = new JScrollPane();
        jTxtArea_LabelText = new JTextArea();
        jSldr_LabelAlignment = new JSlider();
        jChkBx_LabelTextField = new JCheckBox();
        jBt_LabelBrowse = new JButton();
        jBt_LabelCreate = new JButton();
        jBt_LabelModify = new JButton();
        jLabel22 = new JLabel();
        jSldr_LabelDeepness = new JSlider();
        jLabel26 = new JLabel();
        jPnl_Sprite = new JPanel();
        jLabel4 = new JLabel();
        jLabel7 = new JLabel();
        jLabel11 = new JLabel();
        jLabel12 = new JLabel();
        jLabel21 = new JLabel();
        jLabel10 = new JLabel();
        jLabel23 = new JLabel();
        jTxtFld_SpriteID = new JTextField();
        jTxtFld_SpriteImage = new JTextField();
        jTxtFld_SpritePosX = new JTextField();
        jTxtFld_SpritePosY = new JTextField();
        jTxtFld_SpritePosZ = new JTextField();
        jTxtFld_SpriteCustomClassID = new JTextField();
        jSeparator2 = new JSeparator();
        jTxtFld_SpriteDefaultState = new JTextField();
        jTxtFld_SpriteDefaultFrame = new JTextField();
        jScrollPane2 = new JScrollPane();
        jTbl_SpriteStateMatrix = new JTable();
        jBt_SpriteBrowseImage = new JButton();
        jBt_SpriteAddState = new JButton();
        jBt_SpriteRemoveState = new JButton();
        jBt_SpriteCreate = new JButton();
        jBt_SpriteModify = new JButton();
        jLabel27 = new JLabel();
        jSldr_SpriteDeepness = new JSlider();
        jPanel1 = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        m_GLCanvas.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent evt) {
                onViewZoom(evt);
            }
        });
        m_GLCanvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                onViewDown(evt);
            }
            public void mouseClicked(MouseEvent evt) {
                onViewClicked(evt);
            }
        });
        m_GLCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent evt) {
                onViewMove(evt);
            }
            public void mouseDragged(MouseEvent evt) {
                onViewDragged(evt);
            }
        });

        jToolBar.setRollover(true);
        jToolBar.setMinimumSize(new Dimension(56, 37));
        jToolBar.setPreferredSize(new Dimension(100, 37));

        jBtToolbar_New.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconNewFile.png"))); // NOI18N
        jBtToolbar_New.setFocusable(false);
        jBtToolbar_New.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtToolbar_New.setVerticalTextPosition(SwingConstants.BOTTOM);
        jBtToolbar_New.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onToolbarButtonNew(evt);
            }
        });
        jToolBar.add(jBtToolbar_New);

        jBtToolbar_OpenJava.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconOpenJava.png"))); // NOI18N
        jBtToolbar_OpenJava.setFocusable(false);
        jBtToolbar_OpenJava.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtToolbar_OpenJava.setVerticalTextPosition(SwingConstants.BOTTOM);
        jBtToolbar_OpenJava.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onToolbarButtonLoadJava(evt);
            }
        });
        jToolBar.add(jBtToolbar_OpenJava);

        jBtToolbar_SaveJava.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconSaveJava.png"))); // NOI18N
        jBtToolbar_SaveJava.setFocusable(false);
        jBtToolbar_SaveJava.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtToolbar_SaveJava.setVerticalTextPosition(SwingConstants.BOTTOM);
        jBtToolbar_SaveJava.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onToolbarButtonSaveJava(evt);
            }
        });
        jToolBar.add(jBtToolbar_SaveJava);
        jToolBar.add(jSeparator1);

        jBt_SaveScreenshot.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconScreenshot.png"))); // NOI18N
        jBt_SaveScreenshot.setFocusable(false);
        jBt_SaveScreenshot.setHorizontalTextPosition(SwingConstants.CENTER);
        jBt_SaveScreenshot.setVerticalTextPosition(SwingConstants.BOTTOM);
        jBt_SaveScreenshot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onToolbarButtonScreenshot(evt);
            }
        });
        jToolBar.add(jBt_SaveScreenshot);
        jToolBar.add(jSeparator3);

        jBtRemove.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconMinus.png"))); // NOI18N
        jBtRemove.setFocusable(false);
        jBtRemove.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtRemove.setVerticalTextPosition(SwingConstants.BOTTOM);
        jBtRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onButtonDelete(evt);
            }
        });
        jToolBar.add(jBtRemove);

        jBtMove.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconPan.png"))); // NOI18N
        jBtMove.setFocusable(false);
        jBtMove.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtMove.setVerticalTextPosition(SwingConstants.BOTTOM);
        jToolBar.add(jBtMove);

        jBtZoomIn.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconMagnifyPlus.png"))); // NOI18N
        jBtZoomIn.setFocusable(false);
        jBtZoomIn.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtZoomIn.setVerticalTextPosition(SwingConstants.BOTTOM);
        jToolBar.add(jBtZoomIn);

        jBtZoomOut.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconMagnifyMinus.png"))); // NOI18N
        jBtZoomOut.setFocusable(false);
        jBtZoomOut.setHorizontalTextPosition(SwingConstants.CENTER);
        jBtZoomOut.setVerticalTextPosition(SwingConstants.BOTTOM);
        jToolBar.add(jBtZoomOut);

        jLabel14.setText("ID");

        jLabel15.setText("Image");

        jLabel16.setText("Position");

        jTxtFld_BackgroundID.setText("Back_");

        jTxtFld_BackgroundPosX.setText("0");

        jTxtFld_BackgroundPosY.setText("0");

        jTxtFld_BackgroundPosZ.setText("0");

        jBt_BackgroundBrowse.setText("...");
        jBt_BackgroundBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onBackgroundFilenameBrowse(evt);
            }
        });

        jBt_BackgroundCreate.setText("Create!");
        jBt_BackgroundCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onBackgroundCreate(evt);
            }
        });

        jBt_BackgroundModify.setText("Modify");
        jBt_BackgroundModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onBackgroundModifiy(evt);
            }
        });

        jSldr_BackgroundDeepness.setMajorTickSpacing(1);
        jSldr_BackgroundDeepness.setMaximum(2);
        jSldr_BackgroundDeepness.setPaintTicks(true);
        jSldr_BackgroundDeepness.setSnapToTicks(true);
        jSldr_BackgroundDeepness.setValue(1);

        jLabel24.setText("Deepness");

        GroupLayout jPnl_BackgroundLayout = new GroupLayout(jPnl_Background);
        jPnl_Background.setLayout(jPnl_BackgroundLayout);
        jPnl_BackgroundLayout.setHorizontalGroup(
            jPnl_BackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_BackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(jPnl_BackgroundLayout.createSequentialGroup()
                            .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jLabel14)
                                .addComponent(jLabel15))
                            .addGap(22, 22, 22)
                            .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(jTxtFld_BackgroundID)
                                .addComponent(jTxtFld_BackgroundImage, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBt_BackgroundBrowse, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                        .addGroup(Alignment.LEADING, jPnl_BackgroundLayout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jSldr_BackgroundDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPnl_BackgroundLayout.createSequentialGroup()
                                    .addComponent(jTxtFld_BackgroundPosX, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTxtFld_BackgroundPosY, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTxtFld_BackgroundPosZ, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                            .addGap(142, 142, 142)))
                    .addComponent(jLabel24))
                .addContainerGap(62, Short.MAX_VALUE))
            .addGroup(Alignment.TRAILING, jPnl_BackgroundLayout.createSequentialGroup()
                .addContainerGap(379, Short.MAX_VALUE)
                .addComponent(jBt_BackgroundModify)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jBt_BackgroundCreate)
                .addContainerGap())
        );
        jPnl_BackgroundLayout.setVerticalGroup(
            jPnl_BackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_BackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTxtFld_BackgroundID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTxtFld_BackgroundImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBt_BackgroundBrowse))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTxtFld_BackgroundPosX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTxtFld_BackgroundPosY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_BackgroundPosZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addGap(5, 5, 5)
                .addComponent(jSldr_BackgroundDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(272, 272, 272)
                .addGroup(jPnl_BackgroundLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jBt_BackgroundCreate)
                    .addComponent(jBt_BackgroundModify))
                .addContainerGap())
        );

        jTabPane_ObjectProperties.addTab("Background", jPnl_Background);

        jLabel1.setText("ID");

        jLabel2.setText("Image");

        jLabel8.setText("Animation Speed");

        jLabel9.setText("Position");

        jLabel17.setText("Nb Frames");

        jTxtFld_ButtonID.setText("Bt_");

        jTxtFld_ButtonNbFramesIdle.setText("1");

        jTxtFld_ButtonNbFramesOver.setText("1");

        jTxtFld_ButtonNbFramesSelected.setText("1");

        jTxtFld_ButtonNbFramesClicked.setText("1");

        jTxtFld_ButtonAnimSpeedIdle.setText("0.0");

        jTxtFld_ButtonAnimSpeedOver.setText("0.0");

        jTxtFld_ButtonAnimSpeedSelected.setText("0.0");

        jTxtFld_ButtonAnimSpeedClicked.setText("0.0");

        jTxtFld_ButtonPosX.setText("0");

        jTxtFld_ButtonPosY.setText("0");

        jTxtFld_ButtonPosZ.setText("0");

        jBt_ButtonBrowse.setText("...");
        jBt_ButtonBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onButtonFilenameBrowse(evt);
            }
        });

        jBt_ButtonCreate.setText("Create!");
        jBt_ButtonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onButtonCreate(evt);
            }
        });

        jBt_ButtonModify.setText("Modify");
        jBt_ButtonModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onButtonModify(evt);
            }
        });

        jSldr_ButtonDeepness.setMajorTickSpacing(1);
        jSldr_ButtonDeepness.setMaximum(2);
        jSldr_ButtonDeepness.setPaintTicks(true);
        jSldr_ButtonDeepness.setSnapToTicks(true);
        jSldr_ButtonDeepness.setValue(1);

        jLabel25.setText("Deepness");

        GroupLayout jPnl_ButtonLayout = new GroupLayout(jPnl_Button);
        jPnl_Button.setLayout(jPnl_ButtonLayout);
        jPnl_ButtonLayout.setHorizontalGroup(
            jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, jPnl_ButtonLayout.createSequentialGroup()
                        .addComponent(jBt_ButtonModify)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jBt_ButtonCreate))
                    .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                        .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel17))
                        .addGap(61, 61, 61)
                        .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
                            .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                                .addComponent(jTxtFld_ButtonNbFramesIdle, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonNbFramesOver, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonNbFramesSelected, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonNbFramesClicked, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.TRAILING, false)
                                    .addComponent(jTxtFld_ButtonID, Alignment.LEADING)
                                    .addComponent(jTxtFld_ButtonImage, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jBt_ButtonBrowse, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))))
                    .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                        .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel25))
                        .addGap(21, 21, 21)
                        .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jSldr_ButtonDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                                .addComponent(jTxtFld_ButtonAnimSpeedIdle, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonAnimSpeedOver, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonAnimSpeedSelected, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonAnimSpeedClicked, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                                .addComponent(jTxtFld_ButtonPosX, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonPosY, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_ButtonPosZ, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPnl_ButtonLayout.setVerticalGroup(
            jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTxtFld_ButtonID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTxtFld_ButtonImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBt_ButtonBrowse))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTxtFld_ButtonNbFramesIdle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonNbFramesOver, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonNbFramesSelected, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonNbFramesClicked, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTxtFld_ButtonAnimSpeedIdle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonAnimSpeedOver, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonAnimSpeedSelected, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonAnimSpeedClicked, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTxtFld_ButtonPosX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_ButtonPosY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTxtFld_ButtonPosZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPnl_ButtonLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(ComponentPlacement.RELATED, 271, Short.MAX_VALUE)
                        .addGroup(jPnl_ButtonLayout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(jBt_ButtonCreate)
                            .addComponent(jBt_ButtonModify)))
                    .addComponent(jSldr_ButtonDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabPane_ObjectProperties.addTab("Button ", jPnl_Button);

        jLabel3.setText("ID");

        jLabel5.setText("Font");

        jLabel13.setText("Position");

        jLabel19.setText("Rectangle");

        jLabel6.setText("Text");

        jLabel18.setText("Alignment");

        jTxtFld_LabelID.setText("Lbl_");

        jTxtFld_LabelFontSize.setText("25");

        jTxtFld_LabelPosX.setText("0");

        jTxtFld_LabelPosY.setText("0");

        jTxtFld_LabelPosZ.setText("0");

        jTxtFld_LabelWidth.setText("100");

        jTxtFld_LabelHeight.setText("100");

        jTxtArea_LabelText.setColumns(20);
        jTxtArea_LabelText.setRows(5);
        jScrollPane1.setViewportView(jTxtArea_LabelText);

        jSldr_LabelAlignment.setMaximum(2);
        jSldr_LabelAlignment.setPaintLabels(true);
        jSldr_LabelAlignment.setPaintTicks(true);
        jSldr_LabelAlignment.setSnapToTicks(true);
        jSldr_LabelAlignment.setValue(0);

        jChkBx_LabelTextField.setText("is TextField ?");

        jBt_LabelBrowse.setText("...");
        jBt_LabelBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onLabelBrowseFont(evt);
            }
        });

        jBt_LabelCreate.setText("Create!");
        jBt_LabelCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onLabelCreate(evt);
            }
        });

        jBt_LabelModify.setText("Modify");
        jBt_LabelModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onLabelModify(evt);
            }
        });

        jLabel22.setText("Color: ");
        jLabel22.setEnabled(false);

        jSldr_LabelDeepness.setMajorTickSpacing(1);
        jSldr_LabelDeepness.setMaximum(2);
        jSldr_LabelDeepness.setPaintTicks(true);
        jSldr_LabelDeepness.setSnapToTicks(true);
        jSldr_LabelDeepness.setValue(1);

        jLabel26.setText("Deepness");

        GroupLayout jPnl_LabelLayout = new GroupLayout(jPnl_Label);
        jPnl_Label.setLayout(jPnl_LabelLayout);
        jPnl_LabelLayout.setHorizontalGroup(
            jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_LabelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, jPnl_LabelLayout.createSequentialGroup()
                        .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel13)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
                            .addGroup(jPnl_LabelLayout.createSequentialGroup()
                                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.TRAILING)
                                    .addComponent(jTxtFld_LabelID, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                    .addGroup(Alignment.LEADING, jPnl_LabelLayout.createSequentialGroup()
                                        .addComponent(jTxtFld_LabelPosX, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(jTxtFld_LabelPosY, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(jTxtFld_LabelPosZ, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(Alignment.LEADING, jPnl_LabelLayout.createSequentialGroup()
                                        .addComponent(jTxtFld_LabelWidth, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(jTxtFld_LabelHeight, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTxtFld_LabelFont, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jTxtFld_LabelFontSize, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(jBt_LabelBrowse, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)))
                    .addGroup(jPnl_LabelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jSldr_LabelAlignment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(jChkBx_LabelTextField)
                        .addGap(83, 83, 83))
                    .addComponent(jLabel22)
                    .addGroup(jPnl_LabelLayout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addComponent(jSldr_LabelDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(Alignment.TRAILING, jPnl_LabelLayout.createSequentialGroup()
                        .addComponent(jBt_LabelModify)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jBt_LabelCreate)))
                .addContainerGap())
        );
        jPnl_LabelLayout.setVerticalGroup(
            jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPnl_LabelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTxtFld_LabelID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTxtFld_LabelFont, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jBt_LabelBrowse)
                    .addComponent(jTxtFld_LabelFontSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTxtFld_LabelPosX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTxtFld_LabelPosY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_LabelPosZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTxtFld_LabelWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_LabelHeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPnl_LabelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(88, 88, 88)
                        .addComponent(jLabel18))
                    .addGroup(jPnl_LabelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jSldr_LabelAlignment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jChkBx_LabelTextField))))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jSldr_LabelDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addGroup(jPnl_LabelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jBt_LabelCreate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBt_LabelModify, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addComponent(jLabel22)
                .addGap(656, 656, 656))
        );

        jTabPane_ObjectProperties.addTab("Label", jPnl_Label);

        jLabel4.setText("ID");

        jLabel7.setText("Image");

        jLabel11.setText("Position");

        jLabel12.setText("Default State");

        jLabel21.setText("Frame");

        jLabel10.setText("States");

        jLabel23.setText("ClassID Custom");

        jTxtFld_SpriteID.setText("Spr_");

        jTxtFld_SpritePosX.setText("0");

        jTxtFld_SpritePosY.setText("0");

        jTxtFld_SpritePosZ.setText("0");

        jTxtFld_SpriteCustomClassID.setText("com.bianisoft.engine.base.sprites.Sprite");

        jTxtFld_SpriteDefaultState.setText("0");

        jTxtFld_SpriteDefaultFrame.setText("0");

        jTbl_SpriteStateMatrix.setModel(new MyTableModel());
        jTbl_SpriteStateMatrix.setMaximumSize(new Dimension(100, 32768));
        jTbl_SpriteStateMatrix.setMinimumSize(new Dimension(100, 0));
        jTbl_SpriteStateMatrix.setPreferredSize(null);
        jScrollPane2.setViewportView(jTbl_SpriteStateMatrix);

        jBt_SpriteBrowseImage.setText("...");
        jBt_SpriteBrowseImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onSpriteBrowseImage(evt);
            }
        });

        jBt_SpriteAddState.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconPlus.png"))); // NOI18N
        jBt_SpriteAddState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onSpriteButtonAddState(evt);
            }
        });

        jBt_SpriteRemoveState.setIcon(new ImageIcon(getClass().getResource("/com/bianisoft/tools/contexteditor/res/iconMinus.png"))); // NOI18N
        jBt_SpriteRemoveState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onSpriteButtonRemoveState(evt);
            }
        });

        jBt_SpriteCreate.setText("Create!");
        jBt_SpriteCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onSpriteButtonCreate(evt);
            }
        });

        jBt_SpriteModify.setText("Modify");
        jBt_SpriteModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onSpriteButtonModify(evt);
            }
        });

        jLabel27.setText("Deepness");

        jSldr_SpriteDeepness.setMajorTickSpacing(1);
        jSldr_SpriteDeepness.setMaximum(2);
        jSldr_SpriteDeepness.setPaintTicks(true);
        jSldr_SpriteDeepness.setSnapToTicks(true);
        jSldr_SpriteDeepness.setValue(1);

        GroupLayout jPnl_SpriteLayout = new GroupLayout(jPnl_Sprite);
        jPnl_Sprite.setLayout(jPnl_SpriteLayout);
        jPnl_SpriteLayout.setHorizontalGroup(
            jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jSeparator2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                        .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                            .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                                .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                                    .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel7))
                                    .addGap(22, 22, 22)
                                    .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(jTxtFld_SpriteID)
                                        .addComponent(jTxtFld_SpriteImage, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jBt_SpriteBrowseImage, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTxtFld_SpritePosX, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTxtFld_SpritePosY, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTxtFld_SpritePosZ, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(62, Short.MAX_VALUE))
                        .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                            .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addComponent(jLabel10))
                            .addGap(26, 26, 26)
                            .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                                .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                                    .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jBt_SpriteAddState)
                                        .addComponent(jBt_SpriteRemoveState)))
                                .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                                    .addComponent(jTxtFld_SpriteDefaultState, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                    .addGap(68, 68, 68)
                                    .addComponent(jLabel21)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jTxtFld_SpriteDefaultFrame, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(64, Short.MAX_VALUE))
                        .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jTxtFld_SpriteCustomClassID, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                            .addComponent(jLabel27)
                            .addComponent(jSldr_SpriteDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(232, Short.MAX_VALUE)))
                    .addGroup(Alignment.TRAILING, jPnl_SpriteLayout.createSequentialGroup()
                        .addComponent(jBt_SpriteModify)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jBt_SpriteCreate)
                        .addContainerGap())))
        );
        jPnl_SpriteLayout.setVerticalGroup(
            jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTxtFld_SpriteID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTxtFld_SpriteImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBt_SpriteBrowseImage))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTxtFld_SpritePosX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jTxtFld_SpritePosY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_SpritePosZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTxtFld_SpriteCustomClassID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jSldr_SpriteDeepness, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel21)
                    .addComponent(jTxtFld_SpriteDefaultFrame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFld_SpriteDefaultState, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(jPnl_SpriteLayout.createSequentialGroup()
                        .addComponent(jBt_SpriteAddState, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jBt_SpriteRemoveState, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(jPnl_SpriteLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jBt_SpriteCreate)
                    .addComponent(jBt_SpriteModify))
                .addContainerGap())
        );

        jTabPane_ObjectProperties.addTab("Sprite", jPnl_Sprite);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );

        jTabPane_ObjectProperties.addTab("Object3D", jPanel1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(jToolBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1188, Short.MAX_VALUE)
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(m_GLCanvas, GroupLayout.PREFERRED_SIZE, 640, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jTabPane_ObjectProperties, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(m_GLCanvas, GroupLayout.PREFERRED_SIZE, 480, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabPane_ObjectProperties, GroupLayout.PREFERRED_SIZE, 553, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


	private void onViewClicked(MouseEvent evt) {//GEN-FIRST:event_onViewClicked
		Camera cam= Camera.getCur(Camera.TYPE_2D);
		int posX= (int)cam.doUnprojectionX(evt.getX());
		int posY= (int)cam.doUnprojectionY(evt.getY());

		if(m_isPanningActive){
		}else{
			m_nPosXBase= evt.getX();
			m_nPosYBase= evt.getY();

			PhysObj physObj= m_curContext.findAtByRtti(posX, posY, -1, Obj.IDCLASS_PhysObj);

			if(m_isDeleteActive){
				m_curContext.removeChild(physObj);
				m_curContext.setSelectedPhysObj(null);
				return;
			}
			m_curContext.setSelectedPhysObj(physObj);

			if(physObj == null)
				return;

			switch(physObj.getClassID()){
				case Obj.IDCLASS_Background:
					Background bck= (Background)physObj;

					if(bck.m_stResImage.startsWith("file:"))
						bck.m_stResImage= bck.m_stResImage.substring(5);

					jTabPane_ObjectProperties.setSelectedIndex(0);
					jTxtFld_BackgroundImage.setText(((Background)physObj).m_stResImage);
					jTxtFld_BackgroundID.setText(physObj.getTextID());
					jTxtFld_BackgroundPosX.setText(Integer.toString((int)physObj.m_vPos[0]));
					jTxtFld_BackgroundPosY.setText(Integer.toString((int)physObj.m_vPos[1]));
					jTxtFld_BackgroundPosZ.setText(Integer.toString((int)physObj.m_vPos[2]));
					jSldr_BackgroundDeepness.setValue(physObj.m_nDeepnessLevel);

				break;
				case Obj.IDCLASS_Button:
					Button btn= (Button)physObj;

					if(btn.m_stResImage.startsWith("file:"))
						btn.m_stResImage= btn.m_stResImage.substring(5);

					jTabPane_ObjectProperties.setSelectedIndex(1);
					jTxtFld_ButtonImage.setText(btn.m_stResImage);
					jTxtFld_ButtonID.setText(physObj.getTextID());
					jTxtFld_ButtonPosX.setText(Integer.toString((int)physObj.m_vPos[0]));
					jTxtFld_ButtonPosY.setText(Integer.toString((int)physObj.m_vPos[1]));
					jTxtFld_ButtonPosZ.setText(Integer.toString((int)physObj.m_vPos[2]));
					jSldr_ButtonDeepness.setValue(physObj.m_nDeepnessLevel);

					jTxtFld_ButtonNbFramesIdle.setText(Integer.toString(btn.m_vecStates.get(0).m_nMaxFrames));
					jTxtFld_ButtonNbFramesOver.setText(Integer.toString(btn.m_vecStates.get(1).m_nMaxFrames));
					jTxtFld_ButtonNbFramesClicked.setText(Integer.toString(btn.m_vecStates.get(2).m_nMaxFrames));
					jTxtFld_ButtonNbFramesSelected.setText(Integer.toString(btn.m_vecStates.get(3).m_nMaxFrames));

					jTxtFld_ButtonAnimSpeedIdle.setText(Double.toString(btn.m_vecStates.get(0).getAnimationSpeed()));
					jTxtFld_ButtonAnimSpeedOver.setText(Double.toString(btn.m_vecStates.get(1).getAnimationSpeed()));
					jTxtFld_ButtonAnimSpeedClicked.setText(Double.toString(btn.m_vecStates.get(2).getAnimationSpeed()));
					jTxtFld_ButtonAnimSpeedSelected.setText(Double.toString(btn.m_vecStates.get(3).getAnimationSpeed()));
				break;
				case Obj.IDCLASS_Label:
					Label lbl= (Label)physObj;

					if(lbl.m_stFontName.startsWith("file:"))
						lbl.m_stFontName= lbl.m_stFontName.substring(5);

					jTabPane_ObjectProperties.setSelectedIndex(2);
					jTxtFld_LabelID.setText(physObj.getTextID());
					jTxtFld_LabelFont.setText(lbl.m_stFontName);
					jTxtFld_LabelFontSize.setText(Integer.toString(lbl.m_nFontSize));
					jTxtFld_LabelPosX.setText(Integer.toString((int)physObj.m_vPos[0]));
					jTxtFld_LabelPosY.setText(Integer.toString((int)physObj.m_vPos[1]));
					jTxtFld_LabelPosZ.setText(Integer.toString((int)physObj.m_vPos[2]));
					jSldr_LabelDeepness.setValue(physObj.m_nDeepnessLevel);

					jTxtFld_LabelWidth.setText(Integer.toString(lbl.m_recLimit.width));
					jTxtFld_LabelHeight.setText(Integer.toString(lbl.m_recLimit.height));
					jTxtArea_LabelText.setText(lbl.m_stText);
					jSldr_LabelAlignment.setValue(lbl.m_nMode);
					jChkBx_LabelTextField.setSelected(lbl.getSubClassID() == Label.TYPE_TEXTFIELD);
				break;
				default:
					if(!physObj.isKindOf(Obj.IDCLASS_Sprite))
						break;

					Sprite spr= (Sprite)physObj;
					int nClassID= spr.getClassID();

					if(spr.m_stResImage.startsWith("file:"))
						spr.m_stResImage= spr.m_stResImage.substring(5);

					jTabPane_ObjectProperties.setSelectedIndex(3);
					jTxtFld_SpriteImage.setText(spr.m_stResImage);
					jTxtFld_SpriteID.setText(physObj.getTextID());
					jTxtFld_SpritePosX.setText(Integer.toString((int)physObj.m_vPos[0]));
					jTxtFld_SpritePosY.setText(Integer.toString((int)physObj.m_vPos[1]));
					jTxtFld_SpritePosZ.setText(Integer.toString((int)physObj.m_vPos[2]));
					jSldr_SpriteDeepness.setValue(physObj.m_nDeepnessLevel);

					jTxtFld_SpriteDefaultState.setText(Integer.toString(spr.getCurState()));
					jTxtFld_SpriteDefaultFrame.setText(Integer.toString(spr.getCurFrame()));
					jTxtFld_SpriteCustomClassID.setText(physObj.getTextClassID());
					
					MyTableModel tblModel= MyTableModel.gThis;

					tblModel.m_arStateNames.clear();
					tblModel.m_arAnimSpeed.clear();
					tblModel.m_arNbFrames.clear();
					tblModel.fireTableRowsDeleted(0, 0);

					for(State state : spr.m_vecStates){
						tblModel.m_arStateNames.add(state.m_stName);
						tblModel.m_arNbFrames.add(state.m_nMaxFrames);
						tblModel.m_arAnimSpeed.add(state.getAnimationSpeed());
					}
					tblModel.fireTableRowsInserted(0, tblModel.getRowCount());

				break;
			}
		}
	}//GEN-LAST:event_onViewClicked

	private void onViewDragged(MouseEvent evt) {//GEN-FIRST:event_onViewDragged
		if(m_isPanningActive){
			m_curContext.onPanDo(evt.getX() - m_nPosXBase , evt.getY() - m_nPosYBase);
		}else{
			int	nPosXDif= m_nPosXBase - evt.getX();
			int	nPosYDif= m_nPosYBase - evt.getY();
			m_nPosXBase= evt.getX();
			m_nPosYBase= evt.getY();

			PhysObj physObj= m_curContext.getSelectedPhysObj();

			if(physObj == null)
				return;

			if(physObj.isKindOf(Obj.IDCLASS_Background)){
				jTabPane_ObjectProperties.setSelectedIndex(0);
				jTxtFld_BackgroundPosX.setText(Integer.toString((int)(physObj.m_vPos[0]-= nPosXDif)));
				jTxtFld_BackgroundPosY.setText(Integer.toString((int)(physObj.m_vPos[1]-= nPosYDif)));
			}else if(physObj.isKindOf(Obj.IDCLASS_Button)){
				jTabPane_ObjectProperties.setSelectedIndex(1);
				jTxtFld_ButtonPosX.setText(Integer.toString((int)(physObj.m_vPos[0]-= nPosXDif)));
				jTxtFld_ButtonPosY.setText(Integer.toString((int)(physObj.m_vPos[1]-= nPosYDif)));
			}else if(physObj.isKindOf(Obj.IDCLASS_Label)){
				jTabPane_ObjectProperties.setSelectedIndex(2);
				jTxtFld_LabelPosX.setText(Integer.toString((int)(physObj.m_vPos[0]-= nPosXDif)));
				jTxtFld_LabelPosY.setText(Integer.toString((int)(physObj.m_vPos[1]-= nPosYDif)));
			}else if(physObj.isKindOf(Obj.IDCLASS_Sprite)){
				jTabPane_ObjectProperties.setSelectedIndex(3);
				jTxtFld_SpritePosX.setText(Integer.toString((int)(physObj.m_vPos[0]-= nPosXDif)));
				jTxtFld_SpritePosY.setText(Integer.toString((int)(physObj.m_vPos[1]-= nPosYDif)));
			}
		}
	}//GEN-LAST:event_onViewDragged

	private void onBackgroundFilenameBrowse(ActionEvent evt) {//GEN-FIRST:event_onBackgroundFilenameBrowse
		JFileChooser fc= new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG file", "png");

		fc.setFileFilter(filter);
		int returnVal= fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		jTxtFld_BackgroundImage.setText(/*"file:" + */fc.getSelectedFile().getAbsolutePath());
	}//GEN-LAST:event_onBackgroundFilenameBrowse

	private void onBackgroundCreate(ActionEvent evt) {//GEN-FIRST:event_onBackgroundCreate
		Background back= Background.create(Background.TYPE_NORMAL, jTxtFld_BackgroundImage.getText(), null);

		Integer nPosX= Integer.parseInt(jTxtFld_BackgroundPosX.getText());
		Integer nPosY= Integer.parseInt(jTxtFld_BackgroundPosY.getText());
		Integer nPosZ= Integer.parseInt(jTxtFld_BackgroundPosZ.getText());

		back.setPos(nPosX, nPosY, nPosZ);
		back.setTextID(jTxtFld_BackgroundID.getText());
		back.m_nDeepnessLevel= jSldr_BackgroundDeepness.getValue();

		m_curContext.addChild(back);
	}//GEN-LAST:event_onBackgroundCreate

	private void onBackgroundModifiy(ActionEvent evt) {//GEN-FIRST:event_onBackgroundModifiy
		PhysObj physObj= m_curContext.getSelectedPhysObj();

		if(physObj == null)
			return;
		if(!physObj.isKindOf(Obj.IDCLASS_Background))
			return;
		
		Background back= (Background)physObj;

		Integer posX= Integer.parseInt(jTxtFld_BackgroundPosX.getText());
		Integer posY= Integer.parseInt(jTxtFld_BackgroundPosY.getText());
		Integer posZ= Integer.parseInt(jTxtFld_BackgroundPosZ.getText());

		back.m_image= null;
		back.m_stResImage= jTxtFld_BackgroundImage.getText();
		back.m_nDeepnessLevel= jSldr_BackgroundDeepness.getValue();
		back.setPos(posX, posY, posZ);
		back.setTextID(jTxtFld_BackgroundID.getText());
	}//GEN-LAST:event_onBackgroundModifiy

	private void onButtonFilenameBrowse(ActionEvent evt) {//GEN-FIRST:event_onButtonFilenameBrowse
		JFileChooser fc= new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG file", "png");

		fc.setFileFilter(filter);
		int returnVal= fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		jTxtFld_ButtonImage.setText(/*"file:" + */fc.getSelectedFile().getAbsolutePath());
	}//GEN-LAST:event_onButtonFilenameBrowse

	private void onButtonCreate(ActionEvent evt) {//GEN-FIRST:event_onButtonCreate
		String res= jTxtFld_ButtonImage.getText();
		Integer nbFrameIdle= Integer.parseInt(jTxtFld_ButtonNbFramesIdle.getText());
		Integer nbFrameOver= Integer.parseInt(jTxtFld_ButtonNbFramesOver.getText());
		Integer nbFrameSelected= Integer.parseInt(jTxtFld_ButtonNbFramesSelected.getText());
		Integer nbFrameClicked= Integer.parseInt(jTxtFld_ButtonNbFramesClicked.getText());
		Double animSpeedIdle= Double.parseDouble(jTxtFld_ButtonAnimSpeedIdle.getText());
		Double animSpeedOver= Double.parseDouble(jTxtFld_ButtonAnimSpeedOver.getText());
		Double animSpeedSelected= Double.parseDouble(jTxtFld_ButtonAnimSpeedSelected.getText());
		Double animSpeedClicked= Double.parseDouble(jTxtFld_ButtonAnimSpeedClicked.getText());
		Integer posX= Integer.parseInt(jTxtFld_ButtonPosX.getText());
		Integer posY= Integer.parseInt(jTxtFld_ButtonPosY.getText());
		Integer posZ= Integer.parseInt(jTxtFld_ButtonPosZ.getText());

		Button button= new Button(res, nbFrameIdle, animSpeedIdle, nbFrameOver, animSpeedOver, nbFrameSelected, animSpeedSelected, nbFrameClicked, animSpeedClicked);

		button.setPos(posX, posY, posZ);
		button.setTextID(jTxtFld_ButtonID.getText());
		button.m_nDeepnessLevel= jSldr_ButtonDeepness.getValue();

		m_curContext.addChild(button);
	}//GEN-LAST:event_onButtonCreate

	private void onButtonModify(ActionEvent evt) {//GEN-FIRST:event_onButtonModify
		PhysObj physObj= m_curContext.getSelectedPhysObj();

		if(physObj == null)
			return;
		if(!physObj.isKindOf(Obj.IDCLASS_Button))
			return;
		
		Button button= (Button)physObj;

		String res= jTxtFld_ButtonImage.getText();
		Integer nbFrameIdle= Integer.parseInt(jTxtFld_ButtonNbFramesIdle.getText());
		Integer nbFrameOver= Integer.parseInt(jTxtFld_ButtonNbFramesOver.getText());
		Integer nbFrameSelected= Integer.parseInt(jTxtFld_ButtonNbFramesSelected.getText());
		Integer nbFrameClicked= Integer.parseInt(jTxtFld_ButtonNbFramesClicked.getText());
		Double animSpeedIdle= Double.parseDouble(jTxtFld_ButtonAnimSpeedIdle.getText());
		Double animSpeedOver= Double.parseDouble(jTxtFld_ButtonAnimSpeedOver.getText());
		Double animSpeedSelected= Double.parseDouble(jTxtFld_ButtonAnimSpeedSelected.getText());
		Double animSpeedClicked= Double.parseDouble(jTxtFld_ButtonAnimSpeedClicked.getText());
		Integer posX= Integer.parseInt(jTxtFld_ButtonPosX.getText());
		Integer posY= Integer.parseInt(jTxtFld_ButtonPosY.getText());
		Integer posZ= Integer.parseInt(jTxtFld_ButtonPosZ.getText());

		button.m_vecStates.get(0).m_nMaxFrames= nbFrameIdle;
		button.m_vecStates.get(0).m_nSpeed= (int)(animSpeedIdle*32);
		button.m_vecStates.get(1).m_nMaxFrames= nbFrameOver;
		button.m_vecStates.get(1).m_nSpeed= (int)(animSpeedOver*32);
		button.m_vecStates.get(2).m_nMaxFrames= nbFrameSelected;
		button.m_vecStates.get(2).m_nSpeed= (int)(animSpeedSelected*32);
		button.m_vecStates.get(3).m_nMaxFrames= nbFrameClicked;
		button.m_vecStates.get(3).m_nSpeed= (int)(animSpeedClicked*32);

		button.m_stResImage= res;
		button.m_image= null;
		button.m_nDeepnessLevel= jSldr_ButtonDeepness.getValue();
		button.setPos(posX, posY, posZ);
		button.setTextID(jTxtFld_ButtonID.getText());
	}//GEN-LAST:event_onButtonModify

	private void onLabelBrowseFont(ActionEvent evt) {//GEN-FIRST:event_onLabelBrowseFont
		JFileChooser fc= new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TrueType Font", "ttf");

		fc.setFileFilter(filter);
		int returnVal= fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		jTxtFld_LabelFont.setText(/*"file:" + */fc.getSelectedFile().getAbsolutePath());
	}//GEN-LAST:event_onLabelBrowseFont

	private void onLabelCreate(ActionEvent evt) {//GEN-FIRST:event_onLabelCreate
		int		nAlignmentValue	= jSldr_LabelAlignment.getValue();
		String	stLabelFont		= jTxtFld_LabelFont.getText();
		String	stLabelTxt		= jTxtArea_LabelText.getText();
		boolean isMultiline		= stLabelTxt.contains("\n");
		boolean isTextField		= jChkBx_LabelTextField.isSelected();

		Integer nFontSize= Integer.parseInt(jTxtFld_LabelFontSize.getText());
		Integer posX	= Integer.parseInt(jTxtFld_LabelPosX.getText());
		Integer posY	= Integer.parseInt(jTxtFld_LabelPosY.getText());
		Integer posZ	= Integer.parseInt(jTxtFld_LabelPosZ.getText());
		Integer width	= Integer.parseInt(jTxtFld_LabelWidth.getText());
		Integer height	= Integer.parseInt(jTxtFld_LabelHeight.getText());

		Rectangle recLabel= null;

		if(nAlignmentValue == 0)
			recLabel= new Rectangle(0, -(height/2), width, height);
		else if(nAlignmentValue == 1)
			recLabel= new Rectangle(-(width/2), -(height/2), width, height);
		else if(nAlignmentValue == 2)
			recLabel= new Rectangle(-width, -(height/2), width, height);

		Label lblNew= Label.create(isTextField?Label.TYPE_TEXTFIELD:Label.TYPE_NORMAL, stLabelFont, nFontSize, stLabelTxt, nAlignmentValue, isMultiline, recLabel);
		lblNew.setPos(posX, posY, posZ);
		lblNew.setTextID(jTxtFld_LabelID.getText());
		lblNew.m_nDeepnessLevel= jSldr_LabelDeepness.getValue();

		m_curContext.addChild(lblNew);
	}//GEN-LAST:event_onLabelCreate

	private void onLabelModify(ActionEvent evt) {//GEN-FIRST:event_onLabelModify
		PhysObj physObj= m_curContext.getSelectedPhysObj();

		if(physObj == null)
			return;
		if(!physObj.isKindOf(Obj.IDCLASS_Label))
			return;

		m_curContext.removeChild(physObj);
		onLabelCreate(evt);
	}//GEN-LAST:event_onLabelModify

	private void onSpriteBrowseImage(ActionEvent evt) {//GEN-FIRST:event_onSpriteBrowseImage
		JFileChooser fc= new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG file", "png");

		fc.setFileFilter(filter);
		int returnVal= fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		jTxtFld_SpriteImage.setText(/*"file:" + */fc.getSelectedFile().getAbsolutePath());
	}//GEN-LAST:event_onSpriteBrowseImage

	private void onSpriteButtonAddState(ActionEvent evt) {//GEN-FIRST:event_onSpriteButtonAddState
		MyTableModel tblModel=	MyTableModel.gThis;

		int nNbRow= tblModel.m_arStateNames.size();
		tblModel.m_arStateNames.add("StateName" + nNbRow);
		tblModel.m_arNbFrames.add(8);
		tblModel.m_arAnimSpeed.add(0.1);
		
		tblModel.fireTableRowsInserted(nNbRow, tblModel.m_arStateNames.size());
	}//GEN-LAST:event_onSpriteButtonAddState

	private void onSpriteButtonRemoveState(ActionEvent evt) {//GEN-FIRST:event_onSpriteButtonRemoveState
		int nRowSelected= jTbl_SpriteStateMatrix.getSelectedRow();

		if(nRowSelected == -1)
			return;
		
		MyTableModel tblModel=	MyTableModel.gThis;

		tblModel.m_arStateNames.remove(nRowSelected);
		tblModel.m_arNbFrames.remove(nRowSelected);
		tblModel.m_arAnimSpeed.remove(nRowSelected);
		
		tblModel.fireTableRowsDeleted(nRowSelected-1, tblModel.m_arStateNames.size());
	}//GEN-LAST:event_onSpriteButtonRemoveState

	private void onSpriteButtonCreate(ActionEvent evt) {//GEN-FIRST:event_onSpriteButtonCreate
		MyTableModel tblModel=	MyTableModel.gThis;

		Integer posX	= Integer.parseInt(jTxtFld_SpritePosX.getText());
		Integer posY	= Integer.parseInt(jTxtFld_SpritePosY.getText());
		Integer posZ	= Integer.parseInt(jTxtFld_SpritePosZ.getText());
		int		nClassID= Obj.IDCLASS_GAME | Obj.IDCLASS_Sprite;
		Sprite newSprite= new Sprite(jTxtFld_SpriteImage.getText());

		newSprite.setTextID(jTxtFld_SpriteID.getText());
		newSprite.setPos(posX, posY, posZ);
		newSprite.setClassID(nClassID);
		newSprite.setClassID(jTxtFld_SpriteCustomClassID.getText());

		for(int i= 0; i < tblModel.getRowCount(); ++i){
			String	stStateName	= (String)tblModel.getValueAt(i, 0);
			Integer	nNbFrame	= (Integer)tblModel.getValueAt(i, 1);
			Double	fAnimSpeed	= (Double)tblModel.getValueAt(i, 2);

			newSprite.addState(newSprite.new State(stStateName, nNbFrame, fAnimSpeed));
		}

		newSprite.setCurState(Integer.parseInt(jTxtFld_SpriteDefaultState.getText()));
		newSprite.setCurFrame(Integer.parseInt(jTxtFld_SpriteDefaultFrame.getText()));
		newSprite.m_nDeepnessLevel= jSldr_SpriteDeepness.getValue();

		m_curContext.addChild(newSprite);		
	}//GEN-LAST:event_onSpriteButtonCreate

	private void onSpriteButtonModify(ActionEvent evt) {//GEN-FIRST:event_onSpriteButtonModify
		PhysObj physObj= m_curContext.getSelectedPhysObj();

		if(physObj == null)
			return;
		if(!physObj.isKindOf(Obj.IDCLASS_Sprite))
			return;

		Sprite spr= (Sprite)physObj;
		MyTableModel tblModel=	MyTableModel.gThis;

		Integer posX	= Integer.parseInt(jTxtFld_SpritePosX.getText());
		Integer posY	= Integer.parseInt(jTxtFld_SpritePosY.getText());
		Integer posZ	= Integer.parseInt(jTxtFld_SpritePosZ.getText());
		int		nClassID= Obj.IDCLASS_GAME | Obj.IDCLASS_Sprite;

		spr.setTextID(jTxtFld_SpriteID.getText());
		spr.setPos(posX, posY, posZ);
		spr.setClassID(nClassID);
		spr.setClassID(jTxtFld_SpriteCustomClassID.getText());

		spr.m_vecStates.clear();
		spr.m_image= null;
		spr.m_stResImage= jTxtFld_SpriteImage.getText();
		spr.m_nDeepnessLevel= jSldr_SpriteDeepness.getValue();

		for(int i= 0; i < tblModel.getRowCount(); ++i){
			String	stStateName	= (String)tblModel.getValueAt(i, 0);
			Integer	nNbFrame	= (Integer)tblModel.getValueAt(i, 1);
			Double	fAnimSpeed	= (Double)tblModel.getValueAt(i, 2);

			spr.addState(spr.new State(stStateName, nNbFrame, fAnimSpeed));
		}

		spr.setCurState(Integer.parseInt(jTxtFld_SpriteDefaultState.getText()));
		spr.setCurFrame(Integer.parseInt(jTxtFld_SpriteDefaultFrame.getText()));
	}//GEN-LAST:event_onSpriteButtonModify

	private void onToolbarButtonScreenshot(ActionEvent evt) {//GEN-FIRST:event_onToolbarButtonScreenshot
		JFileChooser fc= new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Targa Image file", "tga");

		fc.setFileFilter(filter);
		int returnVal= fc.showSaveDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		m_curContext.m_pendingScreenshotFile= fc.getSelectedFile();
	}//GEN-LAST:event_onToolbarButtonScreenshot

	private void ValidateToolbarIcons(){
		jBtMove.setSelected(m_isPanningActive);
		jBtRemove.setSelected(m_isDeleteActive);
	}

	private void onViewMove(MouseEvent evt) {//GEN-FIRST:event_onViewMove
		m_isPanningActive= evt.isControlDown();
		ValidateToolbarIcons();
	}//GEN-LAST:event_onViewMove

	private void onViewDown(MouseEvent evt) {//GEN-FIRST:event_onViewDown
		if(m_isPanningActive){
			m_nPosXBase= evt.getX();
			m_nPosYBase= evt.getY();
			m_curContext.onPanStart();
		}
	}//GEN-LAST:event_onViewDown

	private void onViewZoom(MouseWheelEvent evt) {//GEN-FIRST:event_onViewZoom
		Camera cam= Camera.getCur(Camera.TYPE_2D);
		cam.m_fZoom-= (double)evt.getWheelRotation() / 10.0;
	}//GEN-LAST:event_onViewZoom

	private void onButtonDelete(ActionEvent evt) {//GEN-FIRST:event_onButtonDelete
		m_isDeleteActive= !m_isDeleteActive;
		ValidateToolbarIcons();
	}//GEN-LAST:event_onButtonDelete

	private void onToolbarButtonNew(ActionEvent evt) {//GEN-FIRST:event_onToolbarButtonNew
		m_curContext.removeAllChilds();
	}//GEN-LAST:event_onToolbarButtonNew

	private void onToolbarButtonLoadJava(ActionEvent evt) {//GEN-FIRST:event_onToolbarButtonLoadJava
		JFileChooser fc= new JFileChooser(m_curDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Context Design file", "java");

		fc.setFileFilter(filter);
		int returnVal= fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		m_curDirectory= fc.getSelectedFile().getAbsolutePath();

		m_curDirectory= m_curDirectory.substring(0, m_curDirectory.lastIndexOf(File.separator));
		m_curContext.onLoadJava(fc.getSelectedFile().getAbsolutePath());
	}//GEN-LAST:event_onToolbarButtonLoadJava

	private void onToolbarButtonSaveJava(ActionEvent evt) {//GEN-FIRST:event_onToolbarButtonSaveJava
		JFileChooser fc= new JFileChooser(m_curDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Java file", "java");

		fc.setFileFilter(filter);
		int returnVal= fc.showSaveDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION)
			return;

		m_curDirectory= fc.getSelectedFile().getAbsolutePath();
		m_curDirectory= m_curDirectory.substring(0, m_curDirectory.lastIndexOf(File.separator));
		m_curContext.onSaveJava(fc.getSelectedFile().getAbsolutePath());
	}//GEN-LAST:event_onToolbarButtonSaveJava

    private GLCapabilities createGLCapabilites() {
        GLCapabilities capabilities = new GLCapabilities();
        capabilities.setHardwareAccelerated(true);

        // try to enable 2x anti aliasing - should be supported on most hardware
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);
        
        return capabilities;
    }

    public static void main(String args[]){
        // Run this in the AWT event thread to prevent deadlocks and race conditions
        EventQueue.invokeLater(new Runnable(){
            public void run() {

                // switch to system l&f for native font rendering etc.
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "can not enable system look and feel", ex);
                }

                ContextEditor frame = new ContextEditor();
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jBtMove;
    private JButton jBtRemove;
    private JButton jBtToolbar_New;
    private JButton jBtToolbar_OpenJava;
    private JButton jBtToolbar_SaveJava;
    private JButton jBtZoomIn;
    private JButton jBtZoomOut;
    private JButton jBt_BackgroundBrowse;
    private JButton jBt_BackgroundCreate;
    private JButton jBt_BackgroundModify;
    private JButton jBt_ButtonBrowse;
    private JButton jBt_ButtonCreate;
    private JButton jBt_ButtonModify;
    private JButton jBt_LabelBrowse;
    private JButton jBt_LabelCreate;
    private JButton jBt_LabelModify;
    private JButton jBt_SaveScreenshot;
    private JButton jBt_SpriteAddState;
    private JButton jBt_SpriteBrowseImage;
    private JButton jBt_SpriteCreate;
    private JButton jBt_SpriteModify;
    private JButton jBt_SpriteRemoveState;
    private JCheckBox jChkBx_LabelTextField;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel24;
    private JLabel jLabel25;
    private JLabel jLabel26;
    private JLabel jLabel27;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPnl_Background;
    private JPanel jPnl_Button;
    private JPanel jPnl_Label;
    private JPanel jPnl_Sprite;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private Separator jSeparator1;
    private JSeparator jSeparator2;
    private Separator jSeparator3;
    private JSlider jSldr_BackgroundDeepness;
    private JSlider jSldr_ButtonDeepness;
    private JSlider jSldr_LabelAlignment;
    private JSlider jSldr_LabelDeepness;
    private JSlider jSldr_SpriteDeepness;
    private JTabbedPane jTabPane_ObjectProperties;
    private JTable jTbl_SpriteStateMatrix;
    private JToolBar jToolBar;
    private JTextArea jTxtArea_LabelText;
    private JTextField jTxtFld_BackgroundID;
    private JTextField jTxtFld_BackgroundImage;
    private JTextField jTxtFld_BackgroundPosX;
    private JTextField jTxtFld_BackgroundPosY;
    private JTextField jTxtFld_BackgroundPosZ;
    private JTextField jTxtFld_ButtonAnimSpeedClicked;
    private JTextField jTxtFld_ButtonAnimSpeedIdle;
    private JTextField jTxtFld_ButtonAnimSpeedOver;
    private JTextField jTxtFld_ButtonAnimSpeedSelected;
    private JTextField jTxtFld_ButtonID;
    private JTextField jTxtFld_ButtonImage;
    private JTextField jTxtFld_ButtonNbFramesClicked;
    private JTextField jTxtFld_ButtonNbFramesIdle;
    private JTextField jTxtFld_ButtonNbFramesOver;
    private JTextField jTxtFld_ButtonNbFramesSelected;
    private JTextField jTxtFld_ButtonPosX;
    private JTextField jTxtFld_ButtonPosY;
    private JTextField jTxtFld_ButtonPosZ;
    private JTextField jTxtFld_LabelFont;
    private JTextField jTxtFld_LabelFontSize;
    private JTextField jTxtFld_LabelHeight;
    private JTextField jTxtFld_LabelID;
    private JTextField jTxtFld_LabelPosX;
    private JTextField jTxtFld_LabelPosY;
    private JTextField jTxtFld_LabelPosZ;
    private JTextField jTxtFld_LabelWidth;
    private JTextField jTxtFld_SpriteCustomClassID;
    private JTextField jTxtFld_SpriteDefaultFrame;
    private JTextField jTxtFld_SpriteDefaultState;
    private JTextField jTxtFld_SpriteID;
    private JTextField jTxtFld_SpriteImage;
    private JTextField jTxtFld_SpritePosX;
    private JTextField jTxtFld_SpritePosY;
    private JTextField jTxtFld_SpritePosZ;
    private GLCanvas m_GLCanvas;
    // End of variables declaration//GEN-END:variables

}
