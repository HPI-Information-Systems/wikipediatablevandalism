package parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class PagePathFinder {

  private final Path path;

  public Map<Integer, Path> findAll(Collection<Integer> pageIds) {
    try {
      val pageFileVisitor = new PageFileVisitor(pageIds);
      Files.walkFileTree(path, pageFileVisitor);
      return pageFileVisitor.getParsedPages();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
