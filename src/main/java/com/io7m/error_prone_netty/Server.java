package com.io7m.error_prone_netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;

public final class Server
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(Client.class);
  }

  private Server()
  {
  }

  public static void main(
    final String[] args)
    throws Exception
  {
    Server.LOG.debug("creating server");

    Server.LOG.trace("creating boss group");
    final NioEventLoopGroup boss_group = new NioEventLoopGroup(1);
    Server.LOG.trace("creating worker group");
    final NioEventLoopGroup worker_group = new NioEventLoopGroup();

    Server.LOG.trace("creating bootstrap");
    final ServerBootstrap b = new ServerBootstrap();
    b.group(boss_group, worker_group)
      .channel(NioServerSocketChannel.class)
      .handler(new LoggingHandler(LogLevel.DEBUG))
      .childHandler(new ChannelInitializer<SocketChannel>()
      {
        @Override
        public void initChannel(final SocketChannel ch)
          throws Exception
        {
          final ChannelPipeline p = ch.pipeline();

          p.addLast(
            "in-length",
            new LengthFieldBasedFrameDecoder(
              ByteOrder.BIG_ENDIAN, 1000000, 0, 4, 0, 4, true));
          p.addLast(
            "in-decode",
            new ServerDecoder());

          p.addLast(
            "out-length",
            new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 4, 0, false));
          p.addLast(
            "out-encode",
            new ServerEncoder());

          p.addLast(
            "handler",
            new ServerHandler());

          p.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        }
      });

    final SocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
    Server.LOG.trace("bind {}", address);
    final ChannelFuture chan = b.bind(address).sync();
    Server.LOG.debug("server bound");
  }
}
