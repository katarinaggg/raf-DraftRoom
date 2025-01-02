package raf.draft.dsw.commands;

import raf.draft.dsw.gui.swing.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    // lista koja simulira stek u kojoj se nalaze izvrsene komande
    private List<AbstractCommand> commands = new ArrayList<>();

    // pointer za listu komandi da znamo sta treba da undo/redo
    // pokazuje na PRVO PRAZNO MESTO
    private int pointer=0;

    // dodavanje komande
    public void addCommand(AbstractCommand command) {
        // kada dodajemo brisemo sve sto je desno od pointera
        while(pointer<commands.size()) {
            commands.remove(pointer);
        }
        commands.add(command);
        doCommand();
    }

    // izvrsavanje komandi i REDO
    public void doCommand(){
        // ako je pointer manji od velicine liste komandi
        // imamo sta da REDO pa cemo otkljucati i UNDO
        if(pointer<commands.size()) {
            // pomeramo pointer udesno na prvo prazno mesto da se tu doda nova komanda
            commands.get(pointer++).doCommand();
            MainFrame.getInstance().getProjectView().getCurrentRoomView().enableUndoAction();
        }
        // nemamo sta da REDO -> dosli smo do kraja liste komandi
        if(pointer == commands.size()) {
            MainFrame.getInstance().getProjectView().getCurrentRoomView().disableRedoAction();
        }
    }

    // UNDO
    public void undoCommand(){
        // ako imamo elemenete u listi komandi onda mozemo i da ih UNDO
        // a zatim i REDO
        if(pointer>0) {
            MainFrame.getInstance().getProjectView().getCurrentRoomView().enableRedoAction();
            // pomeramo pointer ulevo jer "brisemo" komandu
            // prvo --pointer jer pointer pokazuje na prvo prazno mesto
            commands.get(--pointer).undoCommand();
        }
        // ako je pointer 0 nemamo elemeneata koje mozemo da UNDO
        if(pointer==0) {
            MainFrame.getInstance().getProjectView().getCurrentRoomView().disableUndoAction();
        }
    }

    public int getPointer() {
        return pointer;
    }

    public List<AbstractCommand> getCommands() {
        return commands;
    }
}
