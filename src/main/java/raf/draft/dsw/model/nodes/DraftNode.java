package raf.draft.dsw.model.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public abstract class DraftNode {

    private String ime;
    private DraftNode parent;

    public DraftNode(String ime, DraftNode parent) {
        this.ime = ime;
        this.parent = parent;
    }
}
