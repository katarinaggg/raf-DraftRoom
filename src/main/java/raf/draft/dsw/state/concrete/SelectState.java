package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.painter.ElementPainter;
import raf.draft.dsw.painter.RoomPainter;
import raf.draft.dsw.state.State;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;

@NoArgsConstructor
public class SelectState implements State {

    private Point kliknut;

    // ne mozemo ovde da koristimo misKliknut jer je to kombinacija
    // misZadrzan i misPusten samo kad to uradimo na istom pointu
    @Override
    public void misKliknut(RoomView roomView, Point point) {

    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll(RoomView roomView, MouseWheelEvent e) {

    }


    ///mousePressed
    @Override
    public void misZadrzan(RoomView roomView, Point point) {

        if(!roomView.isNacrtanaSoba()){
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate prvo da nacrtate sobu!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // postavljamo klik
        kliknut = point;
        roomView.getSelectionList().clear();
        roomView.setSelectionRectangleLT(point);
        System.out.println("Mis zadrzan");
    }


    ///mouseDragged
    @Override
    public void misPovucen(RoomView roomView, Point point) {
        if(roomView.getSelectionRectangleLT() != null){
            roomView.setSelectionRectangleRB(point);
            roomView.repaint();
            System.out.println("Mis povucen");
        }

        //kada je mouseDown uzmem i setujem gornju levu tacku, dok prevlacim ukoliko postoji gornja levatacka,
        // svaki put kad se triggeruje dragged tu tacku (iz neceg toggled, mozda nisam dobro cukla?) setujem na donju desnu

        //tokom prevlacenja svaki put kad setujemo donju desnu ivicu pozovi repaint modela
    }

    // mouseReleased
    @Override
    public void misPusten(RoomView roomView, Point point) {
        roomView.setSelectionRectangleRB(point);

        // ako je pocetni i krajnji klik na istom mestu
        // onda je to zapravo bio samo misKliknut
        if(kliknut != null && kliknut.equals(point)){

            //svaki put kada kliknem obrisem sve iz prethodne selektovane liste, da bih dodala nove elemente
            roomView.getSelectionList().clear();

            for(ElementPainter e : roomView.getPainters()){
                if(!(e instanceof RoomPainter)){
                    /// elementAt nam kaze za ovu tacku, reci mi da li se ta taj Point sece sa mojim elementom
                    if(e.elementAt(point, null)){
                        roomView.getSelectionList().add(e);
                    }
                }
            }
            roomView.repaint();
            System.out.println("Mis kliknut");
            System.out.println(roomView.getSelectionList());
            return;
        }

        // ako nije bio misKliknut znaci da smo prevlacili

        // Izračunavanje pravougaonika
        int x = Math.min(roomView.getSelectionRectangleLT().x, roomView.getSelectionRectangleRB().x);
        int y = Math.min(roomView.getSelectionRectangleLT().y, roomView.getSelectionRectangleRB().y);
        int width = Math.abs(roomView.getSelectionRectangleRB().x - roomView.getSelectionRectangleLT().x);
        int height = Math.abs(roomView.getSelectionRectangleRB().y - roomView.getSelectionRectangleLT().y);

        // Kreiranje pravougaonika
        Rectangle selectionRectangle = new Rectangle(x, y, width, height);

        for(ElementPainter e : roomView.getPainters()){
            if(!(e instanceof RoomPainter)){
                /// elementAt nam kaze za ovu tacku, reci mi da li se ta taj Point sece sa mojim elementom
                if(e.elementAt(point, selectionRectangle)){
                    roomView.getSelectionList().add(e);
                }
            }
        }

        // Resetovanje selekcionih tačaka
        // da bi se pravouganoik obrisao kada pustimo mis
        roomView.setSelectionRectangleLT(null);
        roomView.setSelectionRectangleRB(null);

        roomView.repaint();
        System.out.println("Mis pusten");
        System.out.println(roomView.getSelectionList());

    }
}
