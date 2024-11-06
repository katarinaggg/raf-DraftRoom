package raf.draft.dsw.gui.swing.jtree.view;

import raf.draft.dsw.gui.swing.jtree.controller.TreeCellEditor;
import raf.draft.dsw.gui.swing.jtree.controller.TreeSelectionListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class JTreeView extends JTree {

    // konstruktor u koji dodajemo sve iz Jtree komponente
    public JTreeView(DefaultTreeModel defaultTreeModel){

        //dodajemo defaultTreeModel jer nam je tu celo stablo (vec postojeca klasa)
        //item ne dodajemo jer je to krsenje MVC
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
