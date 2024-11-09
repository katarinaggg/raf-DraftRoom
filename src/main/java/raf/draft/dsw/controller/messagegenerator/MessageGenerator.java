package raf.draft.dsw.controller.messagegenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.observer.IPublisher;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.model.messages.Message;
import raf.draft.dsw.model.messages.MessageType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor@AllArgsConstructor
public class MessageGenerator implements IPublisher {

    // lista prijavljenih na IPublisher
    List<ISubscriber> subscriberList = new ArrayList<>();

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
    public void notifySubscribers(Message message) {
        for(ISubscriber subscriber : subscriberList){
            subscriber.update(message);
        }
    }

    public Message generateMessage(String sadrzaj, MessageType tip, LocalDateTime vreme){
        Message message = new Message(sadrzaj, tip, vreme);
        return message;
    }

}
