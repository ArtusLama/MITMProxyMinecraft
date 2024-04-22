package de.artus.packets.fields;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class StringField implements FieldType<String> {

    @Accessors(chain = true)
    @Getter @Setter
    private String value;


    @Override
    public StringField read(ByteBuf stream) throws IOException {
        int length = new VarIntField().read(stream).getValue();
        log.trace("Reading String with len: {}", length);

        byte[] bytes = new byte[length];
        stream.readBytes(bytes);
        setValue(new String(bytes));
        
        log.trace("Read String \"{}\"", getValue());

        return this;
    }

    @Override
    public StringField write(ByteBuf stream) throws IOException {
        log.trace("Writing String ({})\n\"{}\"", getValue().getBytes().length, getValue());
        new VarIntField().setValue(getValue().getBytes().length).write(stream);
        stream.writeBytes(getValue().getBytes());

        return this;
    }
}
