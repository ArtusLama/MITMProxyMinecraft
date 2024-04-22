package de.artus.packets.s2c;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.JsonTextComponentField;
import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
public class DisconnectPacket extends Packet {

    @Getter
    private JsonTextComponentField reason = new JsonTextComponentField();


    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getReason().read(stream);

        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getReason().write(stream);
    }
}
