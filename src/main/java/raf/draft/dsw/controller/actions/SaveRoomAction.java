package raf.draft.dsw.controller.actions;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDateTime;

public class SaveRoomAction extends AbstractRoomAction {

    public SaveRoomAction() {
        putValue(SMALL_ICON, loadIcon("/images/saveRoom.png"));
        putValue(NAME, "Save Room");
        putValue(SHORT_DESCRIPTION, "Save Room");
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
        Room clicked = MainFrame.getInstance().getClickedRoom();
        if(clicked != null){
            ApplicationFramework.getInstance().getSerializer().sacuvajSablonSobe(clicked);
        }
        else{
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate da izaberete sobu koji zelite da sacuvate", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }
    }
}
