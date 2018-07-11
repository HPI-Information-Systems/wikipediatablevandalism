package parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor
public class PagePathFinder {

  private final Path path;

  public Map<Integer, Path> findAll(Collection<Integer> pageIds) {
    try {
      log.info("Scanning page files from {}", path);
      val pageFileVisitor = new PageFileVisitor(pageIds);
      Files.walkFileTree(path, pageFileVisitor);
      return pageFileVisitor.getParsedPages();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
