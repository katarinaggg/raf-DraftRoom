package raf.draft.dsw.controller.messagegenerator;

import raf.draft.dsw.model.messages.Message;

public class ConsoleLogger implements Logger{
    @Override
    public void update(Message message) {
        System.out.println(message);
    }
}
