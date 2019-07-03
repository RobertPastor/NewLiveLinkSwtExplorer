# NewLiveLinkSwtExplorer
Purpose :
LiveLink opentext content management explorer.

This piece of java software is dedicated to explore opentext livelink
www.opentext.fr 
Content management systems.
This piece of java software is only working on windows platform (tested for Windows seven)

Exploring means displaying from a configured node , a tree with all sub nodes.
Hence, for a livelink node, the software shows a tree, a table view and a rough XML result of the node XML retrieved data.

It is relying on the livelink feature returning an XML answer when livelink is queried with the action XML export.
The SW allows to export recursively all sub nodes of a given one.

The export data is retrieved in an Excel 2003 file with its limitations of 65550 rows.
One Excel row containing the data of one node.

In order to achieve this goal , the eclipse SWT API is used.
This API has a Java Web browser (relying on the windows internet explorer if the windows platform) able to extract livelink node information using the XML export feature of a livelink node.

https://www.eclipse.org/swt/

Recursively, nodes that are explored, and their data extracted using a specific tailored ANTLR parser.
See www.antlr.org

This parser is needed because livelink retrieved data is not fully XML compliant.

The SW has also a windows file and folder extractor.

Finally there are online help tabs for both main features.
