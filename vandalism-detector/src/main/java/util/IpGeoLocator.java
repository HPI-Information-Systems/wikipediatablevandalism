package util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Map an IP address onto a geo location.
 */
@Slf4j
public class IpGeoLocator {

  private static final String GEO_DB = "GeoLite2-City_20180703/GeoLite2-City.mmdb";

  private final DatabaseReader reader;

  public IpGeoLocator() {
    reader = createDatabaseReader();
  }

  @Nullable
  public Location findLocation(final String rawIp) {
    final InetAddress ip = toInetAddress(rawIp);
    return findLocation(ip);
  }

  @Nullable
  private Location findLocation(final InetAddress address) {
    try {
      final CityResponse city = reader.city(address);
      log.trace("Located user with IP address {} to {}", address, city);
      return city.getLocation();
    } catch (IOException | GeoIp2Exception e) {
      log.error("Could not geolocate IP", e);
      return null;
    }
  }

  private DatabaseReader createDatabaseReader() {
    try (val input = getClass().getClassLoader().getResourceAsStream(GEO_DB)) {
      return new DatabaseReader.Builder(input).build();
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private InetAddress toInetAddress(final String ip) {
    try {
      return InetAddress.getByName(ip);
    } catch (final UnknownHostException e) {
      throw new RuntimeException("Cannot convert IP string", e);
    }
  }
}
