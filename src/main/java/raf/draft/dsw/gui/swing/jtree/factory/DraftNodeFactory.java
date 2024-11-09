package raf.draft.dsw.gui.swing.jtree.factory;

import raf.draft.dsw.model.nodes.DraftNode;

public abstract class DraftNodeFactory {

    public abstract DraftNode createDraftNode(DraftNode parent);
}
