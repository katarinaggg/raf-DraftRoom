package raf.draft.dsw.controller.actions.SideToolBarActions;

import com.sun.tools.javac.Main;
import raf.draft.dsw.gui.swing.AddElementFrame;
import raf.draft.dsw.gui.swing.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

public class CopyPasteAction extends AbstractAction {

    public CopyPasteAction() {
        //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/copypaste.png"));
        putValue(NAME, "Copy/Paste");
        putValue(SHORT_DESCRIPTION, "Copy/Paste");
    }

    public Icon loadIcon(String path) {
        Icon icon = null;
        URL ImageURL = getClass().getResource(path);
        if(ImageURL != null)
        {
            Image img = new ImageIcon(ImageURL).getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            icon = new ImageIcon(newImg);
        }
        else
        {
            System.err.println("File " + path + " not found");
        }
        return icon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getProjectView().startCopyPasteState();
    }
}
