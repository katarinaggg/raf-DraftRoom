package raf.draft.dsw.gui.swing.jtree;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.jtree.factory.ChildFactory;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.gui.swing.jtree.view.JTreeView;
import raf.draft.dsw.model.messages.MessageType;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.structures.ProjectExplorer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.time.LocalDateTime;


public class DraftTreeImplementation implements DraftTree{

    ///ova klasa radi kao 'controller'

    //na osnovu ova dva polja cemo graditi node-ove
    private JTreeView jTreeView;
    private DefaultTreeModel defaultTreeModel;


    // zvacemo je u MainFrame-u : bice JTreeView projectExplorer = DraftTree.generate()
    // jer na projectExplorer mi dodajmo sve, on je root tj, pravimo stablo
    // projectExplorer izvucemo iz DraftRoomExplorerImplementation-a
    @Override
    public JTreeView generate(ProjectExplorer projectExplorer) {
        DraftTreeItem root = new DraftTreeItem(projectExplorer);
        defaultTreeModel = new DefaultTreeModel(root);
        jTreeView = new JTreeView(defaultTreeModel);
        return jTreeView;
    }

    @Override
    public void addChild(DraftTreeItem parent) {
        // prosledjujemo samo parenta zato sto cemo znati na osnovu njega
        // koji tip deteta treba da pravimo

        //samo objekti klase DraftNodeComposite mogu da imaju decu
        //ako probamo da dodamo na nekom drugom tipu objekta decu ne moze ( npr. Room ne moze da ima decu)
        if (!(parent.getDraftNode() instanceof DraftNodeComposite))
            return;

        DraftNode child = createChild(parent.getDraftNode());
        if(child==null){
            //ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste izabrali nijedan objekat!", MessageType.WARNING,LocalDateTime.now());
            return;
        }

        //dodajemo u view tj Jtree
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
    public void removeChild(DraftTreeItem node) {

        if(node == null){
            return;
        }

        // treba ga obrisati i iz modela i iz viewa
        // prosledjen nam je selektovani node koji treba da obrisemo

        // izvlacimo koji je parent nasem selektovanom nodu
        // da bi tom parentu mogli da removujemo selektovano dete iz liste
        DraftTreeItem parent = (DraftTreeItem) node.getParent();
        //System.out.println(parent);

//        DraftNodeComposite parent = null;
//        if(treeNode != null) {
//            parent = (DraftNodeComposite) treeNode.getUserObject();
//            System.out.println(treeNode.getUserObject());
//        }

        if(node!=null && parent!=null){

            // BRISANJE IZ MODELA
            ((DraftNodeComposite)parent.getDraftNode()).removeChild(node.getDraftNode());

            // BRISANJE IZ VIEW-A
            // prvo izvlacimo model koji se prikazuje u JTreeView
            DefaultTreeModel model = (DefaultTreeModel) jTreeView.getModel();
            model.removeNodeFromParent(node);


            //expandAllNodes(jTreeView);
            refreshTree();
        }
        return;
    }
    // f-ja koja refreshuje celo stablo tako da se vide sva podstabla
    // da ne moramo da ih otvaramo rucno
    private static void expandAllNodes(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }


    @Override
    public DraftTreeItem getSelectedItem() {
        return (DraftTreeItem) jTreeView.getLastSelectedPathComponent();
    }

    // f-ja za refreshovanje stabla
    @Override
    public void refreshTree() {
        DefaultTreeModel model = (DefaultTreeModel) jTreeView.getModel();
        model.reload();
        expandAllNodes(jTreeView);
    }
}
