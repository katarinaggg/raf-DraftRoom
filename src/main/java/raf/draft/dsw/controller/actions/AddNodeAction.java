package raf.draft.dsw.controller.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.messages.Message;
import raf.draft.dsw.model.messages.MessageType;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.time.LocalDateTime;

@Data
public class AddNodeAction extends AbstractRoomAction{

    public AddNodeAction(){
        //bitno
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/plus.png"));
        putValue(NAME, "Add Node");
        putValue(SHORT_DESCRIPTION, "Add Node");
    }
    @Override
    public Icon loadIcon(String path) {
        Icon icon = null;
        URL ImageURL = getClass().getResource(path);
        if(ImageURL != null)
        {
            Image img = new ImageIcon(ImageURL).getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            icon = new ImageIcon(newImg);
        }
        else
        {
            System.err.println("File " + path + " not found");
        }
        return icon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DraftTreeItem selected = (DraftTreeItem) MainFrame.getInstance().getDraftTree().getSelectedItem();
        // ako smo kliknuli na plus a nije oznacen ni jedan node -> greska
        // pravimo poruku i saljemo je
        // MessageGenerator treba da notify subscribere tj Loggere i MainFrame
        // a oni ce preko update() metoda da urade sta treba
        Message message;
        if(selected == null){
            message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste selektovali nijedan objekat!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(message);
            return;
        }
        if(selected.getDraftNode() instanceof Room){
            message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne možete da dodajete objekte na Room!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(message);
            return;
        }
        MainFrame.getInstance().getDraftTree().addChild(selected);
    }
}
