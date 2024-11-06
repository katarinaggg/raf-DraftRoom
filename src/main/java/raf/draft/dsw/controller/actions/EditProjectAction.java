package raf.draft.dsw.controller.actions;

import raf.draft.dsw.gui.swing.EditProject;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

public class EditProjectAction extends AbstractRoomAction{

    public EditProjectAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/editProject.png"));
        putValue(NAME, "Edit Project");
        putValue(SHORT_DESCRIPTION, "Edit Project");
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
        // uzimamo selektovan Item
        DraftTreeItem selected = (DraftTreeItem) MainFrame.getInstance().getDraftTree().getSelectedItem();
        // ako nije project nece se uopste otvoriti prozor
        if(!(selected.getDraftNode() instanceof Project)){
            // treba da se posalje poruka
        }

        // da li smem da ga prosledim ovde?
        EditProject editProject = new EditProject();
        editProject.setVisible(true);
    }
}
