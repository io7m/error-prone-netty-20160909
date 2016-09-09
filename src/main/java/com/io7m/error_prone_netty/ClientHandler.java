package com.io7m.error_prone_netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClientHandler extends SimpleChannelInboundHandler<String>
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(ClientHandler.class);
  }

  @Override
  protected void channelRead0(
    final ChannelHandlerContext ctx,
    final String msg)
    throws Exception
  {
    ClientHandler.LOG.debug("received: {}", msg);
    final String back = msg.toUpperCase();
    ClientHandler.LOG.debug("sending: {}", back);
    ctx.write(back);
    ctx.flush();
  }
}
