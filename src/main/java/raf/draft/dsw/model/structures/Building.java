package raf.draft.dsw.model.structures;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.serializer.ColorDeserializer;
import raf.draft.dsw.serializer.ColorSerializer;

import java.awt.*;
import java.util.Random;


public class Building extends DraftNodeComposite {

    // treba dodati polje za boju
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    Color color;

    public Building(String ime, DraftNode parent) {
        super(ime, parent);
        loadColor();
    }

    private void loadColor() {
        Random random = new Random();
        int r = random.nextInt(256); // Random red (0-255)
        int g = random.nextInt(256); // Random green (0-255)
        int b = random.nextInt(256); // Random blue (0-255)
        this.color = new Color(r, g, b);
    }

    @Override
    public void addChild(DraftNode child) {
        if (child != null &&  child instanceof Room){
            Room room = (Room) child;
            if (!this.getChildren().contains(room)){
                this.getChildren().add(room);
            }
        }
    }

    @Override
    public void removeChild(DraftNode child) {
        if(child != null &&  child instanceof Room){
            this.getChildren().remove(child);
        }
    }

    public Color getColor() {
        return color;
    }
}
