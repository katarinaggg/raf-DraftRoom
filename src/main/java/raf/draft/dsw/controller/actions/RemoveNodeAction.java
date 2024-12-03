package raf.draft.dsw.controller.actions;

import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.ProjectExplorer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.time.LocalDateTime;

public class RemoveNodeAction extends AbstractRoomAction{

    public RemoveNodeAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/remove.png"));
        putValue(NAME, "Remove Node");
        putValue(SHORT_DESCRIPTION, "Remove Node");
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
        DraftTreeItem selected = MainFrame.getInstance().getDraftTree().getSelectedItem();
        if (selected == null) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste selektovali nijedan objekat!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }
        if (selected.getDraftNode() instanceof ProjectExplorer) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne možete da obrišete Project Explorer!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        MainFrame.getInstance().getDraftTree().removeChild(selected);

       if (selected.getDraftNode() instanceof Project) {
           selected.getDraftNode().notifySubscribers(new Notification(selected.getDraftNode(), true));
       } else {
            selected.getDraftNode().notifySubscribers(new Notification(selected.getDraftNode()));
       }
    }
}
