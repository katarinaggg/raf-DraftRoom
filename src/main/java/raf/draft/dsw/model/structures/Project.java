package raf.draft.dsw.model.structures;

import lombok.Data;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

@Data
public class Project extends DraftNodeComposite {

    // naziv projekta
    private String ime;
    // ime kreatora projekta
    private String autorProjekta;
    // putanja do foldera
    private String path;


    @Override
    public void addChild(DraftNode n) {

    }

    @Override
    public void removeChild(DraftNode n) {

    }
}
