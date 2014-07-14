package ch.skyguide.ei.prototype.converter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;


public class MulticastIoPump implements Runnable {

	 
	 public MulticastIoPump(final String sourceFilename, 
			 				final String multicastAddress, 
			 				final int groupPort) throws IOException {
		 
		 m_sourceFilename = sourceFilename;
		 m_multicastAddress = multicastAddress;
		 m_groupPort = groupPort;
	 }
	 
	 public void run() {
		 
		try {
			
			System.out.println("Starting Multicast IO pump Thread ...");
	        
			System.out.println("Setup data pump to multicast address: " 
					+ m_multicastAddress +" and port: " + m_groupPort);
			final InetAddress address 
				= InetAddress.getByName(m_multicastAddress);

	        System.out.println("Reading from file: " + m_sourceFilename );
	        final InputStream is 
	        	= getClass().getResourceAsStream("/" + m_sourceFilename);
	        
	        final byte[] buffer = new byte[1024];
	        int bytesRead = 0;
	        
	        final DatagramSocket datagramSocket = new DatagramSocket();
			
	        while((bytesRead = is.read(buffer)) > 0) {
				
				System.out.println("Read bytes: " + bytesRead + 
						" pumping datagram packet to multicast address: " 
						+ m_multicastAddress + " and port: " + m_groupPort );
	        	datagramSocket
	        		.send(new DatagramPacket(buffer, bytesRead, address, m_groupPort));
			}
				
			System.out.println("...Mulitcast IO pump thread shutting down.");
			datagramSocket.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}     
	 }
	 
		public static void main(String[] args) {

			final Properties appProps = new Properties();
			
			try {

				final InputStream in 
					= new FileInputStream("./multicast_io_pump.properties");
				appProps.load(in);
				in.close();
		
				final String sourceFilename 
					= appProps.getProperty("sourceFilename");

				final String multicastAddress 
					= appProps.getProperty("multicastAddress");
				final int groupPort 
					= Integer.valueOf(appProps.getProperty("groupPort"));
				
				// Start the io pump.
				final MulticastIoPump ioPump 
					= new MulticastIoPump(sourceFilename, 
										  multicastAddress, groupPort);
				
				new Thread(ioPump).start();
				
			} 
			catch (IOException e) {

				e.printStackTrace();
			}
		}
	 
	 private final String m_sourceFilename;
	 private final String m_multicastAddress;
	 private final int m_groupPort;
}
