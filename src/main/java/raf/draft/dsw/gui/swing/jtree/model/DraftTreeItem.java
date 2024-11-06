package raf.draft.dsw.gui.swing.jtree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.structures.Project;

import javax.swing.tree.DefaultMutableTreeNode;


public class DraftTreeItem extends DefaultMutableTreeNode {

    DraftNode draftNode;

    public DraftTreeItem(DraftNode draftNode) {
        this.draftNode = draftNode;
    }

    // da se u Jtree ispisuje samo ime a ne ceo path
    @Override
    public String toString() {
        return draftNode.getIme();
    }

    // ovde smemo da imamo setIme koje cemo da koristimo jer ne pristupamo direktno modelu
    public void setIme(String ime){
        this.draftNode.setIme(ime);
    }

    public void setAutor(String autor){
        if(this.draftNode instanceof Project){
            ((Project) this.draftNode).setAutorProjekta(autor);
        }
    }

    public void setPath(String path){
        if(this.draftNode instanceof Project){
            ((Project) this.draftNode).setPath(path);
        }
    }

    public DraftNode getDraftNode() {
        return draftNode;
    }

    public void setDraftNode(DraftNode draftNode) {
        this.draftNode = draftNode;
    }

}
