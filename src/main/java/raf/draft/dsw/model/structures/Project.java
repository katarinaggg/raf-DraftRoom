package raf.draft.dsw.model.structures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.UUID;

@Getter@Setter@NoArgsConstructor
public class Project extends DraftNodeComposite {

    //private String ime;     vec ima kao polje u DraftNode :')

    // ime kreatora projekta
    private String autorProjekta;
    // putanja do foldera
    private String path;

    // da sacuvamo projekat samo kad je izmenjen
    @JsonIgnore
    private boolean changed;

    @JsonIgnore
    private final String id= UUID.randomUUID().toString(); // Generišemo jedinstveni ID

    public Project(String ime, DraftNode parent) {
        super(ime, parent);
        this.changed = true ;
    }


    @Override
    public void addChild(DraftNode child) {
        // kada dodamo dete menja se Projekat pa setujemo changed
        if (child != null &&  child instanceof Building){
            Building building = (Building) child;
            if (!this.getChildren().contains(building)){
                this.getChildren().add(building);
                setChanged(true);
            }
        }
        else if (child != null &&  child instanceof Room) {
            Room room = (Room) child;
            if (!this.getChildren().contains(room)){
                this.getChildren().add(room);
                setChanged(true);
            }
        }
    }

    @Override
    public void removeChild(DraftNode child) {
        // i kada brisemo dete menja se projekat pa setujemo changed
        if(child!=null && (child instanceof Building || child instanceof Room)){
            this.getChildren().remove(child);
            setChanged(true);
        }
    }

    public void setAutorProjekta(String autorProjekta) {
        this.autorProjekta = autorProjekta;
        setChanged(true);
    }

    public void setPath(String path) {
        this.path = path;
        setChanged(true);
    }

    // f-ja za izvlacenje svih soba iz projekta
    @JsonIgnore
    public ArrayList<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        // prolazimo kroz svu njegovu decu
        for (DraftNode child : this.getChildren()) {

            // ako mu je dete Room dodaj ga u listu
            if (child instanceof Room) {
                rooms.add((Room) child);
            }
            // ako mu je dete Building proci kroz svu njegovu decu (koja su Rooms)
            if (child instanceof Building) {
                for(DraftNode child2: ((Building)child).getChildren()){
                    rooms.add((Room) child2);
                }
            }
        }
        return rooms;
    }

    public void setChanged(boolean changed) {
        System.out.println("PROMENA "+ changed);
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Project other = (Project) obj;
        return id.equals(other.id); // Projekti su jednaki samo ako imaju isti ID
    }

    @Override
    public int hashCode() {
        return id.hashCode(); // Hash kod baziran na jedinstvenom ID-u
    }

}
