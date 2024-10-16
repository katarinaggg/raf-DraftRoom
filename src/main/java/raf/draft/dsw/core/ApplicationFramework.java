package raf.draft.dsw.core;

import raf.draft.dsw.gui.swing.MainFrame;

public class ApplicationFramework {
    //buduca polja za model celog projekta

    private static ApplicationFramework applicationFrameworkInstance;
    private ApplicationFramework(){
    }

    public static ApplicationFramework getInstance(){
        if(applicationFrameworkInstance==null){
            applicationFrameworkInstance = new ApplicationFramework();
            applicationFrameworkInstance.initialize();
        }
        return applicationFrameworkInstance;
    }

    public void initialize(){
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setVisible(true);
    }
}
