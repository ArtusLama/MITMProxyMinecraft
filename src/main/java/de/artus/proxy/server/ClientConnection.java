package de.artus.proxy.server;

import de.artus.netty.PacketDecoder;
import de.artus.netty.PacketEncoder;
import de.artus.netty.PacketListener;
import de.artus.netty.encryption.DecryptHandler;
import de.artus.netty.encryption.EncryptHandler;
import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.StringField;
import de.artus.packets.fields.UUIDField;
import de.artus.packets.fields.VarIntField;
import de.artus.packets.fields.XArrayField;
import de.artus.packets.fields.custom.PropertyField;
import de.artus.packets.fields.custom.obj.Property;
import de.artus.packets.s2c.LoginSuccessPacket;
import de.artus.proxy.MinecraftVersion;
import de.artus.util.network.DisconnectPacketHelper;
import de.artus.util.network.EncryptionUtils;
import de.artus.proxy.server.handlers.*;
import de.artus.util.network.PlayerInfo;
import de.artus.util.network.auth.GameProfile;
import io.netty.channel.socket.SocketChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ClientConnection {

    private final ProxyServer proxyServer;
    private final SocketChannel channel;

    @Setter
    private PlayerInfo playerInfo;
    @Setter
    private GameProfile gameProfile;

    @Setter(AccessLevel.PRIVATE)
    private KeyPair serverKeyPair;
    @Setter(AccessLevel.PRIVATE)
    private SecretKey sharedSecret;

    private final PacketDecoder decoder = new PacketDecoder(PacketRegistry.PacketDirection.TO_SERVER, this);
    private final PacketEncoder encoder = new PacketEncoder(PacketRegistry.PacketDirection.TO_CLIENT, this);
    private final PacketListener listener = new PacketListener(this);

    @Setter
    private PacketRegistry.PacketState state = PacketRegistry.PacketState.HANDSHAKE;
    @Setter
    private MinecraftVersion version;

    private final List<PacketHandler<?>> handlers = new ArrayList<>();


    public ClientConnection init() {
        log.trace("Initializing client...");
        initEncryption();
        setupPipeline();
        initHandlers();
        log.trace("Client initialized!");
        return this;
    }
    public void initEncryption() {
        log.trace("Initializing encryption...");
        setServerKeyPair(EncryptionUtils.generateRsaKeyPair(1024));
        log.trace("Encryption initialized!");
    }
    public void setupPipeline() {
        log.trace("Setting up pipeline...");
        getChannel().pipeline().addLast("decoder", getDecoder());
        getChannel().pipeline().addLast("packetListener", getListener());
        getChannel().pipeline().addLast("encoder", getEncoder());
        log.trace("Pipeline set up!");
    }

    public void initHandlers() {
        log.trace("Initializing handlers...");
        getHandlers().add(new UnknownPacketHandler());

        getHandlers().add(new HandshakeHandler());
        getHandlers().add(new StatusRequestHandler());
        getHandlers().add(new PingRequestHandler());
        getHandlers().add(new LoginStartHandler());
        getHandlers().add(new EncryptionResponseHandler());
        log.trace("Handlers initialized!");
    }


    public void enableEncryption() {
        log.trace("Enabling encryption...");
        getChannel().pipeline().addBefore("decoder", "decrypt", new DecryptHandler(getSharedSecret()));
        getChannel().pipeline().addAfter("encoder", "encrypt", new EncryptHandler(getSharedSecret()));

        log.info(getChannel().pipeline().names().toString());
        log.trace("Encryption enabled!");
    }
    public void triggerLoginSuccess() {
        log.trace("Sending login success!");
        UUIDField uuid = new UUIDField(getGameProfile().getUUID());
        StringField username = new StringField(getGameProfile().getName());
        List<GameProfile.Property> properties = getGameProfile().getProperties();
        List<PropertyField> propertyList = properties.stream().map(
                p -> new PropertyField(new Property(p.getName(), p.getValue(), p.getSignature() != "", Optional.of(p.getSignature())))
        ).toList();
        XArrayField<PropertyField> propertiesField = new XArrayField<>(propertyList.toArray(PropertyField[]::new), propertyList.size());

        sendPacket(new LoginSuccessPacket(uuid, username, new VarIntField(propertiesField.getLength()), propertiesField));
    }


    public void onPacketReceived(Packet packet) {
        log.info("Received Packet: " + packet.getClass().getSimpleName());
        for (PacketHandler<? extends Packet> handler : handlers) {
            if (handler.getPacketClass().isInstance(packet)) {
                handler.handlePacket(packet, this);
            }
        }
    }

    public void sendPacket(Packet packet) {
        log.info("Sending packet: " + packet.getClass().getSimpleName());
        getChannel().writeAndFlush(packet);
    }


    public void disconnect() {
        log.trace("Closing client connection!");
        getChannel().close();
    }
    public void disconnect(String reason) {
        sendPacket(DisconnectPacketHelper.sendReason(reason));
        getChannel().close();
    }


}
