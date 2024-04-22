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
public class VarIntField implements FieldType<Integer> {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    @Accessors(chain = true)
    @Getter @Setter
    private Integer value;


    @Override
    public VarIntField read(ByteBuf stream) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = stream.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        setValue(value);
        return this;
    }

    @Override
    public VarIntField write(ByteBuf stream) throws IOException {
        while (true) {
            if ((getValue() & ~SEGMENT_BITS) == 0) {
                stream.writeByte(getValue());
                return this;
            }

            stream.writeByte((getValue() & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            setValue(getValue() >>> 7);
        }
    }

}
