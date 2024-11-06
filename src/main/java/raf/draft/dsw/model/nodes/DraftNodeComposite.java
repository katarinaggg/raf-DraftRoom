package raf.draft.dsw.model.nodes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter
public abstract class DraftNodeComposite extends DraftNode {

    private List<DraftNode> children = new ArrayList<>();

    public DraftNodeComposite(String ime, DraftNode parent) {
        super(ime, parent);
    }


    public abstract void addChild(DraftNode n);

    public abstract void removeChild(DraftNode n);

}
