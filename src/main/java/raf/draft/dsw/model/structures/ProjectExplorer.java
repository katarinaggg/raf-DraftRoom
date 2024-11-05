package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

@Data
public class ProjectExplorer extends DraftNodeComposite {

    public ProjectExplorer(String ime){
        super(ime, null);
    }

    @Override
    public void addChild(DraftNode child) {
        if (child != null &&  child instanceof Project){
            Project project = (Project) child;
            if (!this.getChildren().contains(project)){
                this.getChildren().add(project);
            }
        }
    }

    @Override
    public void removeChild(DraftNode n) {

    }
}
