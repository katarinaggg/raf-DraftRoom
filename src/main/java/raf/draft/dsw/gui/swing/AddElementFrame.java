package raf.draft.dsw.gui.swing;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.notification.MessageType;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AddElementFrame {
    String element;

    public AddElementFrame() {
        element = typeOfElement();
    }

    public String typeOfElement() {
        String[] elements = {"Bojler", "Kada/Tus kabina", "Krevet", "Lavabo", "Ormar", "Sto", "Ves masina", "Vrata", "WC solja"};

        Icon[] icons = {
                loadIcon("namestaj/toilet.png"),
                loadIcon("namestaj/door.png"),
                loadIcon("namestaj/laundry-machine.png"),
                loadIcon("namestaj/desk.png"),
                loadIcon("namestaj/closet.png"),
                loadIcon("namestaj/sink.png"),
                loadIcon("namestaj/double-bed.png"),
                loadIcon("namestaj/bath.png"),
                loadIcon("namestaj/boiler.png")
        };


        // Kreiranje panela za svaku opciju
        JPanel[] options = new JPanel[elements.length];
        for (int i = 0; i < elements.length; i++) {
            options[i] = new JPanel();
            options[i].add(new JLabel(icons[i]));
            options[i].add(new JLabel(elements[i]));
        }

        // Prikazivanje JOptionPane dialoga za izbor opcije
        int choice = JOptionPane.showOptionDialog(
                ///da li treba da bude parent component null ili MainFrame
                MainFrame.getInstance(),
                "Odaberite element koji zelite da dodate :",
                "Element types",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);


        // Vraćanje izabranog elementa (ili null ako ništa nije izabrano)
        if (choice >= 0 && choice < elements.length) {
            return elements[choice];
        } else {
            // Ako korisnik zatvori dijalog ili ne odabere ništa
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Niste izabrali nijedan element!", MessageType.WARNING, LocalDateTime.now()
            );
            return null;
        }
    }


    // Privatna metoda za učitavanje ikonice
    private Icon loadIcon(String path) {
        try {
            return new ImageIcon(getClass().getResource("/" + path));
        } catch (Exception e) {
            System.err.println("Ikonica nije učitana sa putanje: " + path);
            return UIManager.getIcon("OptionPane.errorIcon");
        }
    }
}

//        // Kreiranje objekta na osnovu izbora
//        switch (choice) {
//            case 0:
//                return "Bojler";
//            case 1:
//                return "Kada/Tus kabina";
//            case 2:
//                return "Krevet";
//            case 3:
//                return "Lavabo";
//            case 4:
//                return "Ormar";
//            case 5:
//                return "Sto";
//            case 6:
//                return "Ves masina";
//            case 7:
//                return "Vrata";
//            case 8:
//                return "WC solja";
//            default:
//                // ne radi ovde?
//                ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste izabrali nijedan element!", MessageType.WARNING, LocalDateTime.now());
//                return null; // Ako korisnik zatvori dialog ili ne odabere ništa
//        }