package de.artus.proxy.server.handlers;

import de.artus.packets.PacketRegistry;
import de.artus.packets.c2s.HandshakePacket;
import de.artus.packets.fields.StringField;
import de.artus.packets.s2c.StatusResponsePacket;
import de.artus.proxy.MinecraftVersion;
import de.artus.proxy.server.ClientConnection;
import de.artus.util.network.DisconnectPacketHelper;
import de.artus.util.network.ServerListStatus;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;

@Slf4j
public class HandshakeHandler extends PacketHandler<HandshakePacket> {


    @Override
    public void onPacket(HandshakePacket packet, ClientConnection client) {

        log.info("Received HandshakePacket!");

        client.setState(packet.getNextState().getValue() == 1 ? PacketRegistry.PacketState.STATUS : PacketRegistry.PacketState.LOGIN);
        client.setVersion(MinecraftVersion.getFromProtocolVersion(packet.getProtocolVersion().getValue()));
        log.info("Using protocol version {} for client => {}", client.getVersion().getProtocolVersion(), client.getState().name());
    }


}
