package parser;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import lombok.val;
import org.xerial.snappy.SnappyInputStream;
import wikixmlsplit.datastructures.MyPageType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class PageParser {
    private final Kryo kryo;

    public PageParser(Kryo kryo) {
        this.kryo = kryo;
    }

    public MyPageType parse(Path path) {
        try (val inputStream = new SnappyInputStream(new FileInputStream(path.toFile()))) {
            return kryo.readObject(new Input(inputStream), MyPageType.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
