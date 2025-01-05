package raf.draft.dsw.gui.swing;

import com.sun.tools.javac.Main;

import javax.swing.*;

public class MyToolBar extends JToolBar {
    public MyToolBar(){
        super(HORIZONTAL);
        setFloatable(false);

        add(MainFrame.getInstance().getActionManager().getAddNodeAction());
        add(MainFrame.getInstance().getActionManager().getRemoveNodeAction());
        add(MainFrame.getInstance().getActionManager().getEditProjectAction());
        add(MainFrame.getInstance().getActionManager().getUndoAction());
        add(MainFrame.getInstance().getActionManager().getRedoAction());
      

        add(MainFrame.getInstance().getActionManager().getSaveRoomAction());
        add(MainFrame.getInstance().getActionManager().getOpenRoomAction());
        add(MainFrame.getInstance().getActionManager().getOrganizeRoomAction());
        add(MainFrame.getInstance().getActionManager().getOpenProjectAction());
        add(MainFrame.getInstance().getActionManager().getSaveProjectAction());
        add(MainFrame.getInstance().getActionManager().getSaveAsAction());
        add(MainFrame.getInstance().getActionManager().getAbouUsAction());
        add(MainFrame.getInstance().getActionManager().getExitAction());
    }
}
