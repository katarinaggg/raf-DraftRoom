package raf.draft.dsw.gui.swing.jtree.controller;

import com.sun.tools.javac.Main;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.ProjectView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectedProjectListener extends MouseAdapter {

    // stablo iz kojeg izvlacimo selektovani objekat
    private final JTree tree;

    public SelectedProjectListener(JTree tree) {
        this.tree = tree;
    }

    // sta se desava na dvoklik
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            //System.out.println("Dvostruki klik detektovan!");

            TreePath path = tree.getPathForLocation(e.getX(), e.getY());

            if (path != null) {
                //DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                DraftTreeItem selectedNode = (DraftTreeItem) path.getLastPathComponent();

                // Provera da li je čvor instanca Project
                if(selectedNode.getDraftNode() instanceof Project) {
                    //System.out.println("JESTE PROJEKAT");
                    // ako smo kliknuli na projekat zovemo metodu iz MainFrame-a koja pravi ProjectView
                    // i prosledjujemo selektovani Project
                    MainFrame.getInstance().displayProjectView((Project) selectedNode.getDraftNode());
                }
            }
        }
    }
}
