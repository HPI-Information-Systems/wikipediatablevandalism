package util;

import com.esotericsoftware.kryo.Kryo;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.JdkProxySerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import de.javakaffee.kryoserializers.guava.ArrayListMultimapSerializer;
import de.javakaffee.kryoserializers.guava.HashMultimapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableListSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableMapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableMultimapSerializer;
import de.javakaffee.kryoserializers.guava.ImmutableSetSerializer;
import de.javakaffee.kryoserializers.guava.LinkedHashMultimapSerializer;
import de.javakaffee.kryoserializers.guava.LinkedListMultimapSerializer;
import de.javakaffee.kryoserializers.guava.ReverseListSerializer;
import de.javakaffee.kryoserializers.guava.TreeMultimapSerializer;
import de.javakaffee.kryoserializers.guava.UnmodifiableNavigableSetSerializer;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class KryoUtil {

  public static Kryo createKryo() {
    final Kryo kryo = new Kryo();

    // copy from https://github.com/magro/kryo-serializers#usage
    // (except for those which are seemingly included in Kryo by default)
    kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
    kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
    kryo.register(InvocationHandler.class, new JdkProxySerializer());
    UnmodifiableCollectionsSerializer.registerSerializers(kryo);
    SynchronizedCollectionsSerializer.registerSerializers(kryo);

    // guava ImmutableList, ImmutableSet, ImmutableMap, ImmutableMultimap, ReverseList, UnmodifiableNavigableSet
    ImmutableListSerializer.registerSerializers(kryo);
    ImmutableSetSerializer.registerSerializers(kryo);
    ImmutableMapSerializer.registerSerializers(kryo);
    ImmutableMultimapSerializer.registerSerializers(kryo);
    ReverseListSerializer.registerSerializers(kryo);
    UnmodifiableNavigableSetSerializer.registerSerializers(kryo);
    // guava ArrayListMultimap, HashMultimap, LinkedHashMultimap, LinkedListMultimap, TreeMultimap
    ArrayListMultimapSerializer.registerSerializers(kryo);
    HashMultimapSerializer.registerSerializers(kryo);
    LinkedHashMultimapSerializer.registerSerializers(kryo);
    LinkedListMultimapSerializer.registerSerializers(kryo);
    TreeMultimapSerializer.registerSerializers(kryo);

    return kryo;
  }

}
