package parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import lombok.val;

public class PagePathFinder {

  private final String path;

  public PagePathFinder(String path) {
    this.path = path;
  }

  public Map<Integer, Path> findAll(List<Integer> pageIds) {
    try {
      val baseDir = Paths.get(path);
      val pageFileVisitor = new PageFileVisitor(pageIds);
      Files.walkFileTree(baseDir, pageFileVisitor);
      return pageFileVisitor.getParsedPages();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
