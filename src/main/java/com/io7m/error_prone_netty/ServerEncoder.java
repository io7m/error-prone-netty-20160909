package com.io7m.error_prone_netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public final class ServerEncoder extends MessageToByteEncoder<String>
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(ServerEncoder.class);
  }

  @Override
  protected void encode(
    final ChannelHandlerContext ctx,
    final String msg,
    final ByteBuf out)
    throws Exception
  {
    final byte[] data = msg.getBytes(StandardCharsets.UTF_8);
    ServerEncoder.LOG.debug(
      "encoding: {} ({} bytes)", msg, Integer.valueOf(data.length));
    out.writeBytes(data);
  }
}
