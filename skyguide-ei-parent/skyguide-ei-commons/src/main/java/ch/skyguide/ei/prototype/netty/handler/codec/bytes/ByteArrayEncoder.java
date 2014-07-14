package ch.skyguide.ei.prototype.netty.handler.codec.bytes;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes the requested message into a byte array
 * 
 * This decoder is available in the 4.0 version of netty. The JBossFuse 6.1
 * comes with netty 3.8 also without bytearray encoder.
 * 
 * @author Nikolaj Majorov
 * 
 */
@Sharable
public class ByteArrayEncoder extends OneToOneEncoder {

	Logger logger = LoggerFactory.getLogger(ByteArrayEncoder.class);

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {

		logger.debug("message class: " + msg.getClass().getCanonicalName());
		
		if (msg instanceof ChannelBuffer) {
			ChannelBuffer buf = (ChannelBuffer) msg;
			final int length = buf.readableBytes();
			// copy the ByteBuf content to a byte array
			final byte[] array = new byte[length];
			buf.getBytes(0, array);
			// channel.write(ChannelBuffers.wrappedBuffer(array));
			return ctx.getChannel().getConfig().getBufferFactory()
					.getBuffer(array, 0, array.length);
		}

		byte[] array = (byte[]) msg;
		return ctx.getChannel().getConfig().getBufferFactory()
				.getBuffer(array, 0, array.length);

	}
}
