package raf.draft.dsw.model.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import raf.draft.dsw.model.structures.ProjectExplorer;

@Getter
public class DraftRoomExplorerImplementation implements DraftRoomRepository{

    //jedan jedini projectExplorer samo tu ga pravimo, a cuvamo ga u ApplicationFramework-u
    private ProjectExplorer projectExplorer;

    public DraftRoomExplorerImplementation() {
        projectExplorer = new ProjectExplorer("Project explorer");
    }

    @Override
    public void getRoot() {
    }
}
