package raf.draft.dsw.gui.swing.jtree.view;

import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.ProjectExplorer;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.net.URL;

public class TreeCellRenderer extends DefaultTreeCellRenderer {

    ///ovde pravimo izgled Buildinga i Room-a, sve sto je LEVO


    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        URL imageURL = null;

        if (((DraftTreeItem)value).getDraftNode() instanceof ProjectExplorer) {
            imageURL = getClass().getResource("/images/projectExplorer.png");
        }

        else if (((DraftTreeItem)value).getDraftNode() instanceof Project) {
            imageURL = getClass().getResource("/images/project.png");
        }

        else if(((DraftTreeItem)value).getDraftNode() instanceof Building){
            imageURL = getClass().getResource("/images/building.png");
        }

        else if(((DraftTreeItem)value).getDraftNode() instanceof Room){
            imageURL = getClass().getResource("/images/rooms.png");
        }

        ///nisam sigurna za ovo

        else if(((DraftTreeItem)value).getDraftNode() instanceof Bojler){
            imageURL = getClass().getResource("/namestaj/boiler.png");
        }

        else if(((DraftTreeItem)value).getDraftNode() instanceof Kada){
            imageURL = getClass().getResource("/namestaj/bath.png");
        }

        else if(((DraftTreeItem)value).getDraftNode() instanceof Krevet){
            imageURL = getClass().getResource("/namestaj/double-bed.png");
        }
        else if(((DraftTreeItem)value).getDraftNode() instanceof Lavabo){
            imageURL = getClass().getResource("/namestaj/sink.png");
        }
        else if(((DraftTreeItem)value).getDraftNode() instanceof Ormar){
            imageURL = getClass().getResource("/namestaj/closet.png");
        }
        else if(((DraftTreeItem)value).getDraftNode() instanceof Sto){
            imageURL = getClass().getResource("/namestaj/desk.png");
        }
        else if(((DraftTreeItem)value).getDraftNode() instanceof VesMasina){
            imageURL = getClass().getResource("/namestaj/laundry-machine.png");
        }
        else if(((DraftTreeItem)value).getDraftNode() instanceof Vrata){
            imageURL = getClass().getResource("/namestaj/door.png");
        }
        else if(((DraftTreeItem)value).getDraftNode() instanceof WCSolja){
            imageURL = getClass().getResource("/namestaj/toilet.png");
        }


        Icon icon = null;
        if (imageURL != null)
            icon = new ImageIcon(imageURL);
        setIcon(icon);

        return this;
    }
}
