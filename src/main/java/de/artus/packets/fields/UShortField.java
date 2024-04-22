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
public class UShortField implements FieldType<Integer> {

    @Accessors(chain = true)
    @Getter @Setter
    private Integer value;


    @Override
    public UShortField read(ByteBuf stream) throws IOException {
        setValue(stream.readUnsignedShort());

        return this;
    }

    @Override
    public UShortField write(ByteBuf stream) throws IOException {
        stream.writeShort(getValue());

        return this;
    }
}
