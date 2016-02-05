/*
 * Title: Single Client-Server : Server Side
 * Project: #1 Applied Distributed Computing 
 * Autumn 2015
 * Developer: Seyed Mehdi Jazayeri
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerMain {

	static BufferedReader bufReader;

	public static void main(String[] args) {
		System.out.println("****Attention****");
		System.out.println("****This application doesn't work with command line parameter****");
		System.out.println("****Try to get required parameters to the application when it is asked****");
		
		bufReader = new BufferedReader(new InputStreamReader(System.in));
		
		//Get the port to which server will listen
		int port = GetPort();

		try {
			boolean isProtocolValid = false;
			
			//get the protocol that server will use
			while(!isProtocolValid){
				
				System.out.println("Choose The Server Protocol you want for server (TCP or UDP)");

				String protocol = bufReader.readLine().toUpperCase();
				
				if(protocol.equals("TCP")){
					StartTCPServer(port);
					isProtocolValid = true;
				}
				else if(protocol.equals("UDP")){
					StartUDPServer(port);
					isProtocolValid = true;
				}
				else
					System.out.println("Invalid Protocol name");
			}
		} 
		catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}

	}

	/*This method creates a UDP socket and its related packets*/
	private static void StartUDPServer(int port){
		UDPServer server = new UDPServer();
		server.StartToListen(port);
	}

	/*This method creates a TCP Socket and its related stream*/
	private static void StartTCPServer(int port){
		TCPServer server = new TCPServer();
		server.StartToListen(port);
	}

	/*this method gets a valid port number*/
	private static int GetPort(){
		int port = 0;
		
		//port is important. then try until a valid port number is entered
		while(true){
			try {
				System.out.println("Enter the port you want to listen to:");

				String portStr = bufReader.readLine();
				port = Integer.parseInt(portStr);
				break;
			} 
			catch (Exception e) {
				System.out.println("Error: "+e.getMessage());
			}
		}
		return port;
	}
}
