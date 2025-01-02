package raf.draft.dsw.state.concrete;

import raf.draft.dsw.commands.concrete.AddBojlerCommand;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.BojlerPainter;
import raf.draft.dsw.model.structures.namestaj.Bojler;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

public class AddBojler implements State {

    // sada ovde pravimo komandu koja ce da ima ceo kod za mis kliknut
    // prosledjujemo potrebna polja za izvrsavanje komande tj
    // prosledjujemo roomView i point

    // ovde treba da ostane unosenje dimenzija jer ne zelimo i za REDO
    // da unosimo opet dimenzije vec da se nacrta onaj postojeci

    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }


        // koristimo metodu koja nam vraca novu lokaciju misa u sljucaju
        // da se nalazimo u nekom zoomiranom stanju
        // treba nam da bi dodali element tacno tamo gde smo kliknuli misem
        point = roomView.transformMousePoint(point);

        // kada kliknemo misem na to mesto (point treba nacrtati element)
        // otvara se prozor za unosenje dimenzija

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        if(dimenzija != null && dimenzija.width > 0 && dimenzija.height > 0) {
            // pravimo novi model
            Bojler bojler = new Bojler(roomView.getRoom(), point, dimenzija);
            BojlerPainter bojlerPainter = new BojlerPainter(bojler, roomView);

            // moramo da proverimo da li element dodajemo unutar sobe
            // tj da li izlazi iz granica sobe ako ga dodamo na
            // tu odredjenuy lokaciju

            // ova metoda nam ne radi kada zumiramo jer
            // nam iz nekog razloga nisu dobre granice sobe
            // iako se uvek uzima u obzir i zoomLevel ???
//            if(!roomView.unutarSobe(point.x,point.y,dimenzija.width,dimenzija.height)){
//                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete da dodate element van granica sobe!", MessageType.WARNING, LocalDateTime.now());
//                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
//                return;
//            }

            // moramo da proverimo da li se sece sa nekim
            // vec postojecim elementom
            // ako ne mozemo da ga nacrtamo
            if (roomView.preklapanjeElemenata(bojlerPainter.getShape())) {
                // ako se preklapaju ne mozemo da ga dodamo i izlazi poruka
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete da dodate element jer se preklapa sa postojecim!", MessageType.WARNING, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }

            AddBojlerCommand addBojlerCommand = new AddBojlerCommand(roomView,bojler,bojlerPainter);
            // dodajemo komandu u CommandManager ove sobe
            roomView.getCommandManager().addCommand(addBojlerCommand);
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
