package raf.draft.dsw.model.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.observer.IPublisher;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.notification.Notification;

import java.util.ArrayList;

@Data
public abstract class DraftNode implements IPublisher {

    ArrayList<ISubscriber> subscriberList = new ArrayList<>();

    private String ime;
    private DraftNode parent;

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
        for(ISubscriber subscriber : subscriberList){
            subscriber.update(notification);
        }
//        for(int i = 0; i < subscriberList.size(); i++){
//            subscriberList.get(i).update(notification);
//        }
    }
}
