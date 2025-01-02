package raf.draft.dsw.state.concrete;

import raf.draft.dsw.commands.concrete.AddOrmarCommand;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.DimensionFrame;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.gui.swing.jtree.model.DraftTreeItem;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.OrmarPainter;
import raf.draft.dsw.model.structures.namestaj.Ormar;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

public class AddOrmar implements State {
    @Override
    public void misKliknut(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        point = roomView.transformMousePoint(point);

        DimensionFrame frame = new DimensionFrame(roomView);
        frame.setVisible(true);

        Dimension dimenzija = frame.getDimenzija();

        if(dimenzija!=null && dimenzija.width>0 && dimenzija.height>0) {
            Ormar ormar = new Ormar(roomView.getRoom(), point, dimenzija);
            OrmarPainter ormarPainter = new OrmarPainter(ormar,roomView);

//            if(!roomView.unutarSobe(point.x,point.y,dimenzija.width,dimenzija.height)){
//                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete da dodate element van granica sobe!", MessageType.WARNING, LocalDateTime.now());
//                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
//                return;
//            }

            if(roomView.preklapanjeElemenata(ormarPainter.getShape())){
                // ako se preklapaju ne mozemo da ga dodamo i izlazi poruka
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Ne mozete da dodate element jer se preklapa sa postojecim!", MessageType.WARNING, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                return;
            }

            AddOrmarCommand addOrmarCommand = new AddOrmarCommand(roomView, ormar, ormarPainter);
            roomView.getCommandManager().addCommand(addOrmarCommand);
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
