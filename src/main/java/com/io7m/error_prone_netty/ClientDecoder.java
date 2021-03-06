package com.io7m.error_prone_netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ClientDecoder extends ByteToMessageDecoder
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(ClientDecoder.class);
  }

  @Override
  protected void decode(
    final ChannelHandlerContext ctx,
    final ByteBuf in,
    final List<Object> out)
    throws Exception
  {
    final byte[] data = new byte[in.readableBytes()];
    in.readBytes(data);

    ClientDecoder.LOG.debug(
      "decoding: {} bytes of {}", Integer.valueOf(data.length), in);
    final String text = new String(data, StandardCharsets.UTF_8);
    ClientDecoder.LOG.debug(
      "decoded: {} ({})", text, DatatypeConverter.printHexBinary(data));
    out.add(text);
  }
}
