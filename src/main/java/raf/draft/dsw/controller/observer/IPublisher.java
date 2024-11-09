package raf.draft.dsw.controller.observer;

import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.Notification;

public interface IPublisher {

    void addSubscriber(ISubscriber subscriber);
    void removeSubscriber(ISubscriber subscriber);
    void notifySubscribers(Notification notification);

}
