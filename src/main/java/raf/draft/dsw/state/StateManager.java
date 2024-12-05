package raf.draft.dsw.state;

import raf.draft.dsw.state.concrete.*;

public class StateManager {
    private State currentState;

    private AddBojler addBojler;
    private AddKada addKada;
    private AddKrevet addKrevet;
    private AddLavabo addLavabo;
    private AddOrmar addOrmar;
    private AddSto addSto;
    private AddVesMasina addVesMasina;
    private AddVrata addVrata;
    private AddWCSolja addWCSolja;
    private CopyPasteState copyPasteState;
    private DeleteState deleteState;
    private EditRoomState editRoomState;
    private EditState editState;
    private MoveState moveState;
    private ResizeState resizeState;
    private RotateRightState rotateRightState;
    private RotateLeftState rotateLeftState;
    private SelectState selectState;
    private ZoomState zoomState;

    public StateManager() {
        initStates();
    }

    public void initStates() {
        addBojler = new AddBojler();
        addKada = new AddKada();
        addKrevet = new AddKrevet();
        addLavabo = new AddLavabo();
        addOrmar = new AddOrmar();
        addSto = new AddSto();
        addVesMasina = new AddVesMasina();
        addVrata = new AddVrata();
        addWCSolja = new AddWCSolja();
        copyPasteState = new CopyPasteState();
        deleteState = new DeleteState();
        editRoomState = new EditRoomState();
        editState = new EditState();
        moveState = new MoveState();
        resizeState = new ResizeState();
        rotateRightState = new RotateRightState();
        rotateLeftState = new RotateLeftState();
        selectState = new SelectState();
        zoomState = new ZoomState();
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setAddBojlerState(){
        currentState = addBojler;
        System.out.println("Zapocet AddBojler State");
    }
    public void setAddKadaState(){
        currentState = addKada;
    }
    public void setAddKrevetState(){
        currentState = addKrevet;
    }
    public void setAddLavaboState(){
        currentState = addLavabo;
    }
    public void setAddOrmarState(){
        currentState = addOrmar;
    }
    public void setAddStoState(){
        currentState = addSto;
    }
    public void setAddVesMasinaState(){
        currentState = addVesMasina;
    }
    public void setAddVrataState(){
        currentState = addVrata;
    }
    public void setAddWCSoljaState(){
        currentState = addWCSolja;
    }

    public void setCopyPasteState() {
        currentState = copyPasteState;
    }

    public void setDeleteState() {
        currentState = deleteState;
    }

    public void setEditRoomState() {
        currentState = editRoomState;
    }

    public void setEditState() {
        currentState = editState;
    }

    public void setMoveState() {
        currentState = moveState;
    }

    public void setResizeState() {
        currentState = resizeState;
    }

    public void setRotateRightState() {
        currentState = rotateRightState;
    }

    public void setRotateLeftState() {
        currentState = rotateLeftState;
    }

    public void setSelectState() {
        currentState = selectState;
    }

    public void setZoomState() {
        currentState = zoomState;
    }
}
