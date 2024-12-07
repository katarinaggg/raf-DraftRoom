package raf.draft.dsw.state.concrete;

import lombok.NoArgsConstructor;
import raf.draft.dsw.gui.swing.MainFrame;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.painter.ElementPainter;
import raf.draft.dsw.model.painter.RoomPainter;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.state.State;

import javax.swing.*;
import java.awt.*;

@NoArgsConstructor
public class SelectState implements State {

    @Override
    public void misKliknut(RoomView roomView, Point point) {

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
    }

    @Override
    public void misPomeren() {

    }

    @Override
    public void misScroll() {

    }


    ///mousePressed
    @Override
    public void misZadrzan(RoomView roomView, Point point) {
        roomView.getSelectionList().clear();
        roomView.setSelectionRectangleLT(point);
    }


    ///mouseDragged
    @Override
    public void misPovucen(RoomView roomView, Point point) {
        if(roomView.getSelectionRectangleLT() != null){
            roomView.setSelectionRectangleRB(point);
            roomView.repaint();
        }


        //kada je mouseDown uzmem i setujem gornju levu tacku, dok prevlacim ukoliko postoji gornja levatacka,
        // svaki put kad se triggeruje dragged tu tacku (iz neceg toggled, mozda nisam dobro cukla?) setujem na donju desnu

        //tokom prevlacenja svaki put kad setujemo donju desnu ivicu pozovi repaint modela
    }

    @Override
    public void misPusten(RoomView roomView, Point point) {
        roomView.setSelectionRectangleRB(point);
        roomView.repaint();
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

        System.out.println(roomView.getSelectionList() +" \n");

    }
}
