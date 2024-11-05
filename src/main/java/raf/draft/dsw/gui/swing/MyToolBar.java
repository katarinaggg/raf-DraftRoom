package raf.draft.dsw.gui.swing;

import javax.swing.*;

public class MyToolBar extends JToolBar {
    public MyToolBar(){
        super(HORIZONTAL);
        setFloatable(false);

        add(MainFrame.getInstance().getActionManager().getAddNodeAction());
        add(MainFrame.getInstance().getActionManager().getAbouUsAction());
        add(MainFrame.getInstance().getActionManager().getExitAction());
    }
}
