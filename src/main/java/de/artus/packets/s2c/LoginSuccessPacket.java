package de.artus.packets.s2c;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.StringField;
import de.artus.packets.fields.UUIDField;
import de.artus.packets.fields.VarIntField;
import de.artus.packets.fields.XArrayField;
import de.artus.packets.fields.custom.PropertyField;
import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessPacket extends Packet {


    private UUIDField uuid = new UUIDField();
    private StringField username = new StringField();
    private VarIntField numberOfProperties = new VarIntField();
    private XArrayField<PropertyField> properties = new XArrayField<>();


    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getUuid().read(stream);
        getUsername().read(stream);
        getNumberOfProperties().read(stream);
        getProperties().setLength(getNumberOfProperties().getValue()).read(stream);
        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getUuid().write(stream);
        getUsername().write(stream);
        getNumberOfProperties().write(stream);
        getProperties().write(stream);
    }
}
