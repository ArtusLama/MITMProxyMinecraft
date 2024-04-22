package de.artus.packets.c2s;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.BooleanField;
import de.artus.packets.fields.StringField;
import de.artus.packets.fields.UUIDField;
import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;


@Slf4j
@Getter
public class LoginStartPacket extends Packet {

    // Mojang did too much weird things with the fields!

    private StringField playername = new StringField();
    private Optional<UUIDField> playerUUID = Optional.empty();

    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        if (version.isInRange(MinecraftVersion.MINECRAFT_1_19, MinecraftVersion.MINECRAFT_1_19_1)) {
            // Mojang did VEEERRYY weird things with the fields there! So these versions are not supported.
            log.error("Unsupported version: " + version);
            return null;
        }

        getPlayername().read(stream);

        if (version.isInRange(MinecraftVersion.MINECRAFT_1_19_3, MinecraftVersion.MINECRAFT_1_20)) {
            boolean hasUUID = new BooleanField().read(stream).getValue();
            if (hasUUID) playerUUID = Optional.of(new UUIDField().read(stream));
        } else if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_2)) {
            playerUUID = Optional.of(new UUIDField().read(stream));
        }

        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        // NOT TESTED YET!
        getPlayername().write(stream);

        if (version.isInRange(MinecraftVersion.MINECRAFT_1_19_3, MinecraftVersion.MINECRAFT_1_20)) {
            new BooleanField(getPlayerUUID().isPresent()).write(stream);
            if (getPlayerUUID().isPresent()) getPlayerUUID().get().write(stream);

        } else if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_2)) {
            getPlayerUUID().get().write(stream);
        }
    }
}
