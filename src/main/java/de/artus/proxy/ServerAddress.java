package de.artus.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServerAddress {

    private String host;
    private int port;

    @Override
    public String toString() {
        return getHost() + ":" + getPort();
    }
}
