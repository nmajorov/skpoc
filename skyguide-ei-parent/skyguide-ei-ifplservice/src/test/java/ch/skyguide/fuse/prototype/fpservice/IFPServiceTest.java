package ch.skyguide.fuse.prototype.fpservice;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.skyguide.exception.MessageException;
import ch.skyguide.message.util.MessageExtractor;


/***
 * 
 * Test for IFP service Required service to be up running for test
 * 
 * @author nikolaj@majorov.biz
 * 
 */
public class IFPServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(IFPServiceTest.class);

//	@Test
	public void testNettyConsumer() throws MessageException, IOException {
		  String host = "localhost";
		  int port = 12345;

		  String message = "Test Message";
    	  Client client = new Client(host, port, message);
    	  client.run();
    
		  final InputStream is = getClass().getResourceAsStream("/icao_ats_flow.txt");
          assertNotNull(is);
          
          while(is.available() > 0) {
        	  
        	  byte[] messageBytes = receiveMessageBytes(is);
        	  message = Arrays.toString(messageBytes);
        	  client = new Client(host, port, message);
        	  client.run();
          }
	}
	
    /***************************************************************************
     * Reads the bytes from the underlying stream which should form the Message.
     * Extracts all bytes which are between the pattern "(" and ")"
     * (including the patterns).
     *
     * @return   The bytes read from the stream (representing the message).
     *
     * @throws   MessageException
     *           If the end pattern occurs before the start
     *           pattern or if the maximum number of bytes has already been read
     *           without finding the end pattern.
     *
     * @throws   IOException
     *           If there was an exception while reading from the stream
     *           or EOF has been encountered.
     */
    private final byte[]
    receiveMessageBytes(final InputStream inputStream)
            throws MessageException,
            IOException {
        //----------------------------------------------------------------------
        // Read a message from the connection.

        // Extract the message
        return m_messageExtractor.readMessageBytes(inputStream);
    }
    
    /**
     * Message extractor.
     */
    private final MessageExtractor m_messageExtractor 
    	= new MessageExtractor(START_PATTERN,
 						       END_PATTERN,
							   MAX_GARBAGE_LENGTH_BETWEEN_MESSAGES,
							   MAX_MESSAGE_LENGTH,
							   true);

	
	//Client to connect to camel netty consumer
	class Client {
		private String host;
		private int port;
		private Object message;

		public Client(String host, int port, Object message) {
			this.host = host;
			this.port = port;
			this.message = message;
		}

		public void run() {
			// Configure the client.
			ClientBootstrap bootstrap = new ClientBootstrap(
					new NioClientSocketChannelFactory(
							Executors.newCachedThreadPool(),
							Executors.newCachedThreadPool()));

			
			
			// Set up the pipeline factory.
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				public ChannelPipeline getPipeline() throws Exception {
					return Channels
							.pipeline(new ClientHandler(message));
				}
			});

			// Start the connection attempt.
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(
					this.host, this.port));

			// Wait until the connection is closed or the connection attempt
			// fails.
			future.getChannel().getCloseFuture().awaitUninterruptibly();

			// Shut down thread pools to exit.
			bootstrap.releaseExternalResources();
		}

	}

	class ClientHandler extends SimpleChannelUpstreamHandler {

		private final  ChannelBuffer messageChanneled;
		private final AtomicLong transferredBytes = new AtomicLong();

		/**
		 * Creates a client-side handler.
		 */
		public ClientHandler(Object message) {
			
			
			int messageSize = 0;
			
			if (!(message instanceof String)){
				throw new RuntimeException("message type" + message.getClass().getCanonicalName()  + " not supported" );
			
			}
			
			messageSize = ((String)message).getBytes().length;
			
			if (messageSize <= 0) {
				throw new IllegalArgumentException("messageSize: "
						+ messageSize);
			}
			
			messageChanneled = ChannelBuffers.buffer(messageSize);
			
			if (message instanceof String){
				messageChanneled.writeBytes(((String) message).getBytes());
			}
			
			
		
		}

		public long getTransferredBytes() {
			return transferredBytes.get();
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) {
			LOG.info("send  message of size: " + messageChanneled.capacity());
			e.getChannel().write(messageChanneled);
			e.getChannel().close(); //force to close after send
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			// Send back the received message to the remote peer.
			transferredBytes.addAndGet(((ChannelBuffer) e.getMessage())
					.readableBytes());
			LOG.info("receive message of size: " + transferredBytes);
			//e.getChannel().write(e.getMessage());
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			// Close the connection when an exception is raised.
			LOG.error("Unexpected exception from downstream.", e);
			
		}
	}
	
    /**
     * The open bracket indicating the start of an ICAO message.
     */
    private static final byte[] START_PATTERN = "(".getBytes();

    /**
     * The closing bracket indicating the end of an ICAO message.
     */
    private static final byte[] END_PATTERN = ")".getBytes();

    /**
     * The maximum number of bytes to read before the start pattern is detected
     * in the input stream (excluding the length of the start pattern),
     * which is identical to the length of garbage data for a "tolerant 
     * application" to accept between messages.<br>
     * Maximum number of bytes to be extracted from the MessageExtractor before
     * it can be assumed that there is a problem with the input stream.
     */
    private static final int MAX_GARBAGE_LENGTH_BETWEEN_MESSAGES = 2048;

    /**
     * The maximum number of bytes to read after the start pattern before the
     * end pattern is detected in the input stream (excluding the length of the
     * end pattern), which is identical to the maximum possible length of a
     * message without the patterns).<br>
     * The start and end patterns are part of the message.
     */
    private static final int MAX_MESSAGE_LENGTH = 8192;


}
