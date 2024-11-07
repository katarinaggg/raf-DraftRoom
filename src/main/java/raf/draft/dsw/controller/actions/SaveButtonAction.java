package raf.draft.dsw.controller.actions;

import com.sun.tools.javac.Main;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.messages.Message;
import raf.draft.dsw.model.messages.MessageType;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
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
//        if(autor==null || naziv==null || putanja==null){
//            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste popunili sva potrebna polja!", MessageType.ERROR, LocalDateTime.now());
//            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(message);
//            return;
//        }


        String autor = this.autor.getText();
        String naziv = this.naziv.getText();
        String putanja = this.putanja.getText();


        selected.setAutor(autor);
        selected.setIme(naziv);
        selected.setPath(putanja);

        // kako da refresujem stablo??

        dialog.dispose();

    }
}
