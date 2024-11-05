package raf.draft.dsw.gui.swing.jtree.view;

import raf.draft.dsw.gui.swing.jtree.controller.TreeCellEditor;
import raf.draft.dsw.gui.swing.jtree.controller.TreeSelectionListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class JTreeView extends JTree {
    public JTreeView(DefaultTreeModel defaultTreeModel){

        //dodajemo defaultTreeModel jer nam je tu celo stablo
        //item ne dodajemo jer je top krsenje MVC
        setModel(defaultTreeModel);

        TreeCellRenderer treeCellRenderer = new TreeCellRenderer();

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener();
        addTreeSelectionListener(treeSelectionListener);

        TreeCellEditor treeCellEditor = new TreeCellEditor(this,treeCellRenderer);
        setCellEditor(treeCellEditor);

        setCellRenderer(treeCellRenderer);
        setEditable(true);
    }
}
