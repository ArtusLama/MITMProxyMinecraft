package de.artus.packets.fields;


import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class ByteArrayField implements FieldType<byte[]> {

    @Accessors(chain = true)
    @Getter @Setter
    private byte[] value;

    @Accessors(chain = true)
    @Getter @Setter
    private int length;

    @Override
    public ByteArrayField read(ByteBuf stream) throws IOException {
        byte[] bytes = new byte[getLength()];
        stream.readBytes(bytes);
        setValue(bytes);

        return this;
    }

    @Override
    public ByteArrayField write(ByteBuf stream) throws IOException {
        stream.writeBytes(getValue());

        return this;
    }

}
