package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;


public class Building extends DraftNodeComposite {


    public Building(String ime, DraftNode parent) {
        super(ime, parent);
    }

    @Override
    public void addChild(DraftNode n) {

    }

    @Override
    public void removeChild(DraftNode n) {

    }
}
