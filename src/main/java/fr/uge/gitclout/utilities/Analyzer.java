package fr.uge.gitclout.utilities;

import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Analyzer {
    private final Git git;

    public Analyzer(@NotNull Git git) {
        this.git = git;
    }

    public void analyze(List<Ref> tags, RevWalk revWalk) throws GitAPIException, IOException {
        try (var reader = git.getRepository().newObjectReader()) {
            for (var i = 0; i < tags.size() - 1; i++) {
                CanonicalTreeParser tags1 = new CanonicalTreeParser();
                tags1.reset(reader, revWalk.parseCommit(tags.get(i).getObjectId()).getTree().getId());
                CanonicalTreeParser tags2 = new CanonicalTreeParser();
                tags2.reset(reader, revWalk.parseCommit(tags.get(i+1).getObjectId()).getTree().getId());
                DiffFormatter df = new DiffFormatter( new ByteArrayOutputStream() ); // use NullOutputStream.INSTANCE if you don't need the diff output
                df.setRepository( git.getRepository() );
                List<DiffEntry> entries = df.scan( tags1, tags2 );
                for( DiffEntry entry : entries ) {
                    System.out.println( entry );
                }
            }
        }
    }
}
