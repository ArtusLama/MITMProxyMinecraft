package de.artus.packets.fields;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class UUIDField implements FieldType<UUID> {


    @Accessors(chain = true)
    @Getter @Setter
    private UUID value;

    @Override
    public UUIDField read(ByteBuf stream) throws IOException {
        long mostSigBits = stream.readLong();
        long leastSigBits = stream.readLong();
        setValue(new UUID(mostSigBits, leastSigBits));


        return this;
    }

    @Override
    public UUIDField write(ByteBuf stream) throws IOException {
        stream.writeLong(getValue().getMostSignificantBits());
        stream.writeLong(getValue().getLeastSignificantBits());

        return this;
    }
}
