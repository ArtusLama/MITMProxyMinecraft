package de.artus.proxy.server.handlers;

import de.artus.packets.UnknownPacket;
import de.artus.proxy.server.ClientConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownPacketHandler extends PacketHandler<UnknownPacket> {


    @Override
    public void onPacket(UnknownPacket packet, ClientConnection client) {

        log.trace("Received UnknownPacket!");
        log.trace("Packet ID: 0x{}", Integer.toHexString(packet.getId()));
        log.trace("Packet Data: {}", packet.getData());


    }


}
