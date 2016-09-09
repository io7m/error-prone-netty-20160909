package com.io7m.error_prone_netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServerHandler extends SimpleChannelInboundHandler<String>
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(ServerHandler.class);
  }

  @Override
  public void channelRegistered(
    final ChannelHandlerContext ctx)
    throws Exception
  {
    ServerHandler.LOG.debug("client connected, sending hello");

    ctx.write("Server hello");
    ctx.flush();
  }

  @Override
  protected void channelRead0(
    final ChannelHandlerContext ctx,
    final String msg)
    throws Exception
  {
    ServerHandler.LOG.debug("received: {}", msg);
  }
}
