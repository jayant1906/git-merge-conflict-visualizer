package com.mergevisualizer.service;

import com.mergevisualizer.model.BranchInfo;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitService {

    public GitService() {
    }

    public List<BranchInfo> getBranches(Path repositoryPath) throws IOException, GitAPIException {
        try(Git git = Git.open(repositoryPath.toFile())){
            Repository repository = git.getRepository();
            List<Ref> branches = git.branchList().call();
            String currBranch = repository.getBranch();
            List<BranchInfo> branchInfo = new ArrayList<>();
            for(int i = 0; i < branches.size(); i++){
                String name = branches.get(i).getName();
                String name_resolved = Repository.shortenRefName(name);
                boolean isCurr = false;
                if(name_resolved.equals(currBranch)){
                    isCurr = true;
                }
                branchInfo.add(new BranchInfo(name_resolved, isCurr));
            }
            return branchInfo;
        }
    }
}
