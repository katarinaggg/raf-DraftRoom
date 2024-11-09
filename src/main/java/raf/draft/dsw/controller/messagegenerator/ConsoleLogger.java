package raf.draft.dsw.controller.messagegenerator;

import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.Notification;

public class ConsoleLogger implements Logger{
    @Override
    public void update(Notification notification) {
        System.out.println(notification.getMessage());
    }
}
