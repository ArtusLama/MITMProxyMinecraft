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
public class LongField implements FieldType<Long> {

    @Accessors(chain = true)
    @Getter @Setter
    private Long value;

    @Override
    public LongField read(ByteBuf stream) throws IOException {
        setValue(stream.readLong());
        return this;
    }

    @Override
    public LongField write(ByteBuf stream) throws IOException {
        stream.writeLong(getValue());
        return this;
    }
}
