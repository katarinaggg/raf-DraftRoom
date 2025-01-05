package raf.draft.dsw.commands.concrete;

import raf.draft.dsw.commands.AbstractCommand;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.structures.namestaj.*;
import raf.draft.dsw.painter.*;

import java.util.ArrayList;
import java.util.List;

public class CopyPasteCommand extends AbstractCommand {

    private RoomView roomView;
    private List<RoomElement> originalSelection = new ArrayList<>();
    private  List<RoomElement> pastedElements = new ArrayList<>();
    private  List<ElementPainter> pastedPainters = new ArrayList<>();

    public CopyPasteCommand(RoomView roomview) {
        this.roomView = roomview;

        // selektovani elementi
        // one koje smo stavili u listu kopiranih
        this.originalSelection.addAll(roomView.getRoom().getCopyElements());

        // prolazimo kroz listu kopiranih
        for(RoomElement e: originalSelection){
            // pravimo isti element
            RoomElement kopija = (RoomElement) e.clone();

            // dodajemo ga u listu za paste-ovanje
            pastedElements.add(kopija);

            // pravimo mu i odgovarajuci painter
            pastedPainters.add(createPainter(kopija));
        }
    }

    private ElementPainter createPainter(RoomElement kopija){
        ElementPainter elementPainter = null;
        if (kopija instanceof Bojler){
            elementPainter = new BojlerPainter((Bojler) kopija,roomView);
        }
        else if (kopija instanceof Kada){
            elementPainter = new KadaPainter((Kada) kopija,roomView);
        }
        else if (kopija instanceof Krevet) {
            elementPainter = new KrevetPainter((Krevet) kopija,roomView);
        }
        else if (kopija instanceof Lavabo) {
            elementPainter = new LavaboPainter((Lavabo) kopija,roomView);
        }
        else if (kopija instanceof Ormar){
            elementPainter = new OrmarPainter((Ormar) kopija,roomView);
        }
        else if (kopija instanceof Sto) {
            elementPainter = new StoPainter((Sto) kopija,roomView);
        }
        else if (kopija instanceof VesMasina) {
            elementPainter = new VesMasinaPainter((VesMasina) kopija, roomView);
        }
        else if (kopija instanceof WCSolja) {
            elementPainter = new WCSoljaPainter((WCSolja) kopija, roomView);
        }
        else if(kopija instanceof Vrata){
            elementPainter = new VrataPainter((Vrata) kopija, roomView);
        }
        return elementPainter;
    }

    @Override
    public void doCommand() {

        // dodajemo nove elemenete iz liste pastedElements jer su nam tu klonovi
        // iscrtavamo ih i dodajemo u stablo
        for(int i=0; i<pastedElements.size(); i++){
            RoomElement kopija = pastedElements.get(i);
            ElementPainter elementPainter = pastedPainters.get(i);

            roomView.addPainter(elementPainter);

            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
            MainFrame.getInstance().getDraftTree().addRoomChild(soba,kopija);
        }

        // ne mora jer se poziva u addChild
        //roomView.repaint();

    }

    @Override
    public void undoCommand() {
        // brisemo kopirane elemente iz prikaza i stabla
        for(int i=0; i<pastedElements.size(); i++){
            RoomElement kopija = pastedElements.get(i);
            ElementPainter elementPainter = pastedPainters.get(i);

            roomView.removePainter(elementPainter);
            DraftTreeItem roomElement = MainFrame.getInstance().getDraftTree().findTreeItem(kopija);
            MainFrame.getInstance().getDraftTree().removeChild(roomElement);
        }
    }
}
