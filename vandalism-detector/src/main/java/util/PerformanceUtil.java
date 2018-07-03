package util;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceUtil {

  public static void runMeasured(final String name, final Runnable runnable) {
    final Supplier<Void> supplier = () -> {
      runnable.run();
      return null;
    };

    runMeasured(name, supplier);
  }

  public static <T> T runMeasured(final String name, final Supplier<T> supplier) {
    final long t0 = System.nanoTime();
    try {
      return supplier.get();
    } finally {
      final long t1 = System.nanoTime();
      final long ms = TimeUnit.MILLISECONDS.convert(t1 - t0, TimeUnit.NANOSECONDS);
      log.debug("{} took {} ms", name, ms);
    }
  }

}
