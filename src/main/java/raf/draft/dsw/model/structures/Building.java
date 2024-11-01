package raf.draft.dsw.model.structures;

import lombok.Data;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

@Data
public class Building extends DraftNodeComposite {


    @Override
    public void addChild(DraftNode n) {

    }

    @Override
    public void removeChild(DraftNode n) {

    }
}
