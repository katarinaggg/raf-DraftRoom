package raf.draft.dsw.controller.actions.SideToolBarActions;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.AddElementFrame;
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

public class AddRoomElementAction extends AbstractAction {

    public AddRoomElementAction() {
        //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/plus.png"));
        putValue(NAME, "Add Room Element");
        putValue(SHORT_DESCRIPTION, "Add Room Element");
    }

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

    // dugme ce se omoguciti kada je soba dodata pa ce moci i namestaj da se dodaje
    @Override
    public void actionPerformed(ActionEvent e) {

        AddElementFrame addElementFrame = new AddElementFrame(MainFrame.getInstance());
        String selectedElement = addElementFrame.getSelectedElement();

        // u zavisnosti od toga koji smo element izabrali pokrecemo njegovo stanje

        if(selectedElement != null){
            if(selectedElement.equalsIgnoreCase("Bojler")){
                MainFrame.getInstance().getProjectView().startAddBojlerState();
            }
            else if(selectedElement.equalsIgnoreCase("Kada")){
                MainFrame.getInstance().getProjectView().startAddKadaState();
            }
            else if(selectedElement.equalsIgnoreCase("Krevet")){
                MainFrame.getInstance().getProjectView().startAddKrevetState();
            }
            else if(selectedElement.equalsIgnoreCase("Lavabo")){
                MainFrame.getInstance().getProjectView().startAddLavaboState();
            }
            else if(selectedElement.equalsIgnoreCase("Ormar")){
                MainFrame.getInstance().getProjectView().startAddOrmarState();
            }
            else if (selectedElement.equalsIgnoreCase("Sto")) {
                MainFrame.getInstance().getProjectView().startAddStoState();
            }
            else if (selectedElement.equalsIgnoreCase("Ves masina")) {
                MainFrame.getInstance().getProjectView().startAddVesMasinaState();
            }
            else if (selectedElement.equalsIgnoreCase("Vrata")) {
                MainFrame.getInstance().getProjectView().startAddVrataState();
            }
            else if (selectedElement.equalsIgnoreCase("WC solja")) {
                MainFrame.getInstance().getProjectView().startAddWCSoljaState();
            }
        }
        else{
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste selektovali nijedan objekat!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            addElementFrame.setVisible(true);
        }
    }
}
