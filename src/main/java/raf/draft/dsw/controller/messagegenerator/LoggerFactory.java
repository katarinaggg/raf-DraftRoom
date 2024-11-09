package raf.draft.dsw.controller.messagegenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor
public class LoggerFactory {


    public Logger createLogger(String type){
        Logger logger = null;
        if(type.equalsIgnoreCase("CONSOLE")){
            logger = new ConsoleLogger();
        }
        else if(type.equalsIgnoreCase("FILE")){
            logger = new FileLogger();
        }
        return logger;
    }

}
