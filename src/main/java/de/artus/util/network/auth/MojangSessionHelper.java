package de.artus.util.network.auth;

import de.artus.proxy.server.ClientConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;


@Slf4j
public class MojangSessionHelper {

    @Getter
    private static final String HAS_JOINED_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s";


    public static void authPlayer(ClientConnection conn, String username, String serverId, Consumer<GameProfile> callback) {
        log.trace("Authenticating player {} with serverId {}", username, serverId);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .setHeader("User-Agent",
                        conn.getVersion().getVersions()[0] + "/ArtusProxy")
                .uri(URI.create(String.format(HAS_JOINED_URL, username, serverId)))
                .build();

        HttpClient client = HttpClient.newBuilder().executor(conn.getProxyServer().getWorkerGroup()).build();
        client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenCompleteAsync((response, throwable) -> {
                    if (throwable != null) {
                        log.error("Failed to authenticate player!", throwable);
                        conn.disconnect("Failed to authenticate player!");
                        return;
                    }

                    if (response.statusCode() != 200) {
                        log.error("Failed to authenticate player! Status code: {}", response.statusCode());
                        conn.disconnect("Failed to authenticate player!");
                        return;
                    }

                    GameProfile gameProfile = GameProfile.fromJson(response.body());
                    // Here would be some checks for 1.19.1, but I'm too lazy to implement them :D

                    log.info("Successfully authenticated player {}!", gameProfile.getName());
                    callback.accept(gameProfile);


                }, conn.getChannel().eventLoop());


    }

}
