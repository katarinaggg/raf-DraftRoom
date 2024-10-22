package raf.draft.dsw.gui.swing;

import lombok.Data;
import lombok.Getter;
import raf.draft.dsw.controller.ActionManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    //buduca polja za sve komponente view-a na glavnom prozoru

    private static MainFrame mainFrameInstance;
    @Getter
    private ActionManager actionManager;

    private MainFrame(){
    }

    public static MainFrame getInstance(){
        if(mainFrameInstance == null){
            mainFrameInstance = new MainFrame();
            mainFrameInstance.initialize();

        }
        return mainFrameInstance;
    }

    private void initialize(){
        //ovde sam inicijalizovala actionManager, jer ovde pravim jedini objekat MainFrame-a
        // tj. sve sto taj objekat treba da ima
        actionManager=new ActionManager();

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("DraftRoom");

        MyMenuBar menu = new MyMenuBar();
        setJMenuBar(menu);

        MyToolBar toolBar = new MyToolBar();
        add(toolBar, BorderLayout.NORTH);
    }
}
