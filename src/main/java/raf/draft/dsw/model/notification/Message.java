package raf.draft.dsw.model.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data@NoArgsConstructor@AllArgsConstructor
public class Message {

    // sadrzaj poruke
    private String sadrzaj;
    // tip
    private MessageType tip;
    // vreme greske
    private LocalDateTime timestamp;


    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        return "["+tip+"] ["+timestamp.format(dtf)+"] "+"["+sadrzaj+"]";
    }
}
