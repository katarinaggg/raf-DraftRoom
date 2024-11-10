package raf.draft.dsw.model.notification;

import raf.draft.dsw.model.nodes.DraftNode;

public class Notification {
    Message message;
    DraftNode draftNode;
    Boolean brisanje=false;

    public Notification(Message message) {
        this.message = message;
    }

    public Notification(DraftNode draftNode) {
        this.draftNode = draftNode;
    }

    public Notification(DraftNode draftNode, Boolean brisanje) {
        this.draftNode = draftNode;
        this.brisanje = brisanje;
    }

    public Notification(Message message, DraftNode draftNode) {
        this.message = message;
        this.draftNode = draftNode;
    }

    public Message getMessage() {
        return message;
    }

    public DraftNode getDraftNode() {
        return draftNode;
    }

    public Boolean getBrisanje() {
        return brisanje;
    }
}
