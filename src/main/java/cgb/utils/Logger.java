package cgb.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

	private static Logger instance;
	
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	
	private static final String LOGS = "logs.txt";
	
	private Logger() {
		
	}
	
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public void log(String content) throws IOException{
		try {
			fileWriter = new FileWriter(LOGS, true);
			printWriter = new PrintWriter(fileWriter);
			
			printWriter.println(content);
			printWriter.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
