package raf.draft.dsw.gui.swing.jtree;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.gui.swing.jtree.view.JTreeView;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.ProjectExplorer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.Random;

public class DraftTreeImplementation implements DraftTree{

    ///ova klasa radi kao 'controller'

    //na osnovu ova dva polja cemo graditi node-ove
    private JTreeView jTreeView;
    private DefaultTreeModel defaultTreeModel;


    //zvacemo je u MainFrame-u : bice JTreeView projectExplorer = DraftTree.generate() jer na projectExplorer mi dodajmo sve, on je rooot
    //tj, pravimo stablo, izvucemo ga iz DraftRoomExplorerImplementation-a
    @Override
    public JTreeView generate(ProjectExplorer projectExplorer) {
        DraftTreeItem root = new DraftTreeItem(projectExplorer);
        defaultTreeModel = new DefaultTreeModel(root);
        jTreeView = new JTreeView(defaultTreeModel);
        return jTreeView;
    }

    @Override
    public void addChild(DraftTreeItem parent) {
        // prosledjujemo samo parenta zato sto cemo znaci na osnovu njega koji tip deteta treba da pravimo

        //samo objekti klase DraftNodeComposite mogu da imaju decu
        //ako probamo da dodamo na nekom drugom tipu objekta decu ne moze ( npr. Room ne moze da ima decu)
        if (!(parent.getDraftNode() instanceof DraftNodeComposite))
            return;

        DraftNode child = createChild(parent.getDraftNode());

        //dodajemo u view
        parent.add(new DraftTreeItem(child));

        //dodajemo u model
        ((DraftNodeComposite) parent.getDraftNode()).addChild(child);
        jTreeView.expandPath(jTreeView.getSelectionPath());
        SwingUtilities.updateComponentTreeUI(jTreeView);
    }


    public DraftNode createChild(DraftNode parent){

        ChildFactory factory = new ChildFactory();

        return factory.createChild(parent);
    }
    @Override
    public void removeChild() {

    }

    @Override
    public DraftTreeItem getSelectedItem() {
        return (DraftTreeItem) jTreeView.getLastSelectedPathComponent();
    }
}
