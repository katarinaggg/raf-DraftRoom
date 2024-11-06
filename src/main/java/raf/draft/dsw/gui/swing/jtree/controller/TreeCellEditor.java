package raf.draft.dsw.gui.swing.jtree.controller;

import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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

        clickedOn = value; // ono na sta smo kliknuli 2 puta
        edit = new JTextField(value.toString()); // menjamo tekst, otvara se prozorcic u kome vec ima
                                                 // staro ime, a da nemamo value.toString() bio bi prazan

        edit.addActionListener(this);
        return edit;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        //da li smo kliknuli 2 puta na nesto
        if(event instanceof MouseEvent){
            if(((MouseEvent)event).getClickCount()==2){
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

        clicked.setIme(e.getActionCommand());
    }
}
