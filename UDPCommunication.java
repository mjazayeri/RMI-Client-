import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPCommunication implements Runnable{
	
	private Logger logger;
	private DataDictionary dictionary;
	private DatagramSocket serverUdpSocket;
	private DatagramPacket requestPacket;
	
	public UDPCommunication(DatagramSocket udpSocket, DatagramPacket requestPacket, DataDictionary dictionary, Logger logger) {
		
		serverUdpSocket = udpSocket;
		this.requestPacket = requestPacket;
		this.logger = logger;
		this.dictionary = dictionary;
		
	}
	
	@Override
	public void run() {
		ExecuteAndSendResponse();
	}

	private boolean ExecuteAndSendResponse()
	{
		try {
			
			//truncate everything in the packet after \n
			String requestStr = GetExactPacket(new String(requestPacket.getData()));
			
			//calculate client's address and local time of arrival
			//String time = LocalDateTime.now().toString();
			String sender = requestPacket.getAddress().toString();
			
			//Print(requestPacket);
			logger.WriteLog("Request From <"+sender+"> : "+ requestStr);
			
			//execute client's request
			String responseStr = dictionary.ExecuteCommand(requestStr, sender);
			
			//writeBytes method and readLine wont work unless there is a \n at the end of the string
			responseStr+= "\n";
			
			byte[] responseData = responseStr.getBytes();
			DatagramPacket responsePacket = new DatagramPacket(responseData,responseData.length, requestPacket.getAddress(), requestPacket.getPort());
			serverUdpSocket.send(responsePacket);
			
			//Print("response: "+ responseStr);
			logger.WriteLog("Sever Response To <"+sender+"> : " + responseStr);

		} 
		catch (Exception e) {
			//Print("UDPerver->SendResponse->exception: "+ e.getMessage());
			logger.WriteLog("UDPerver->SendResponse->exception: "+ e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private String GetExactPacket(String plainCommand){
		int endOfCommand = plainCommand.indexOf('\n');
		return plainCommand.substring(0, endOfCommand);
	}
}
