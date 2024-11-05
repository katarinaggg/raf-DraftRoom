package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;

@Getter
public class Room extends DraftNode {

    public Room(String ime, DraftNode parent) {
        super(ime, parent);
    }
}
