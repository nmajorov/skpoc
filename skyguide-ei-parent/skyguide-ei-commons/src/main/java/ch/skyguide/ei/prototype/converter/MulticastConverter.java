package ch.skyguide.ei.prototype.converter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;

public class MulticastConverter 
implements Runnable {

	 
	 public MulticastConverter(final String multicastAddress, 
			 				   final int groupOutputPort,
			 				  final BlockingQueue<DatagramPacket> inputQueue)
	 throws IOException {
		 
		 m_multicastAddress = multicastAddress;
		 m_groupOutputPort = groupOutputPort;
		 m_inputQueue = inputQueue;
	 }
	 
	 public void run() {
		 
			try {
				System.out.println("Starting Multicast Converter thread ...");

				System.out.println("Connecting to multicast address: " 
						+ m_multicastAddress + " on port: " + m_groupOutputPort);

		        final InetAddress address = InetAddress.getByName(m_multicastAddress);
		        final DatagramSocket clientSocket = new DatagramSocket();
			    
			    try {
				    while(true) {
				    
				    	final DatagramPacket packet = m_inputQueue.take();
						System.out.println("Datagram packet extracted from queue "
								+ "and transmitting to host.");
						
						
						System.out.println("Datagram packet converted from 62/65 -> 242/243.");
				        
						// Input stream to the byte array conversion.
						byte[] internalBuffer = new byte[packet.getLength()];
						ByteBuffer buffer 
							= ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
						buffer.get(internalBuffer);

						System.out.println("Retrieved. " + packet.getLength() + " bytes.");

				        // AST CAT 062/065 to FIXM AST.
				        final FixmAsterixMessage fixmAsterixMessage 
				        	= convert(FixmAsterixMessage.class, internalBuffer);
				        
				        final byte[] asterixMessage = convert(byte[].class, fixmAsterixMessage);
						
				        clientSocket.send(new DatagramPacket(asterixMessage, 
				        		asterixMessage.length, address, m_groupOutputPort));

				        System.out.println("Forwored converted message: " + asterixMessage.length + " bytes.");
				    }
			    }
			    catch(InterruptedException exception) {
			    	
					System.out.println("Unicast Producer thread interrupted...");
			    	Thread.currentThread().interrupt();
			    }
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				    
				System.out.println("...Unicast Producer thread shutting down.");
		        clientSocket.close();
				
			} catch (IOException e) {

				e.printStackTrace();
			}     
	 }
	 
	public static void main(String[] args) {

		try {

			final String multicastAddress = "239.193.192.3";
			final int groupInputPort = 25002;
			final int groupOutputPort = 25001;
			
			final BlockingQueue<DatagramPacket> tranferQueue 
				= new LinkedBlockingQueue<DatagramPacket>();
			
			// Start converter first.
			final MulticastConverter converter 
			= new MulticastConverter(multicastAddress, groupOutputPort, tranferQueue);

			new Thread(converter).start();
			
			// Now start the consumer
			final MulticastConsumer consumer 
				= new MulticastConsumer(multicastAddress, groupInputPort, tranferQueue);
			
			new Thread(consumer).start();
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
    protected <T> T convert(final Class<T> type, final Object value) throws Exception {
    	
        return m_context.getTypeConverter().mandatoryConvertTo(type, value);
    }
	 
	 private final BlockingQueue<DatagramPacket> m_inputQueue;
	 private final String m_multicastAddress;
	 private final int m_groupOutputPort;
	 private final CamelContext m_context = new DefaultCamelContext();
}
