package raf.draft.dsw.controller.actions;

import lombok.*;

@Getter@Data
public class ActionManager {

    //ovde treba da stavljamo i pravimo sve akcije koje treba da koristimo

    private ExitAction exitAction;
    private AbouUsAction abouUsAction;
    private AddNodeAction addNodeAction;
    private RemoveNodeAction removeNodeAction;
    private EditProjectAction editProjectAction;

    public ActionManager(){
        exitAction = new ExitAction();
        abouUsAction = new AbouUsAction();
        addNodeAction = new AddNodeAction();
        removeNodeAction = new RemoveNodeAction();
        editProjectAction = new EditProjectAction();
    }

}
