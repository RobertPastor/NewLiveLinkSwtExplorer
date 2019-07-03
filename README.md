# NewLiveLinkSwtExplorer
LiveLink content management explorer

This java software is dedicated to explore opentext livelink
www.opentext.fr 
Content management system.
Exploring means also exporting recursively all nodes from a given one.
The export format is Excel 2003 with its limitations of 65550 rows.

In order to achieve this goal , the eclipse SWT API is used.
This API has a Java Web browser able to extract livelink node information using the XML export feature of a livelink node.

https://www.eclipse.org/swt/

Recursively, nodes that are not leaf ones are explored, and their data extracted using a home made ANTLR parser.
See www.antlr.org

This parser is needed because livelink data is not fully XML compliant.
