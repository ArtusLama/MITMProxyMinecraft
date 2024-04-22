package de.artus.packets.fields;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

public interface FieldType<T> {

    T getValue();
    FieldType<T> read(ByteBuf stream) throws IOException;

    FieldType<T> write(ByteBuf stream) throws IOException;
}
