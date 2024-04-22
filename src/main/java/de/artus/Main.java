package de.artus;

import de.artus.proxy.Proxy;
import de.artus.proxy.ServerAddress;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    @Getter
    private static int test = 10;

    public static void main(String[] args) {

        new Proxy(25565, new ServerAddress("localhost", 25566), "ArtusDev").start();


    }

}