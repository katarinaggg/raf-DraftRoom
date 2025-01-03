package raf.draft.dsw.gui.swing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.controller.actions.ActionManager;
import raf.draft.dsw.controller.observer.ISubscriber;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.jtree.DraftTree;
import raf.draft.dsw.gui.swing.jtree.DraftTreeImplementation;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MainFrame extends JFrame implements ISubscriber {
    //buduca polja za sve komponente view-a na glavnom prozoru

    private static MainFrame mainFrameInstance;
    private ActionManager actionManager;
    private DraftTree draftTree;

    // valjda se cuva trenutni otvoreni
    private ProjectView projectView;
    private JSplitPane splitPane;

    private Project clickedProject;

    // mora da ima mapu svih ProjectView-ova koji su trenutno otvoreni
    // kljuc je odgovarajuci Project
    // kada "setamo" kroz projekte da nam se ne izbrisu crtezi
    private Map<Project, ProjectView> projectViewMap = new HashMap<>();

    private MainFrame(){
    }

    public static MainFrame getInstance(){
        if(mainFrameInstance == null){
            mainFrameInstance = new MainFrame();
            mainFrameInstance.initialize();

        }
        return mainFrameInstance;
    }

    private void initialize(){
        //ovde sam inicijalizovala actionManager, jer ovde pravim jedini objekat MainFrame-a
        // tj. sve sto taj objekat treba da ima
        actionManager=new ActionManager();
        draftTree = new DraftTreeImplementation();

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("DraftRoom");

        // subscribujemo MainFrame na MessageGenerator koji je IPublisher
        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);

        MyMenuBar menu = new MyMenuBar();
        setJMenuBar(menu);

        MyToolBar toolBar = new MyToolBar();
        add(toolBar, BorderLayout.NORTH);

        // ovde pravimo stablo, vraca se JTreeView
        JTree  projectExplorer =  draftTree.generate(ApplicationFramework.getInstance().getDraftRoomRepositoryImplementation().getRoot());

        // dodajemo MouseListener na model (treba nam za dvoklik na Project)
        // moze i ovde ali bolje u JTreeView
        //projectExplorer.addMouseListener(new SelectedProjectListener(projectExplorer));

        //ovde ubacujemo projectExplorer tj. root projekta u scrollPane
        JScrollPane scroll = new JScrollPane(projectExplorer);
        scroll.setMinimumSize(new Dimension(200,150));

        projectView = new ProjectView();

        // ekran delimo na levi deo sa scrollbarom (i JTree) i desni sa tabovima
        // ali prvo ubacujemo neki random panel a projectView se prikazuje tek nakon dvoklika
        splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scroll,projectView);

        splitPane.setDividerLocation(250);
        splitPane.setOneTouchExpandable(true);
        getContentPane().add(splitPane,BorderLayout.CENTER);
    }

    // metoda za prikazivanje ProjectViewa
    public void displayProjectView(Project project) {
        //System.out.println("Opening ProjectView for project: " + project.getIme() + " (ID: " + project.getId() + ")");

        // proveravamo da li vec postoji ProjectView za selektovani Project

        if(!projectViewMap.containsKey(project)) {
            // ako ne postoji pravimo novi
            //System.out.println("Creating new ProjectView for project: " + project.getIme());

            // kada imamo neki projekat koji vec u sebi ima podstabla
            // Kreiramo novi ProjectView sa selektovanim projektom
            ProjectView newProjectView = new ProjectView(project);// Inicijalizacija tek kada je projekat izabran

            // ProjectView je subscriber kojeg dodajemo na sve nodove iz selektovanog projekta
            // koji se prikazuje u tom projectView
            project.addSubscriber(newProjectView);  // dodajemo ga selektovanom Projectu
            for (DraftNode child : project.getChildren()) {
                if (child instanceof Building) {
                    // ako je building subscribujemo ProjectView i na svu njegovu decu
                    for (DraftNode child2 : ((Building) child).getChildren()) {
                        child2.addSubscriber(newProjectView);
                    }
                }
                // i na zasebne sobe (ili building)
                child.addSubscriber(newProjectView);
            }
            // dodajemo novi projectView u mapu
            projectViewMap.put(project, newProjectView);
        }

        projectView = projectViewMap.get(project);
        //System.out.println("Using ProjectView for project: " + project.getIme() + " (ID: " + project.getId() + ")");
        for (Project project1 : projectViewMap.keySet()) {
            System.out.println("Project key: " + project1.getIme()); // ili neki drugi atribut ključa
        }

        // Postavljamo ProjectView u desni deo JSplitPane
        splitPane.setRightComponent(projectView);

        // project je selektovani project
        // moramo da pozovemo da se prikazu tabovi
        projectView.displayRooms(project);

        splitPane.revalidate();  // Osvežavanje SplitPane-a
        splitPane.repaint();
        draftTree.refreshTree(); // da refresuje stablo (jer se zatvore podstabla)
    }

    @Override
    public void update(Notification notification) {
        Message message = notification.getMessage();
        if(message.getTip().equals(MessageType.ERROR)){
            JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.ERROR_MESSAGE);
        }
        else if(message.getTip().equals(MessageType.WARNING)){
            JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.WARNING_MESSAGE);
        }
        else if(message.getTip().equals(MessageType.INFORMATION)){
            JOptionPane.showMessageDialog(this,message.getSadrzaj(),message.getTip().toString(),JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Project getClickedProject() {
        return clickedProject;
    }

    public void setClickedProject(Project clickedProject) {
        this.clickedProject = clickedProject;
    }
}
