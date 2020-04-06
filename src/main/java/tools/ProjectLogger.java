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
public class ProjectLogger {

	private static Logger logger;
	private static int storageLength = 10;

	public ProjectLogger() {
		try {
			logger = Logger.getLogger("IED_Configurator");

			String config = "handlers= java.util.logging.ConsoleHandler\r\n" +
							".level= INFO\r\n" + 
							"java.util.logging.ConsoleHandler.level = INFO\r\n" + 
							"java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter\r\n" + 
							"java.util.logging.SimpleFormatter.format=[%1$tF %1$tT] %5$s %n";
			LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(config.getBytes()));
			File directory = new File("logs\\"); if (! directory.exists()) directory.mkdir();
			deleteGarbage(directory);
			FileHandler handler = new FileHandler("logs\\"+new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(new Date(System.currentTimeMillis()))+".log");
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
			logger.setLevel(Level.ALL);
		} catch (SecurityException | IOException e) { e.printStackTrace(); }
	}

	private static void deleteGarbage(File directory){
		for (File file:directory.listFiles()){ if(file.getName().contains(".log.lck")) file.delete(); }
		for (File file:directory.listFiles()){ if(directory.listFiles().length > storageLength) file.delete(); else break; }
	}

	public static void enable() { if(logger==null) new Thread(()->new ProjectLogger()).start(); }

	public static void fine(String msg) { if(logger!=null) logger.fine(msg); }
	public static void warning(String msg) { if(logger!=null) logger.warning(msg); }
}
