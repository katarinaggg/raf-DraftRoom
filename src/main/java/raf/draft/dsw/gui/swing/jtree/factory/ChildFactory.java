package raf.draft.dsw.gui.swing.jtree.factory;

import lombok.Data;
import lombok.NoArgsConstructor;
import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.gui.swing.AddNodeFrame;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.structures.Building;
import raf.draft.dsw.model.structures.Project;
import raf.draft.dsw.model.structures.ProjectExplorer;

@Data@NoArgsConstructor
public class ChildFactory {

    public DraftNode createChild(DraftNode parent){
        if(parent instanceof ProjectExplorer){
            return ApplicationFramework.getInstance().getDraftRoomRepositoryImplementation().getFactoryUtils().getFactory("Project").createDraftNode(parent);
        }
        else if(parent instanceof Project){
            AddNodeFrame addNodeFrame = new AddNodeFrame();
            String type = addNodeFrame.getTip();
            if(type==null){
                // nece da prikaze poruku???
                //ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste izabrali nijedan objekat!", MessageType.WARNING,LocalDateTime.now());
                return null;
            }
            else{
                return ApplicationFramework.getInstance().getDraftRoomRepositoryImplementation().getFactoryUtils().getFactory(type).createDraftNode(parent);
            }
        }
        else if(parent instanceof Building){
            return ApplicationFramework.getInstance().getDraftRoomRepositoryImplementation().getFactoryUtils().getFactory("Room").createDraftNode(parent);
        }
        return null;
    }
}
