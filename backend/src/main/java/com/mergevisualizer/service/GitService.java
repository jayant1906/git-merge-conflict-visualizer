package com.mergevisualizer.service;

import com.mergevisualizer.model.BranchInfo;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

@Service
public class GitService {

    public GitService() {
    }

    public List<BranchInfo> getBranches(Path repositoryPath) throws IOException, GitAPIException {
        Path repoRoot = findRepositoryRoot(repositoryPath);

        try(Git git = Git.open(repoRoot.toFile())){
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

    public Path findRepositoryRoot(Path extractedFolder) throws IOException{
        Path gitFolder = extractedFolder.resolve(".git");
        if (Files.isDirectory(gitFolder)) {
            return extractedFolder;
        }
        try (Stream<Path> entries = Files.list(extractedFolder)){
            List<Path> subDirs = entries.filter(Files::isDirectory).collect(Collectors.toList());
            for(Path dir:subDirs){
                Path tempGitFolder = dir.resolve(".git");
                if (Files.isDirectory(tempGitFolder)){
                    return dir;
                }
            }
            for (Path dir: subDirs){
                Path result = findRepositoryRoot(dir);
                if (result != null){
                    return result;
                }
            }
        }
        throw new IOException("No Git repository found in " + extractedFolder);
    }
}
