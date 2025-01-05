package raf.draft.dsw.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.observer.IPublisher;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Room;
import raf.draft.dsw.model.structures.namestaj.*;

import java.util.ArrayList;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // Koristi ime klase za identifikaciju
        include = JsonTypeInfo.As.PROPERTY, // Dodaje "@type" polje u JSON
        property = "@type"               // Ime polja koje sadrži tip
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DraftNodeComposite.class, name = "DraftNodeComposite"),
        @JsonSubTypes.Type(value = Building.class, name = "Building"),
        @JsonSubTypes.Type(value = Room.class, name = "Room"),
        @JsonSubTypes.Type(value = RoomElement.class, name = "RoomElement"),
        @JsonSubTypes.Type(value = Bojler.class, name = "Bojler"),
        @JsonSubTypes.Type(value = Kada.class, name = "Kada"),
        @JsonSubTypes.Type(value = Krevet.class, name = "Krevet"),
        @JsonSubTypes.Type(value = Lavabo.class, name = "Lavabo"),
        @JsonSubTypes.Type(value = Ormar.class, name = "Ormar"),
        @JsonSubTypes.Type(value = Sto.class, name = "Sto"),
        @JsonSubTypes.Type(value = VesMasina.class, name = "VesMasina"),
        @JsonSubTypes.Type(value = Vrata.class, name = "Vrata"),
        @JsonSubTypes.Type(value = WCSolja.class, name = "WCSolja")
})
@Data@NoArgsConstructor
public abstract class DraftNode implements IPublisher {

    @JsonIgnore
    transient ArrayList<ISubscriber> subscriberList = new ArrayList<>();

    private String ime;

    @JsonIgnore
    private transient DraftNode parent;

    public DraftNode(String ime, DraftNode parent) {
        this.ime = ime;
        this.parent = parent;
    }

    @Override
    public void addSubscriber(ISubscriber subscriber) {
        if(!subscriberList.contains(subscriber)){
            subscriberList.add(subscriber);
        }
    }

    @Override
    public void removeSubscriber(ISubscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    @Override
    public void notifySubscribers(Notification notification) {
//        for(ISubscriber subscriber : subscriberList){
//            subscriber.update(notification);
//        }
        for(int i = 0; i < subscriberList.size(); i++){
            subscriberList.get(i).update(notification);
        }
    }
}
