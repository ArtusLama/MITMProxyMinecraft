package de.artus.netty.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@AllArgsConstructor
public class DecryptHandler extends MessageToMessageDecoder<ByteBuf> {

    private final SecretKey sharedSecret;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        log.info("Decrypting message");
        System.out.println("Decrypting message");

        Cipher cipher = Cipher.getInstance("AES/CFB8");
        cipher.init(Cipher.DECRYPT_MODE, getSharedSecret());

        ByteBuf compatible = ctx.alloc().buffer();
        cipher.update(msg.nioBuffer(), compatible.nioBuffer());

        out.add(compatible);
    }

}
