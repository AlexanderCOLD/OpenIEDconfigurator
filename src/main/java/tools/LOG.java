package tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.*;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Класс для записи логов
 */
public class LOG {

	private static LOG self;
	private Logger logger = Logger.getLogger("POKER_FACE"); 
	
	public LOG() {
		try {
			String config = "handlers= java.util.logging.ConsoleHandler\r\n" + 
							".level= INFO\r\n" + 
							"java.util.logging.ConsoleHandler.level = INFO\r\n" + 
							"java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter\r\n" + 
							"java.util.logging.SimpleFormatter.format=[%1$tF %1$tT] %5$s %n";
			LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(config.getBytes()));
			File directory = new File("logs\\"); if (! directory.exists()) directory.mkdir();
			FileHandler handler = new FileHandler("logs\\"+new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(new Date(System.currentTimeMillis()))+".log");
			handler.setFormatter(new SimpleFormatter()); 
			logger.addHandler(handler); 
			logger.setLevel(Level.FINE); 
		} catch (SecurityException | IOException e) { e.printStackTrace(); } 
		self = this;
	}
	
	
	public static void print(String msg) {
		if(self==null) new LOG();
		System.out.println(msg);
		self.logger.fine(msg);
//		LogPanel.writeMessage(msg);
	}
	
}
