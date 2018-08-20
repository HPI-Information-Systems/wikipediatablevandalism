package matching.persistence;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import runner.Arguments;
import util.KryoUtil;
import wikixmlsplit.api.Matching;
import wikixmlsplit.datastructures.MyPageType;

/**
 * Read and write table matches to disk using Kryo.
 */
@Slf4j
class MatchingPersistence {

  private final Arguments arguments;
  private final KryoPool pool;

  MatchingPersistence(final Arguments arguments) {
    this.arguments = arguments;
    pool = new KryoPool.Builder(KryoUtil::createKryo).softReferences().build();
  }

  private boolean isMatchingAvailable(final MyPageType page) {
    val file = getPath(page);
    return file.toFile().exists();
  }

  void persist(final MyPageType page, final Matching matching, final int maxRevisionId) {
    val persistedMatching = toPersistedMatching(page, matching, maxRevisionId);
    val file = getPath(page);
    log.debug("Writing matching of page {} to {}", page.getId(), file);
    createDirectories(file);
    write(persistedMatching, file);
  }

  Optional<Matching> read(final MyPageType page, final int maxRevisionId) {
    if (isMatchingAvailable(page)) {
      val file = getPath(page);
      log.debug("Reading persisted matching for page {} from file {}", page.getId(), file);
      try {
        val persistedMatching = read(file);
        sanityCheck(persistedMatching, page, maxRevisionId);
        return Optional.of(persistedMatching.getMatching());
      } catch (final Exception e) {
        log.warn("Failed to read matching", e);
      }
    }
    return Optional.empty();
  }

  private void sanityCheck(final PersistedMatching matching, final MyPageType page,
      final int maxRevisionId) {

    if (!matching.getPageId().equals(page.getId())) {
      val message = String.format("Matching contained page ID %s, but filename implied %s",
          matching.getPageId(), page.getId());
      throw new IllegalStateException(message);
    }

    if (maxRevisionId > matching.getMaxRevisionId()) {
      val message = String.format(
          "Persisted matching was requested up to revision %s, but is only present up to %s",
          maxRevisionId, matching.getMaxRevisionId());
      throw new IllegalStateException(message);
    }
  }

  private Path getPath(final MyPageType page) {
    Objects.requireNonNull(arguments.getMatchingPath(), "Matching persistence path must be set");
    val filename = String.format("matching_%s.bin", page.getId());
    return arguments.getMatchingPath().resolve(filename);
  }

  private PersistedMatching toPersistedMatching(final MyPageType page, final Matching matching,
      final int maxRevisionId) {

    return PersistedMatching.builder()
        .pageId(page.getId())
        .maxRevisionId(maxRevisionId)
        .matching(matching)
        .build();
  }

  private void createDirectories(final Path path) {
    try {
      Files.createDirectories(path.getParent());
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void write(final PersistedMatching matching, final Path path) {
    Kryo kryo = pool.borrow();
    try (Output output = new Output(Files.newOutputStream(path))) {
      kryo.writeObject(output, matching);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    } finally {
      pool.release(kryo);
    }
  }

  private PersistedMatching read(final Path path) {
    Kryo kryo = pool.borrow();
    try (Input input = new Input(Files.newInputStream(path))) {
      return kryo.readObject(input, PersistedMatching.class);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    } finally {
      pool.release(kryo);
    }
  }
}
