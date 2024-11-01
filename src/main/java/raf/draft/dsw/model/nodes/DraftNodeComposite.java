package raf.draft.dsw.model.nodes;


import java.util.ArrayList;
import java.util.List;

public abstract class DraftNodeComposite extends DraftNode {

    private List<DraftNode> children = new ArrayList<>();

    public abstract void addChild(DraftNode n);
    public abstract void removeChild(DraftNode n);

}
