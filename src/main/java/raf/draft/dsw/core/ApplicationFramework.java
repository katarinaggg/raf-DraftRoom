package raf.draft.dsw.core;


import lombok.Getter;
import raf.draft.dsw.controller.messagegenerator.*;
import raf.draft.dsw.controller.observer.IPublisher;
import raf.draft.dsw.gui.swing.MainFrame;

@Getter
public class ApplicationFramework {
    //buduca polja za model celog projekta
    MessageGenerator messageGenerator = new MessageGenerator();
    Logger consoleLogger;
    Logger fileLogger;

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

        // zasto ne radi ovde??
        //messageGenerator = new MessageGenerator();

        // da li se ovo radi ovde ili u loggeru?
        LoggerFactory factory = new LoggerFactory();
        consoleLogger = factory.createLogger("Console");
        fileLogger = factory.createLogger("File");
    }



}
