package com.velocitypowered.proxy.protocol.netty;

import com.google.common.base.Preconditions;
import com.velocitypowered.natives.encryption.VelocityCipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MinecraftCipherDecoder extends ByteToMessageDecoder {

  private final VelocityCipher cipher;

  public MinecraftCipherDecoder(VelocityCipher cipher) {
    this.cipher = Preconditions.checkNotNull(cipher, "cipher");
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    out.add(cipher.process(ctx, in));
  }

  @Override
  protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
    cipher.dispose();
  }
}
