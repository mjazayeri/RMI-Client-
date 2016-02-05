import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UDPServer {
	
	private Logger logger;
	
	/**
     * initializes a UDP server by a port number
     *
     * @param port <code>int</code> value of a port to listen to.
     */
	public void StartToListen(int port){
		
		try {
			logger = new Logger("ServerLog.text");
			DataDictionary dictionary = new DataDictionary(logger);
			DatagramSocket serverUdpSocket = new DatagramSocket(port);

			logger.WriteLog("Server Started successfully.");

			//Core of UDP Server
			while(true){

				//Accept requests. Practically getting more than 1k increases the chance of missing data.
				byte[] buffer = new byte[1024];

				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				serverUdpSocket.receive(requestPacket);

				ExecutorService executer = Executors.newFixedThreadPool(10);
				UDPCommunication communication = new UDPCommunication(serverUdpSocket, requestPacket, dictionary, logger);

				//perform client's request and response accordingly
				executer.execute(communication);
				
			}			
		} 
		catch (Exception e) {
			logger.WriteLog("UDPServer->StartToListen->exception: "+ e.getMessage());
		}
	}
}
