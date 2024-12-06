package raf.draft.dsw.gui.swing.jtree;

import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.gui.swing.jtree.view.JTreeView;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.ProjectExplorer;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

public interface DraftTree {

    public JTreeView generate(ProjectExplorer projectExplorer);
    public void addChild(DraftTreeItem parent);
    public void removeChild(DraftTreeItem node);
    public DraftTreeItem getSelectedItem();

    // metoda za dodavanje namestaja na Jtree
    public void addRoomChild(DraftTreeItem parent, RoomElement child);

    public void refreshTree();

    public DraftTreeItem findTreeItem(DraftNode targetNode);
}
