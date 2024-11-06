package raf.draft.dsw.controller.actions;

import com.sun.tools.javac.Main;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

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

        // ako nesto nije uneto poruka
        if(autor==null || naziv==null || putanja==null){

        }

        String autor = this.autor.getText();
        String naziv = this.naziv.getText();
        String putanja = this.putanja.getText();

        // morace da bude project, nece moci da se klikne dugme edit ako nije
        // podesiti to kasnije
//        if(!(selected.getDraftNode() instanceof Project)){
//        }

        selected.setAutor(autor);
        selected.setIme(naziv);
        selected.setPath(putanja);

        // kako da refresujem stablo??

        dialog.dispose();

    }
}
