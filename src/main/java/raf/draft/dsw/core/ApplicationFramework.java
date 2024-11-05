package raf.draft.dsw.core;


import lombok.Getter;
import raf.draft.dsw.controller.messagegenerator.*;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.model.repository.DraftRoomExplorerImplementation;
import raf.draft.dsw.model.repository.DraftRoomRepository;

@Getter
public class ApplicationFramework {
    //buduca polja za model celog projekta
    MessageGenerator messageGenerator; //= new MessageGenerator();
    Logger consoleLogger;
    Logger fileLogger;
    DraftRoomExplorerImplementation draftRoomExplorerImplementation;

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

        // fora je da se messageGenerator napravi pre MainFrame-a, da ne bi bio null
        // jer ga inace pozivamo prvo u MainFrame-u, a onda posle inicijalizujemo
        messageGenerator = new MessageGenerator();

        //ne mozemo da pravino DraftRoomRepository zato sto je to interface u kom nemamo nista
        // vec u ExplorerImplementation-u zapravo cuvamo projectExplorer
        draftRoomExplorerImplementation = new DraftRoomExplorerImplementation();

        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setVisible(true);


        // da li se ovo radi ovde ili u loggeru?
        LoggerFactory factory = new LoggerFactory();
        consoleLogger = factory.createLogger("Console");
        messageGenerator.addSubscriber(consoleLogger);
        fileLogger = factory.createLogger("File");
        messageGenerator.addSubscriber(fileLogger);
    }



}
