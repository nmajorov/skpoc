package ch.skyguide.ei.prototype.converter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastConsumer implements Runnable {

	 
	 public MulticastConsumer(final String multicastGroup, 
			 final int groupInputPort, 
			 final BlockingQueue<DatagramPacket> inputQueue) throws IOException {
		 
		 if(inputQueue == null) {
			 
			 throw new IllegalArgumentException("Input Queue not initialized.");
		 }
		 
		 m_inputQueue = inputQueue;

		 if(multicastGroup == null) {
			 
			 throw new IllegalArgumentException("Mulitcast Group not initialized.");
		 }
		 
		 m_multicastGroup = multicastGroup;
		 m_groupInputPort = groupInputPort;
		 
	 }
	 
	 public void terminate() {
		 
		 m_terminate.set(true); 
	 }
	 
	 public void run() {
		 
			try {
				System.out.println("Starting multicast consumer thread ...");

				final MulticastSocket socket 
					= new MulticastSocket(m_groupInputPort);
				final InetAddress address 
					= InetAddress.getByName(m_multicastGroup);
			
				socket.joinGroup(address);
				
				System.out.println("Joining multicast address: " 
						+ m_multicastGroup + " on port: " + m_groupInputPort);

				try {
					while (true) {           
						
						byte[] buffer = new byte[1024];
						final DatagramPacket packet 
							= new DatagramPacket(buffer, buffer.length);             
						socket.receive(packet);

						System.out.println("Datagram packet received pushing to queue.");
						m_inputQueue.put(packet);
					}    
				}
				catch(InterruptedException exception) {
					
					System.out.println("Mulitcast Consumer thread interrupted...");
					Thread.currentThread().interrupt();
				}
				
				System.out.println("...Mulitcast Consumer thread shutting down.");
				socket.leaveGroup(address);
				socket.close();
				
			} 
			catch (IOException e) {

				e.printStackTrace();
			}     
	 }
	 
	 private final AtomicBoolean m_terminate = new AtomicBoolean(false);
	 private final BlockingQueue<DatagramPacket> m_inputQueue;
	 private final String m_multicastGroup;
	 private final int m_groupInputPort;
}
