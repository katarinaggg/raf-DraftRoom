package raf.draft.dsw.gui.swing;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.painter.RoomPainter;
import raf.draft.dsw.model.structures.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class DimensionFrame extends JDialog {

    // cuva RoomView za koji je otvoren
    // da bi iz njega izvukli sobu
    // kojoj treba da podesimo dimenzije
    private RoomView roomView;

    public DimensionFrame(RoomView roomView) {

        this.roomView = roomView;
        setTitle("Dimension");
        setSize(300, 200);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label za dužinu
        JLabel lengthLabel = new JLabel("Length:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lengthLabel, gbc);

        // Polje za unos dužine
        JTextField lengthField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(lengthField, gbc);

        // Label za širinu
        JLabel widthLabel = new JLabel("Width:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(widthLabel, gbc);

        // Polje za unos širine
        JTextField widthField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(widthField, gbc);

        // Dugme Save
        JButton saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // na dugme Save treba da se setuje duzina i sirina sobe na
        // trenutno otvorenom RoomView
        saveButton.addActionListener(e -> {
            try {
                Integer duzina = Integer.parseInt(lengthField.getText());
                Integer sirina = Integer.parseInt(widthField.getText());

                if(duzina<=0 || sirina<=0){
                    Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli odgovarajuce podatke!", MessageType.ERROR, LocalDateTime.now());
                    ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                    return;
                }

//                System.out.println(sirina);
//                System.out.println(duzina);

                // ne pravimo novi objekat soba vec je to ona ciji je tab otvoren i samo treba da
                // joj setujemo duzinu i sirinu koju smo dobili s prozora??

                // krsi MVC ???
                roomView.getRoom().setDuzina(duzina);
                roomView.getRoom().setSirina(sirina);


                Room room = roomView.getRoom();
                RoomPainter roomPainter = new RoomPainter(room,roomView);
                roomView.addPainter(roomPainter);
                roomView.repaint();

                // ako je validan unos da se izadje iz prozora
                dispose();

            }catch (NumberFormatException e1) {
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli odgovarajuce podatke!", MessageType.ERROR, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            }
        });

        add(saveButton, gbc);

        //pack();
        setLocationRelativeTo(null);



    }
}
