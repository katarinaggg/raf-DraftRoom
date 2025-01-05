package raf.draft.dsw.gui.swing;

import lombok.Getter;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Getter
public class OrganizeMyRoom extends JDialog {


    DefaultListModel<RoomElement> listaElemenata  =new DefaultListModel<>();

    @Getter
    private List<String> roomElements; // Lista sa elementima sobe
    private JTextField lengthField;   // Polje za unos dužine
    private JTextField widthField;    // Polje za unos širine
    private JComboBox<String> elementComboBox; // ComboBox za odabir elementa
    private DefaultListModel<String> listModel; // Model za prikazivanje liste
    private JList<String> elementList; // Prikaz elemenata u listi
    @Getter
    private boolean submitted;        // Da li je kliknuto na Submit

    @Getter
    Map<String, Dimension> roomElementsMap = new HashMap<>();

    // da mozemo da dodajemo iste elemente
    // po necemu da razlikujemo imena
    public int counter = 1;

    public OrganizeMyRoom(Frame parent) {

        super(parent, "Dodajte elemente u sobu", true);

        roomElements = new ArrayList<>();
        submitted = false;

        initDialog();
    }

    private void initDialog() {
        // Mapa za čuvanje elemenata sobe sa dimenzijama
        //Map<String, Dimension> roomElementsMap = new HashMap<>();

        // Postavljanje osnovnog layout-a
        setLayout(new BorderLayout(10, 10));

        // Panel za unos dimenzija i izbor elementa
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        inputPanel.add(new JLabel("Dužina (cm):"));
        lengthField = new JTextField();
        inputPanel.add(lengthField);

        inputPanel.add(new JLabel("Širina (cm):"));
        widthField = new JTextField();
        inputPanel.add(widthField);

        inputPanel.add(new JLabel("Element sobe:"));
        elementComboBox = new JComboBox<>(new String[]{"Bojler", "Kada", "Krevet", "Lavabo", "Ormar", "Sto", "VesMasina", "Vrata", "WCSolja"});
        inputPanel.add(elementComboBox);

        add(inputPanel, BorderLayout.NORTH);

        // Panel za prikaz i manipulaciju liste
        listModel = new DefaultListModel<>();
        elementList = new JList<>(listModel);
        elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(elementList);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Dodati elementi:"), BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // Dugmad za manipulaciju liste
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        JButton addButton = new JButton("Dodaj");
        addButton.addActionListener(e -> addElementToMap(roomElementsMap));
        buttonPanel.add(addButton);

        JButton removeButton = new JButton("Ukloni");
        removeButton.addActionListener(e -> removeElementFromMap(roomElementsMap));
        buttonPanel.add(removeButton);

        listPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(listPanel, BorderLayout.CENTER);

        // Panel za potvrdu
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Sacuvaj");
        submitButton.addActionListener(e -> submit(roomElementsMap));
        submitPanel.add(submitButton);

        JButton cancelButton = new JButton("Odustani");
        cancelButton.addActionListener(e -> cancel());
        submitPanel.add(cancelButton);

        add(submitPanel, BorderLayout.SOUTH);

        JButton randomizeButton = new JButton("Randomizuj");
        randomizeButton.addActionListener(e -> randomizeElements());
        buttonPanel.add(randomizeButton);


        // Podešavanje dijaloga
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setVisible(true);
    }

    private void addElementToMap(Map<String, Dimension> roomElementsMap) {
        String length = lengthField.getText().trim();
        String width = widthField.getText().trim();
        String selectedElement = (String) elementComboBox.getSelectedItem() +" "+ counter++;

        if (length.isEmpty() || width.isEmpty() || selectedElement == null) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Molimo popunite sva polja!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        try {
            int l = Integer.parseInt(length);
            int w = Integer.parseInt(width);

            if (l <= 0 || w <= 0) {
                throw new NumberFormatException();
            }

            String dimensionsS = l + "x" + w; // Dimenzije u formatu "dužina x širina"
            Dimension dimensions = new Dimension(w, l);
            roomElementsMap.put(selectedElement, dimensions); // Čuvanje u mapi

            String entry = selectedElement + " (" + dimensionsS + ")";
            listModel.addElement(entry); // Dodavanje u prikaz liste
        } catch (NumberFormatException e) {
            Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Unesite validne brojeve za dimenzije!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));

        }
    }

    private void removeElementFromMap(Map<String, Dimension> roomElementsMap) {
        int selectedIndex = elementList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedItem = listModel.get(selectedIndex);
            String elementName = selectedItem.split(" \\(")[0]; // Ekstraktovanje naziva elementa

            roomElementsMap.remove(elementName); // Uklanjanje iz mape
            listModel.remove(selectedIndex); // Uklanjanje iz prikaza
        } else {
            Message message = new Message("Nijedan element nije selektovan!", MessageType.ERROR, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
        }
    }

    private void submit(Map<String, Dimension> roomElementsMap) {
        if (roomElementsMap.isEmpty()) {
            Message message = new Message("Niste dodali nijedan element!", MessageType.WARNING, LocalDateTime.now());
            ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            return;
        }

        // Ispis mape za debugging
        System.out.println("Sadržaj mape pre zatvaranja dijaloga:");
        roomElementsMap.forEach((key, value) -> {
            System.out.println("Element: " + key + ", Dimenzije: " + value);
        });

        submitted = true;

        // Dalja logika za zatvaranje dijaloga i vraćanje mape
        dispose();
    }

    private void randomizeElements() {
        List<String> elements = new ArrayList<>(roomElementsMap.keySet());
        Collections.shuffle(elements); // Randomizacija elemenata
        listModel.clear();
        roomElementsMap.clear();

        for (String element : elements) {
            Dimension dimension = roomElementsMap.get(element);
            roomElementsMap.put(element, dimension);
            listModel.addElement(element + " (" + dimension.width + "x" + dimension.height + ")");
        }
    }

    private void cancel() {
        submitted = false;
        dispose();
    }

}