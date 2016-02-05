import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class Logger {
	private BufferedWriter writer;
	private String defaultPath;
	
	
	public Logger(String filePath){
		
		defaultPath = "log.txt";
		if(! OpenWriter(filePath))
			OpenWriter(defaultPath);
	}
	
	public synchronized void WriteLog(String log){
		
		try {
			String dateTimeTag = LocalDateTime.now().toString();
			String line = dateTimeTag + " " + log;
			writer.write(line);
			writer.newLine();
			writer.flush();
			System.out.println(line);
			
		} catch (Exception e) {
			System.out.println("Logger-Exception-ex: " + e.getMessage());
		}
	}
	
	private boolean OpenWriter(String filePath){
		try{
			writer = new BufferedWriter(new FileWriter(filePath, true));
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	public boolean CloseWriter(){
		try {
			writer.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
