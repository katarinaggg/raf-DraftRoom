package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.jtree.ChildFactory;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

@Getter
public class Project extends DraftNodeComposite {

    //private String ime;

    // ime kreatora projekta
    private String autorProjekta;
    // putanja do foldera
    private String path;

    public Project(String ime, DraftNode parent) {
        super(ime, parent);
    }


    @Override
    public void addChild(DraftNode child) {
    }

    @Override
    public void removeChild(DraftNode n) {

    }
}
