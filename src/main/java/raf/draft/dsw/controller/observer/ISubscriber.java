package raf.draft.dsw.controller.observer;

import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.Notification;

public interface ISubscriber {

    void update(Notification notification);
}
