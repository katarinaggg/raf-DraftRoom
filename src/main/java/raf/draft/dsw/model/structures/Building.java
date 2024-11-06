package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

import java.awt.*;


public class Building extends DraftNodeComposite {

    // treba dodati polje za boju

    public Building(String ime, DraftNode parent) {
        super(ime, parent);
    }

    @Override
    public void addChild(DraftNode child) {
        if (child != null &&  child instanceof Room){
            Room room = (Room) child;
            if (!this.getChildren().contains(room)){
                this.getChildren().add(room);
            }
        }
    }

    @Override
    public void removeChild(DraftNode child) {
        if(child != null &&  child instanceof Room){
            this.getChildren().remove(child);
        }
    }
}
