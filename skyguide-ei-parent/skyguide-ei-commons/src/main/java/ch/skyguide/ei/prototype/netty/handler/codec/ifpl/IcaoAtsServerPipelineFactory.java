package ch.skyguide.ei.prototype.netty.handler.codec.ifpl;

import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.ServerPipelineFactory;
import org.apache.camel.component.netty.handlers.ServerChannelHandler;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class IcaoAtsServerPipelineFactory extends ServerPipelineFactory {

    private int maxLineSize = 8192;
    private NettyConsumer consumer;

    public IcaoAtsServerPipelineFactory() {
    
    	// Nothing to do.
    }
    
	public IcaoAtsServerPipelineFactory(NettyConsumer consumer) {

		this.consumer = consumer;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {

        ChannelPipeline channelPipeline = Channels.pipeline();

        channelPipeline.addLast("encoder-SD", new StringEncoder(CharsetUtil.UTF_8));
        channelPipeline.addLast("decoder-DELIM", new DelimiterBasedFrameDecoder(maxLineSize, true, IcaoAtsDelimiters.endMessageDelimiter()));
        channelPipeline.addLast("decoder-SD", new IcaoAtsDecoder());
        channelPipeline.addLast("handler", new ServerChannelHandler(consumer));

        return channelPipeline;
	}

	@Override
	public ServerPipelineFactory createPipelineFactory(NettyConsumer consumer) {
		
		return new IcaoAtsServerPipelineFactory(consumer);
	}
	
	/**
	 * A set of commonly used delimiters for {@link IcaoAtsDecoder}.
	 */
	public final static class IcaoAtsDelimiters {

	    /**
	     * Returns a {@code SOH (0x01)} delimiter, which is used for the start
	     * of the header in the ICAO ATS protocol.
	     */
	    public static ChannelBuffer[] startHeaderDelimiter() {
	        return new ChannelBuffer[] {
	        		
	                ChannelBuffers.wrappedBuffer(new byte[] { 0x01 }) };
	    }

	    /**
	     * Returns {@code STX (0x02)}, which is used for the start of the
	     * message in the ICAO ATS protocols.
	     */
	    public static ChannelBuffer[] startMessageDelimiter() {
	        return new ChannelBuffer[] {
	        		
	                ChannelBuffers.wrappedBuffer(new byte[] { 0x02 })};
	    }

	    /**
	     * Returns {@code VT (0x0B)} and EHX (0x03), which is used for the start of the
	     * message in the ICAO ATS protocols.
	     */
	    public static ChannelBuffer[] endMessageDelimiter() {
	        return new ChannelBuffer[] {
	        		
	                ChannelBuffers.wrappedBuffer(new byte[] { 0x0B, 0x03 })};
	    }

	    private IcaoAtsDelimiters() {
	        // Unused
	    }
	}
}
