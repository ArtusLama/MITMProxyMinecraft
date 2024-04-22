package de.artus.proxy.server.handlers;

import de.artus.packets.Packet;
import de.artus.proxy.server.ClientConnection;

@SuppressWarnings("unchecked")
public abstract class PacketHandler<T extends Packet> {

    public abstract void onPacket(T packet, ClientConnection client);

    public void handlePacket(Packet packet, ClientConnection client) {
        if (getPacketClass().isInstance(packet)) {
            onPacket((T) packet, client);
        }
    }

    public Class<T> getPacketClass() {
        return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
