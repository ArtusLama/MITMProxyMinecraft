package de.artus.packets.fields.custom;

import de.artus.packets.fields.LongField;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class CurrentTimeMillisLongField extends LongField {

    @Override
    public LongField write(ByteBuf stream) throws IOException {
        setValue(System.currentTimeMillis());
        return super.write(stream);
    }
}
