package raf.draft.dsw.gui.swing.jtree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

import javax.swing.tree.DefaultMutableTreeNode;

@Data@NoArgsConstructor@AllArgsConstructor
public class DraftTreeItem extends DefaultMutableTreeNode {

    DraftNode draftNode;


    @Override
    public String toString() {
        return draftNode.getIme();
    }

    public void setIme(String ime){
        this.draftNode.setIme(ime);
    }
}
