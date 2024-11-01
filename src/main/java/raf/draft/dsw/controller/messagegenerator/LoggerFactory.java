package raf.draft.dsw.controller.messagegenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.messages.Message;
import raf.draft.dsw.model.messages.MessageType;

@Data@NoArgsConstructor
public class LoggerFactory {

    // na osnovu cega pravimo logger?
    // message ili string?
    public Logger createLogger(String type){
        Logger logger = null;
        if(type.equalsIgnoreCase("CONSOLE")){
            logger = new ConsoleLogger();
        }
        else if(type.equalsIgnoreCase("FILE")){
            logger = new FileLogger();
        }
        // subscribujemo se
        // logger je subscriber za messageGenerator koji je publisher
        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(logger);
        return logger;
    }

}
