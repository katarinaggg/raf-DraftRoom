package raf.draft.dsw.controller.observer;

import raf.draft.dsw.model.messages.Message;

public interface ISubscriber {

    void update(Message message);
}
