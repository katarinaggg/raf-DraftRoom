package raf.draft.dsw.model.nodes;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter@NoArgsConstructor
public abstract class DraftNodeComposite extends DraftNode {

    private List<DraftNode> children = new ArrayList<>();

    public DraftNodeComposite(String ime, DraftNode parent) {
        super(ime, parent);
    }


    public abstract void addChild(DraftNode child);

    public abstract void removeChild(DraftNode child);

}
