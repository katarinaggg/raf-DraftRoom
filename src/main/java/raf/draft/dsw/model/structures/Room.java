package raf.draft.dsw.model.structures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.namestaj.RoomElement;
import raf.draft.dsw.serializer.ColorDeserializer;
import raf.draft.dsw.serializer.ColorSerializer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Getter@NoArgsConstructor
public class Room extends DraftNodeComposite {

    private double sirina;
    private double duzina;

    // Polja za crtanje
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color lineColor = Color.BLACK; // Boja linija (crna)

    private float lineThickness = 2.0f; // Debljina linija (2 px)

    // njegova deca
    // ne treba nam posebna lista jer je Room DraftNodeComposite
    // koji u sebi vec ima ovo polje
    //private ArrayList<RoomElement> namestaj = new ArrayList<>();

    @JsonIgnore
    private ArrayList<RoomElement> copyElements = new ArrayList<>();

    public Room(String ime, DraftNode parent, double sirina, double duzina) {
        super(ime, parent);
        this.sirina = sirina;
        this.duzina = duzina;
    }

    public Room(String ime, DraftNode parent) {
        super(ime, parent);
        //System.out.println(this.getProjectParent());
    }


    // Konverzija centimetara u piksele
    public int convertCmToPx(double cm, double scalingFactor) {
        return (int) (cm * scalingFactor);
    }

    @Override
    public void addChild(DraftNode child) {
        if(!this.getChildren().contains((RoomElement) child)){
            this.getChildren().add((RoomElement) child);
        }
        //  kada dodajemo dete sobi projekat se menja
        getProjectParent().setChanged(true);

        // Room je vec IPublisher i samo treba da obavesti RoomView
        // (koji je sada postao ISubscriber) da je dodato dete i da treba da se repaintuje

        notifySubscribers(new Notification());

    }

    @Override
    public void removeChild(DraftNode child) {
        this.getChildren().remove(child);
        // kada brisemo dete projekat se menja
        getProjectParent().setChanged(true);

        // obavestavamo RoomView da smo uklonili dete
        // i da treba da se repaintuje
        notifySubscribers(new Notification());
    }

    public double getSirina() {
        return sirina;
    }

    public double getDuzina() {
        return duzina;
    }

//    public ArrayList<RoomElement> getNamestaj() {
//        return namestaj;
//    }

    public void setSirina(double sirina) {
        this.sirina = sirina;
    }

    public void setDuzina(double duzina) {
        this.duzina = duzina;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public float getLineThickness() {
        return lineThickness;
    }

    public ArrayList<RoomElement> getCopyElements() {
        return copyElements;
    }

    // funkcija za nalazennje odgovarajuceg Projecta koji je direktni
    // ili indirektni parent ovoj sobi kako bi obelezili da je doslo
    // do promena kako bi mogao da se sacuva

    @JsonIgnore
    public Project getProjectParent(){
        // ako joj je parent Project
        if(this.getParent() instanceof Project){
            return (Project) this.getParent();
        }
        // ako joj je parent Building onda je Buildingov parent Project
        else{
            return (Project) this.getParent().getParent();
        }
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineThickness(float lineThickness) {
        this.lineThickness = lineThickness;
    }
}
