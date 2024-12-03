package raf.draft.dsw.state;

import raf.draft.dsw.state.concrete.*;

public class StateManager {
    private State currentState;
    private AddState addState;
    private CopyPasteState copyPasteState;
    private DeleteState deleteState;
    private EditRoomState editRoomState;
    private MoveState moveState;
    private ResizeState resizeState;
    private RotateState rotateState;
    private SelectState selectState;
    private ZoomState zoomState;
    public StateManager() {
        initStates();
    }

    public void initStates() {
        addState = new AddState();
        copyPasteState = new CopyPasteState();
        deleteState = new DeleteState();
        editRoomState = new EditRoomState();
        moveState = new MoveState();
        resizeState = new ResizeState();
        rotateState = new RotateState();
        selectState = new SelectState();
        zoomState = new ZoomState();
        currentState = addState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setAddState(AddState addState) {
        currentState = addState;
    }

    public void setCopyPasteState(CopyPasteState copyPasteState) {
        currentState = copyPasteState;
    }

    public void setDeleteState(DeleteState deleteState) {
        currentState = deleteState;
    }

    public void setEditRoomState(EditRoomState editRoomState) {
        currentState = editRoomState;
    }

    public void setMoveState(MoveState moveState) {
        currentState = moveState;
    }

    public void setResizeState(ResizeState resizeState) {
        currentState = resizeState;
    }

    public void setRotateState(RotateState rotateState) {
        currentState = rotateState;
    }

    public void setSelectState(SelectState selectState) {
        currentState = selectState;
    }

    public void setZoomState(ZoomState zoomState) {
        currentState = zoomState;
    }
}
