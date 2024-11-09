package raf.draft.dsw.controller.observer;

import lombok.Data;
import raf.draft.dsw.controller.messagegenerator.MessageGenerator;
import raf.draft.dsw.model.messages.Message;

import java.util.ArrayList;
import java.util.List;

public interface IPublisher {

    void addSubscriber(ISubscriber subscriber);
    void removeSubscriber(ISubscriber subscriber);
    void notifySubscribers(Message message);

}
