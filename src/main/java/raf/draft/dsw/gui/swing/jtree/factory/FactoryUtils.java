package raf.draft.dsw.gui.swing.jtree.factory;

public class FactoryUtils {

    public DraftNodeFactory getFactory(String tip) {


        if(tip.equalsIgnoreCase("Project")){
            return new ProjectFactory();
        }
        if(tip.equalsIgnoreCase("Building")){
            return new BuildingFactory();
        }
        if(tip.equalsIgnoreCase("Room")){
            return new RoomFactory();
        }
        return null;
    }
}
