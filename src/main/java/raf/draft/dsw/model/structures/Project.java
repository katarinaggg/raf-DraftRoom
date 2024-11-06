package raf.draft.dsw.model.structures;

import lombok.*;
import raf.draft.dsw.gui.swing.jtree.ChildFactory;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

@Getter@Setter
public class Project extends DraftNodeComposite {

    //private String ime;     vec ima kao polje u DraftNode :')

    // ime kreatora projekta
    private String autorProjekta;
    // putanja do foldera
    private String path;

    public Project(String ime, DraftNode parent) {
        super(ime, parent);
    }


    @Override
    public void addChild(DraftNode child) {
        if (child != null &&  child instanceof Building){
            Building building = (Building) child;
            if (!this.getChildren().contains(building)){
                this.getChildren().add(building);
            }
        }
        else if (child != null &&  child instanceof Room) {
            Room room = (Room) child;
            if (!this.getChildren().contains(room)){
                this.getChildren().add(room);
            }
        }
    }

    @Override
    public void removeChild(DraftNode child) {
        if(child!=null && (child instanceof Building || child instanceof Room)){
            this.getChildren().remove(child);
        }
    }
}
