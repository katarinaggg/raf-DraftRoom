package raf.draft.dsw.gui.swing.jtree.controller;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.notification.Notification;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.EventObject;

public class TreeCellEditor extends DefaultTreeCellEditor implements ActionListener{

    // sta se desi kad kliknemo Node i obradjuje dogadjaje


    private Object clickedOn = null;
    private JTextField edit = null;
    public TreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    public TreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer, javax.swing.tree.TreeCellEditor editor) {
        super(tree, renderer, editor);
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        //kada smo kliknuli 2 puta ulazi se u editMode i poziva se ova metoda

        clickedOn = value; // ono na sta smo kliknuli 3 puta
        edit = new JTextField(value.toString()); // menjamo tekst, otvara se prozorcic u kome vec ima
                                                 // staro ime, a da nemamo value.toString() bio bi prazan

        edit.addActionListener(this);
        return edit;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        //da li smo kliknuli 3 puta na nesto
        if(event instanceof MouseEvent){
            if(((MouseEvent)event).getClickCount()==3){
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(!(clickedOn instanceof DraftTreeItem)){
            //ako smo kliknuli negde sa strane dva puta, a ne na neki node
            return;
        }
        DraftTreeItem clicked = (DraftTreeItem) clickedOn; // ovde smo morali da castujemo u DraftTReeItem
        //tj. da napravimo polje CLICKED jer ne smemo direktno na CLICKEDON da setujemo ime jer je krsenje MVC
        // zato sto menjamo nesto iz modela direktno u view, a to ne sme !

        // ako parent ovog selektovanog Nodea vec ima dete sa istim imenom koje je dato u TextFieldu
        // tj ako u njegovoj listi dece postoji neki Node sa istim imenom ne mozemo da ga preimenujemo
        for(DraftNode dn : ((DraftNodeComposite)clicked.getDraftNode().getParent()).getChildren()){
            if(dn.getIme().equals(edit.getText())){
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Već postoji objekat sa ovim imenom!", MessageType.ERROR, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }
        }

        clicked.setIme(e.getActionCommand());
        clicked.getDraftNode().notifySubscribers(new Notification(clicked.getDraftNode()));
    }
}
