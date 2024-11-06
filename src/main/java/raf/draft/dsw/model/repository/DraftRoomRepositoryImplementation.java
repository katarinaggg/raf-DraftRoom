package raf.draft.dsw.model.repository;

import lombok.Getter;
import raf.draft.dsw.model.structures.ProjectExplorer;

@Getter
public class DraftRoomRepositoryImplementation implements DraftRoomRepository{

    // OVDE CUVAMO MODEL (imamo projectExplorer koji je root)

    //jedan jedini projectExplorer samo tu ga pravimo, a cuvamo ga u ApplicationFramework-u
    private ProjectExplorer projectExplorer;

    public DraftRoomRepositoryImplementation() {
        projectExplorer = new ProjectExplorer("Project Explorer");
    }

    @Override
    public void getRoot() {

    }
}
