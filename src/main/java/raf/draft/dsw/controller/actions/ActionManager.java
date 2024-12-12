package raf.draft.dsw.controller.actions;

import lombok.*;
import raf.draft.dsw.controller.actions.SideToolBarActions.*;

@Getter@Data
public class ActionManager {

    //ovde treba da stavljamo i pravimo sve akcije koje treba da koristimo

    private ExitAction exitAction;
    private AbouUsAction abouUsAction;
    private AddNodeAction addNodeAction;
    private RemoveNodeAction removeNodeAction;
    private EditProjectAction editProjectAction;

    // akcije na desnom toolbaru
    private AddRoomElementAction addRoomElementAction;
    private CopyPasteAction copyPasteAction;
    private DeleteAction deleteAction;
    private EditRoomAction editRoomAction;
    private EditRoomElementAction editRoomElementAction;
    private MoveRoomElementAction moveRoomElementAction;
    private ResizeAction resizeAction;
    private RotateLeftAction rotateLeftAction;
    private RotateRightAction rotateRightAction;
    private SelectRoomElementAction selectRoomElementAction;
    private ZoomAction zoomAction;

    public ActionManager(){
        exitAction = new ExitAction();
        abouUsAction = new AbouUsAction();
        addNodeAction = new AddNodeAction();
        removeNodeAction = new RemoveNodeAction();
        editProjectAction = new EditProjectAction();

        addRoomElementAction = new AddRoomElementAction();
        copyPasteAction = new CopyPasteAction();
        deleteAction = new DeleteAction();
        editRoomAction = new EditRoomAction();
        editRoomElementAction = new EditRoomElementAction();
        moveRoomElementAction = new MoveRoomElementAction();
        resizeAction = new ResizeAction();
        rotateLeftAction = new RotateLeftAction();
        rotateRightAction = new RotateRightAction();
        selectRoomElementAction = new SelectRoomElementAction();
        zoomAction = new ZoomAction();


    }

}
