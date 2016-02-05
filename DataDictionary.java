import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class DataDictionary {
	
	//Data center of the server :D
	private Map<String,String> dictionary;
	private Logger logger;
	public DataDictionary(Logger logger){
		dictionary = new HashMap<String,String>();
		this.logger = logger;
	}
	
	/**
     * executes commands and generate the response
     * messages for client
     * 
     * @param packet <code>String</code> command and its parameters.
     * @param sender <code>String</code> address of the client who requested this packet
     * @param time <code>String</code> server local time at which the packet arrived.
     */
	public synchronized String ExecuteCommand(String packet, String sender){
		
		if(!ValidatePacket(packet, sender))
			return "Invalid packet";
		
		String result = "";
		
		//each command's part separated by a blank space
		String[] tokens = packet.split(" ");
		
		//0th token is actual command i.e. put|get|delete
		switch (tokens[0].toLowerCase()) {
		case "put":
			
			result = Put(tokens, sender);
			break;
			
		case "get":
			result = Get(tokens, sender);
			break;
			
		case "delete":
			result = Delete(tokens, sender);
			break;
		
		case "print":
			result = PrintDictionary();
			break;
			
		default:
			result = "Invalid Command <" + tokens[0] + ">";
			break;
		}
		return result;
	}
	
	private String Put(String[] tokens, String sender){
		try {
			String key = tokens[1];
			String value = tokens[2];
			dictionary.put(key, value);
			return "PUT " + key + " " + value + " done successfully";
			
		} catch (Exception e) {
			
			logger.WriteLog("Request From <" + sender + "> : argument missing for PUT");
			return "missing argument for PUT";
		}
	}
	
	private String Get(String[] tokens, String sender){
		try {
			String key = tokens[1];
			return dictionary.get(key);
		} catch (Exception e) {
			logger.WriteLog("Request From <" + sender + "> : argument missing for GET");
			return "missing argument for GET";
		}
	}
	
	private String Delete(String[] tokens, String sender){
		try {
			String key = tokens[1];
			
			if(!dictionary.containsKey(key))
				return "No pair with key=" + key +" to DELETE";
			
			dictionary.remove(key);
			return  "Key=" + key + " deleted successfully";
		} catch (Exception e) {
			logger.WriteLog("Request From <" + sender + "> : argument missing for DELETE");
			return "missing argument for DELETE";
		}
	}
	
	/**
     * validates packets by checking:
     * 1. length of packet
     * 2. format of packet
     * 
     * uses regex objects
     * 
     * @param packet <code>String</code> command and its parameters.
     * @param sender <code>String</code> address of the client who requested this packet
     * @param time <code>String</code> server local time at which the packet arrived.
     */
	private boolean ValidatePacket(String packet, String sender){
		
		boolean isValidPacket = false;
		
		//minLen: "get a" = 5
		if(packet.length() < 5 || packet.length() > 2048){
			logger.WriteLog("Request From <" + sender + ">: packet size is NOT in a correct LENGTH");
			return false;
		}
		
		Pattern pattern = Pattern.compile("^[A-Za-z]{3,6}\\s{0,1}\\w*\\s{0,1}\\w*");
		Matcher matcher = pattern.matcher(packet);
		
		isValidPacket = matcher.find();
		if(!isValidPacket)
			logger.WriteLog("Request From <" + sender + ">: packet size is NOT in a correct FORMAT");
		
		return isValidPacket;
	}
	
	/**
     * Somehow it is like DataDictionary.toString()
     */
	private String PrintDictionary()
	{
		String dictionaryStr = "";
		try {
			for (Map.Entry<String, String> entry : dictionary.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    dictionaryStr += key + ":";
			    dictionaryStr += value + ";";
			}
		} 
		catch (Exception e) {
			logger.WriteLog("DataDictionary->PrintDictionary->exception:" + e.getMessage());
			return "DataDictionary->PrintDictionary->exception:" + e.getMessage();
		}
		
		return dictionaryStr;
	}
}
