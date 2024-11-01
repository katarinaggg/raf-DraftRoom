package raf.draft.dsw.model.repository;

import lombok.Getter;
import raf.draft.dsw.model.structures.ProjectExplorer;

@Getter
public class DraftRoomExplorerImplementation implements DraftRoomRepository{

    private ProjectExplorer projectExplorer;

    @Override
    public void getRoot() {

    }
}
