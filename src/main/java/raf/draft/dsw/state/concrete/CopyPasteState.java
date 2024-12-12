package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.namestaj.*;
import raf.draft.dsw.painter.*;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

@NoArgsConstructor
public class CopyPasteState implements State {

    // i copy i paste u jednom stanju da bi se na jedan klik sve desilo

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // brisemo staru listu kopiranih
        roomView.getRoom().getCopyElements().clear();

        // COPY
        // prolazimo kroz listu selektovanih paintera
        // i pravimo kopije za svaki element i dodajemo ih u listu
        if(!roomView.getSelectionList().isEmpty()){
            for(ElementPainter e:roomView.getSelectionList()){
                RoomElement roomElement = e.getRoomElement();
                roomView.getRoom().getCopyElements().add(roomElement);
            }
        }
        else{
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste selektovali elemente za kopiranje", MessageType.INFORMATION, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // PASTE
        // prolazimo kroz listu kopiranih
        for(RoomElement roomElement:roomView.getRoom().getCopyElements()){

            // pravimo isti element
            RoomElement kopija = (RoomElement) roomElement.clone();

            // pravimo novi Painter
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
            // dodajemo painter u roomView da bi se nacrtao
            roomView.addPainter(elementPainter);

            // dodajemo element kao dete sobi:
            DraftTreeItem soba = MainFrame.getInstance().getDraftTree().findTreeItem(roomView.getRoom());
            MainFrame.getInstance().getDraftTree().addRoomChild(soba,kopija);
        }

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll(RoomView roomView, MouseWheelEvent e) {

    }

    @Override
    public void misZadrzan(RoomView roomView, Point point) {

    }

    @Override
    public void misPovucen(RoomView roomView, Point point) {

    }

    @Override
    public void misPusten(RoomView roomView, Point point) {

    }

}
