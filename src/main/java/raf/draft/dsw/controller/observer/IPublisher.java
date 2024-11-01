package raf.draft.dsw.controller.observer;

import lombok.Data;
import raf.draft.dsw.controller.messagegenerator.MessageGenerator;
import raf.draft.dsw.model.messages.Message;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class IPublisher {

    public abstract void addSubscriber(ISubscriber subscriber);
    public abstract void removeSubscriber(ISubscriber subscriber);
    public abstract void notifySubscribers(Message message);     // mozda

}
