package de.artus.util.network;

import de.artus.proxy.ServerAddress;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.net.InetSocketAddress;

@Slf4j
public class SRVLookup {

    private static final String SRV_QUERY_PREFIX = "_minecraft._tcp.%s";

    public static ServerAddress lookup(ServerAddress target) {
        log.info("Looking up SRV Record for {}...", target);
        String hostname = target.getHost();
        int port = target.getPort();

        try {
            Lookup lookup = new Lookup(String.format(SRV_QUERY_PREFIX, hostname), Type.SRV);
            Record[] records = lookup.run();
            if (records == null || records.length == 0) {
                log.info("No SRV Records found for {}! Using {}:{}", hostname, hostname, port);
                return new ServerAddress(hostname, port);
            }

            SRVRecord srvRecord = (SRVRecord) records[0];
            log.info("Found SRV Record! Using {}:{}", srvRecord.getTarget(), srvRecord.getPort());
            return new ServerAddress(srvRecord.getTarget().toString(), srvRecord.getPort());
        } catch (TextParseException e) {
            log.error("Error while parsing SRV Record!", e);
            return new ServerAddress(hostname, port);
        }
    }


}
