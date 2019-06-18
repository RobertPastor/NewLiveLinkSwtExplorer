package RecursiveLiveLinkBrowser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class TestXMLpurifier {
	
private static String filterErroneousCharacters(String strXML) {
		
		String[] keyWords = {"created=" , "createdby=", "createdbyname=" , "description=" , "id=", "modified=", "name=", "objname=" , "parentid=" , "ownedbyname=" , "size="};
		Map<Integer, String> keyWordsMap = new TreeMap<Integer, String>();
		
		for (String strKeyWord : keyWords) {
			int position = strXML.indexOf(strKeyWord);
			if (position > 0) {
				keyWordsMap.put(position, strKeyWord);
			}
		}
		Boolean descriptionFound = false;
		Boolean nextFound = false;
		int beginOfDescription = 0;
		int beginOfNext = 0;
		for (Integer position : keyWordsMap.keySet()) {
			if ((descriptionFound == true) && (nextFound == false)) {
				beginOfNext = position;
				nextFound = true;
			}
			System.out.println("position = " + (position) + " --- map string = " + keyWordsMap.get(position));
			if (keyWordsMap.get(position).equalsIgnoreCase("description=")) {
				descriptionFound = true;
				beginOfDescription = position;
			}
		}
		if (descriptionFound && nextFound){
			String correctedStrXML = strXML.substring(0, beginOfDescription);
			correctedStrXML += "description=" + "\"";
			correctedStrXML += strXML.substring(beginOfDescription + "description=".length(), beginOfNext).replaceAll("\""," - ");
			correctedStrXML += "\"  ";
			correctedStrXML += strXML.substring(beginOfNext, strXML.length());
			//System.out.println(correctedStrXML);
			return correctedStrXML;
		}
		
		return strXML;
	}

	private static void breakLLNodes(String strXML) {
		
		String finalPurifiedXMLString = "";
		String[] parts = strXML.split("<llnode");
		Boolean firstPass = false;
		for (String part : parts) {
			if (firstPass == true){
				finalPurifiedXMLString += " <llnode ";
			} else {
				firstPass = true;
			}
			//System.out.println(part);
			part = filterErroneousCharacters(part);
			finalPurifiedXMLString += filterErroneousCharacters(part);
		}
		System.out.println(finalPurifiedXMLString);
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner s = null;
		String strXML = "";
        try {
            s = new Scanner(new BufferedReader(new FileReader("D:/Users/T0007330/Documents/Mes Outils Personnels/workspace/NewLiveLinkSwtExplorer/src/RecursiveLiveLinkBrowser/LLNode.txt")));

            while (s.hasNext()) {
            	strXML += " " + s.next().replaceAll("'", "\"");
                //System.out.println(strXML);
            }
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (s != null) {
                s.close();
            }
        }
        s.close();
        System.out.println(strXML);
        System.out.println("--------------");
        breakLLNodes(strXML);
	}

}
