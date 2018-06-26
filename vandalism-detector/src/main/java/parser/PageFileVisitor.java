package parser;

import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class PageFileVisitor extends SimpleFileVisitor<Path> {
    private List<Integer> pageIds;
    private Map<Integer, Path> parsedPages;

    public PageFileVisitor(List<Integer> pageIds) {
        this.pageIds = pageIds;
        this.parsedPages = new HashMap<>();
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
        val fileName = path.getFileName().toString();

        if (!fileName.endsWith(".parsed")) {
            return FileVisitResult.CONTINUE;
        }

        val pageId = Integer.valueOf(fileName.split("-")[0]);

        if (pageIds.contains(pageId)) {
            Path previous = parsedPages.put(pageId, path);
            if (previous != null) {
                throw new IllegalStateException("Page id not unique");
            }
        }

        if (parsedPages.keySet().containsAll(pageIds)) {
            return FileVisitResult.TERMINATE;
        }

        return FileVisitResult.CONTINUE;
    }

    public Map<Integer, Path> getParsedPages() {
        return parsedPages;
    }
}
