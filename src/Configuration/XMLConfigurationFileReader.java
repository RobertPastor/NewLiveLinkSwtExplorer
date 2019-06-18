package Configuration;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import LiveLinkCore.LiveLinkURLObservable;


/**
 * Read an XML configuration file.
 * @since July 2016
 * @author Robert PASTOR
 */
public class XMLConfigurationFileReader {

	final static Logger logger = Logger.getLogger(XMLConfigurationFileReader.class.getName()); 


	private String xmlConfigurationFilePath = "";
	private ArrayList<LiveLinkURLObservable> livelinkUrlConfigList = null;

	public XMLConfigurationFileReader(File file) {
		this.livelinkUrlConfigList = new ArrayList<>();
		if (file.isFile()) {
			try {
				this.xmlConfigurationFilePath = file.getCanonicalPath();
				parseXmlFile();
			} catch (IOException IOex) {
				logger.log(Level.SEVERE, IOex.getLocalizedMessage());
			}

		}
	}

	public XMLConfigurationFileReader(String xmlConfigurationFilePath) {
		this.xmlConfigurationFilePath = xmlConfigurationFilePath;
		this.livelinkUrlConfigList = new ArrayList<>();
		parseXmlFile();

	}

	public void parseXmlFile(){

		logger.info( "parse XML File =  " + this.xmlConfigurationFilePath);

		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder builder  = dbf.newDocumentBuilder();

			File xmlConfigurationFile = new File(this.xmlConfigurationFilePath);

			if (xmlConfigurationFile.exists() && !xmlConfigurationFile.isDirectory()) {
				logger.info( "file with path: " + this.xmlConfigurationFilePath + " is existing ");

				//parse using builder to get DOM representation of the XML file
				Document document = builder.parse(xmlConfigurationFile);

				final Element racine = document.getDocumentElement();
				logger.info( "root = " + racine.getNodeName());

				final NodeList racineNoeuds = racine.getChildNodes();
				logger.info( "======================");

				final int nbRacineNoeuds = racineNoeuds.getLength();
				logger.info( "======================");

				for (int i = 0; i < nbRacineNoeuds; i++) {

					if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {

						final Node configSubNode = racineNoeuds.item(i);

						logger.info( "Element Node name = " + configSubNode.getNodeName());

						if (configSubNode.getNodeName().equalsIgnoreCase("livelinkNode")) {
							logger.info(" =========== it is a Livelink node ============== ");
							logger.info("---------- node type is Element Node= " + Boolean.toString(configSubNode.getNodeType() == Node.ELEMENT_NODE));

							final NodeList LLnodeSubNodes = configSubNode.getChildNodes();
							final int nbSubNodes = LLnodeSubNodes.getLength();
							for (int j = 0 ; j < nbSubNodes ; j++) {

								logger.info( "Element sub Node name = " + LLnodeSubNodes.item(j).getNodeName() );
								final Node configSubSubNode = LLnodeSubNodes.item(j);
								if (configSubSubNode.getNodeName().equalsIgnoreCase("livelinkURL") ) {

									logger.info( "Element sub sub Node ---> is  livelinkURL ");
									logger.info( "Element sub sub Node ---> is livelinkURL " + configSubSubNode.getFirstChild().getNodeValue());
									try {
										URL url = new URL(configSubSubNode.getFirstChild().getNodeValue());
										LiveLinkURLObservable llUrlObservable = new LiveLinkURLObservable( url );
										this.livelinkUrlConfigList.add(llUrlObservable);
									} catch (MalformedURLException ex) {
										ex.printStackTrace();
									}


								}

							}
						}
					}
				}
			}else {
				logger.log(Level.SEVERE, " =========> file is NOT existing = " + this.xmlConfigurationFilePath);
			}

		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch(SAXException se) {
			se.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}