package raf.draft.dsw.gui.swing;

import lombok.Getter;
import raf.draft.dsw.controller.actions.ActionManager;
import raf.draft.dsw.controller.messagegenerator.MessageGenerator;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.messages.Message;
import raf.draft.dsw.model.messages.MessageType;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements ISubscriber {
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

        // subscribujemo MainFrame na MessageGenerator koji je IPublisher
        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);

        MyMenuBar menu = new MyMenuBar();
        setJMenuBar(menu);

        MyToolBar toolBar = new MyToolBar();
        add(toolBar, BorderLayout.NORTH);
    }
// JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.PLAIN_MESSAGE);
    @Override
    public void update(Message message) {
        if(message.getTip().equals(MessageType.ERROR)){
            JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.ERROR_MESSAGE);
        }
        else if(message.getTip().equals(MessageType.WARNING)){
            JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.WARNING_MESSAGE);
        }
        else if(message.getTip().equals(MessageType.INFORMATION)){
            JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
