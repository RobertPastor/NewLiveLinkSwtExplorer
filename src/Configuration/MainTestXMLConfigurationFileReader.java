package Configuration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainTestXMLConfigurationFileReader {

	public static void main(String[] args) {

		final Logger logger = Logger.getLogger(MainTestXMLConfigurationFileReader.class.getName());

		String fileName = "LiveLink-Configuration-V001.xml";

		try {

			String filePath = MainTestXMLConfigurationFileReader.class.getResource(fileName).getPath();
			logger.info(filePath);
			File xmlConfigurationFile = new File(filePath);

			if (xmlConfigurationFile.exists() && !xmlConfigurationFile.isDirectory()) {

				logger.info("file is existing");
			}

			XMLConfigurationFileReader xmLConfigurationFileReader = new XMLConfigurationFileReader(filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE, "file= " + fileName + " -> error= " + ex.getLocalizedMessage());

		}


	}

}
