package com.io7m.error_prone_netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;

public final class Client
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(Client.class);
  }

  private Client()
  {

  }

  public static void main(
    final String[] args)
    throws Exception
  {
    final EventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap b = new Bootstrap();
    b.group(group)
      .channel(NioSocketChannel.class)
      .handler(new ChannelInitializer<SocketChannel>()
      {
        @Override
        public void initChannel(final SocketChannel ch)
          throws Exception
        {
          final ChannelPipeline p = ch.pipeline();

          p.addLast(
            "out-length",
            new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 4, 0, false));
          p.addLast(
            "out-encode",
            new ClientEncoder());

          p.addLast(
            "in-length",
            new LengthFieldBasedFrameDecoder(
              ByteOrder.BIG_ENDIAN, 1000000, 0, 4, 0, 4, true));
          p.addLast(
            "in-decode",
            new ClientDecoder());
          p.addLast(
            "handler",
            new ClientHandler());

          p.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        }
      });

    final SocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
    Client.LOG.trace("connect {}", address);
    final ChannelFuture chan = b.connect(address).sync();
    Client.LOG.debug("client connected");
  }
}
