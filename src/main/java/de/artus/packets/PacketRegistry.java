package de.artus.packets;

import de.artus.packets.c2s.*;
import de.artus.packets.s2c.*;
import de.artus.proxy.MinecraftVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PacketRegistry {

    // - - - - - - C L I E N T - - - - - -

    HANDSHAKE(PacketDirection.TO_SERVER, 0x00, PacketState.HANDSHAKE, HandshakePacket.class),

    STATUS_REQUEST(PacketDirection.TO_SERVER, 0x00, PacketState.STATUS, StatusRequestPacket.class),
    PING_REQUEST(PacketDirection.TO_SERVER, 0x01, PacketState.STATUS, PingRequestPacket.class),

    LOGIN_START(PacketDirection.TO_SERVER, 0x00, PacketState.LOGIN, LoginStartPacket.class),
    ENCRYPTION_RESPONSE(PacketDirection.TO_SERVER, 0x01, PacketState.LOGIN, EncryptionResponsePacket.class),



    // - - - - - - S E R V E R - - - - - -

    STATUS_RESPONSE(PacketDirection.TO_CLIENT, 0x00, PacketState.STATUS, StatusResponsePacket.class),
    PING_RESPONSE(PacketDirection.TO_CLIENT, 0x01, PacketState.STATUS, PingResponsePacket.class),

    DISCONNECT(PacketDirection.TO_CLIENT, 0x00, PacketState.LOGIN, DisconnectPacket.class),
    ENCRYPTION_REQUEST(PacketDirection.TO_CLIENT, 0x01, PacketState.LOGIN, EncryptionRequestPacket.class),
    LOGIN_SUCCESS(PacketDirection.TO_CLIENT, 0x02, PacketState.LOGIN, LoginSuccessPacket.class),
    ;

    private final PacketDirection direction;
    private final int id;
    private final PacketState state;
    private final Class<? extends Packet> packetClass;



    public static int getIdOfPacket(Packet packet) {
        if (packet instanceof UnknownPacket) {
            return ((UnknownPacket) packet).getId();
        }
        for (PacketRegistry packetRegistry : values()) {
            if (packetRegistry.getPacketClass().equals(packet.getClass())) {
                return packetRegistry.getId();
            }
        }
        return -1;
    }


    public static Packet createPacket(int packetId, int packetLength, PacketDirection direction, PacketState state) {
        for (PacketRegistry packetRegistry : values()) {
            if (packetRegistry.getId() == packetId && packetRegistry.getDirection() == direction && packetRegistry.getState() == state) {
                try {
                    return packetRegistry.getPacketClass().getConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new UnknownPacket().setId(packetId).setLength(packetLength).setDirection(direction);
    }


    public enum PacketDirection {
        TO_SERVER,
        TO_CLIENT
    }
    public enum PacketState {
        HANDSHAKE,
        STATUS,
        LOGIN,
        PLAY
    }
}
