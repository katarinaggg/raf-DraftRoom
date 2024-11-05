package raf.draft.dsw.controller.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
@Data
public class AddNodeAction extends AbstractRoomAction{

    public AddNodeAction(){
        //bitno
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/plus.png"));
        putValue(NAME, "Add Node");
        putValue(SHORT_DESCRIPTION, "Add Node");
    }
    @Override
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
        DraftTreeItem selected = (DraftTreeItem) MainFrame.getInstance().getDraftTree().getSelectedItem();
        MainFrame.getInstance().getDraftTree().addChild(selected);
    }
}
