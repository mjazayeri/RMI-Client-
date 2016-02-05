import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TCPServer {
	
	/************Class Members***********/
	
	private ServerSocket serverSocket;
	private Logger logger;

	/**********Class Methods**********/
	
	/**
     * initializes a TCP server by a port number
     *
     * @param port <code>int</code> value of a port to listen to.
     */
	public void StartToListen(int port){
		try {
			logger = new Logger("ServerLog.text");
			DataDictionary dictionary= new DataDictionary(logger);
			serverSocket = new ServerSocket(port);
			
			logger.WriteLog("Server Started successfully.");
			
			//As creating new thread cause overhead
			//I decided to use a fixedThreadPool of size 10

			ExecutorService executer = Executors.newFixedThreadPool(10);
			
			//Core of TCP Server
			while(true){
				Socket client = serverSocket.accept();
				TCPCommunication communication = new TCPCommunication(client, logger, dictionary);
				executer.execute(communication);
			}
		} 
		catch (Exception e) {
			logger.WriteLog("server->sratrtolisten->exception:"+ e.getMessage());
		}
	}
	
}
