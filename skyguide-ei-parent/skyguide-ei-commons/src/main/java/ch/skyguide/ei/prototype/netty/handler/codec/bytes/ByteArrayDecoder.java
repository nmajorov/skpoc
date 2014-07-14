package ch.skyguide.ei.prototype.netty.handler.codec.bytes;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelHandler.Sharable;

import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * Decodes a received org.jboss.netty.buffer.ChannelBuffer into a byte array.
 * This decoder is available in the 4.0 version of netty.<br/>
 * The JBossFuse 6.1 comes with netty 3.8 also without bytearray encoder.
 * 
 * @author Nikolaj Majorov
 * 
 */
@Sharable
public class ByteArrayDecoder extends OneToOneDecoder {
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {

		if (!(msg instanceof ChannelBuffer)) {
			return msg;
		}

		ChannelBuffer buf = (ChannelBuffer) msg;
		final int length = buf.readableBytes();
		// copy the ByteBuf content to a byte array
		final byte[] array = new byte[length];
		buf.getBytes(0, array);

		return array;

	}
}
