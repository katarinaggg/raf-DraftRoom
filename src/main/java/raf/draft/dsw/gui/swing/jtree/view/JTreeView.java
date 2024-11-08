package raf.draft.dsw.gui.swing.jtree.view;

import raf.draft.dsw.gui.swing.jtree.controller.SelectedProjectListener;
import raf.draft.dsw.gui.swing.jtree.controller.TreeCellEditor;
import raf.draft.dsw.gui.swing.jtree.controller.TreeSelectionListener;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTreeView extends JTree {


    // konstruktor u koji dodajemo sve iz Jtree komponente
    public JTreeView(DefaultTreeModel defaultTreeModel){

        // Listener koji "slusa" ovo stablo
        // dodajemo nasu klasu koja obradjuje doogadjaj
        addMouseListener(new SelectedProjectListener(this));

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
