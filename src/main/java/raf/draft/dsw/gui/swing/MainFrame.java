package raf.draft.dsw.gui.swing;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    //buduca polja za sve komponente view-a na glavnom prozoru

    private static MainFrame mainFrameInstance;

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
