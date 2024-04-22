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
public class BooleanField implements FieldType<Boolean> {

    @Accessors(chain = true)
    @Getter @Setter
    private Boolean value;


    @Override
    public BooleanField read(ByteBuf stream) throws IOException {
        setValue(stream.readBoolean());

        return this;
    }

    @Override
    public BooleanField write(ByteBuf stream) throws IOException {
        stream.writeBoolean(getValue());
        return this;
    }
}
