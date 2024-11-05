package raf.draft.dsw.gui.swing.jtree;

import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.gui.swing.jtree.view.JTreeView;
import raf.draft.dsw.model.structures.ProjectExplorer;

import javax.swing.*;

public interface DraftTree {

    public JTreeView generate(ProjectExplorer projectExplorer);
    public void addChild(DraftTreeItem parent);
    public void removeChild();
    public DraftTreeItem getSelectedItem();
}
