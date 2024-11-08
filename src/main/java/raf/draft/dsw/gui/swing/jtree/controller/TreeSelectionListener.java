package raf.draft.dsw.gui.swing.jtree.controller;

import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;

public class TreeSelectionListener implements javax.swing.event.TreeSelectionListener {

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        //ova metoda daje putanju do selektovanpg cvora i ispisuje ;)
        TreePath path = e.getPath();
        DraftTreeItem treeItemSelected = (DraftTreeItem)path.getLastPathComponent();
        System.out.println("Selektovan čvor: " + treeItemSelected.getDraftNode().getIme());
        System.out.println("getPath: "+ e.getPath());
    }
}
