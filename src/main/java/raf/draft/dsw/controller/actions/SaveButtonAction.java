package raf.draft.dsw.controller.actions;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class SaveButtonAction implements ActionListener {

    private JTextField autor;
    private JTextField naziv;
    private JTextField putanja;
    JDialog dialog;

    public SaveButtonAction(JTextField autor, JTextField naziv, JTextField putanja, JDialog dialog) {
        this.autor = autor;
        this.naziv = naziv;
        this.putanja = putanja;
        this.dialog = dialog;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // selectovan item iz MainFramea
        DraftTreeItem selected = MainFrame.getInstance().getDraftTree().getSelectedItem();


        // OVO POPRAVITI

        // ako je neki field null tj nismo uneli nista -> greska
        // ako nesto nije uneto poruka
        if(naziv.getText().isEmpty() || autor.getText().isEmpty() || putanja.getText().isEmpty()) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste popunili sva potrebna polja!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        String autor = this.autor.getText();
        String naziv = this.naziv.getText();
        String putanja = this.putanja.getText();


        ((Project)(selected.getDraftNode())).setAutorProjekta(autor);
        selected.setIme(naziv);
        ((Project)(selected.getDraftNode())).setPath(putanja);

        MainFrame.getInstance().getDraftTree().refreshTree();
        selected.getDraftNode().notifySubscribers(new Notification(selected.getDraftNode()));

        dialog.dispose();

    }
}
