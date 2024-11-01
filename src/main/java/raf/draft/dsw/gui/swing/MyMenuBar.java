package raf.draft.dsw.gui.swing;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MyMenuBar extends JMenuBar {
    public MyMenuBar(){
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        //ExitAction ea = new ExitAction(); - ovo je zakomentarisano jer ne pravimo nove akcije,
        // vec ih izvlacimo iz MainFrame-a
        fileMenu.add(MainFrame.getInstance().getActionManager().getExitAction());
        add(fileMenu);


        JMenu aboutUsMenu = new JMenu("About us");
        aboutUsMenu.setMnemonic(KeyEvent.VK_F);
        aboutUsMenu.add(MainFrame.getInstance().getActionManager().getAbouUsAction());
        add(aboutUsMenu);
    }
}
