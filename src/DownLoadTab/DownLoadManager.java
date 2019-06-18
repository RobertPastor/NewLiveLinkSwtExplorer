package DownLoadTab;

/**
 * 
 * The ability to implement a custom download manager was introduced in Microsoft Internet Explorer 5.5. 
 * This feature enables you to extend the functionality of Windows Internet Explorer 
 * and WebBrowser applications by implementing a Component Object Model (COM) object to handle the file download process.
 * 
 * By implementing a custom download manager, your WebBrowser application can be extended 
 * to display a custom user interface. You could, for example, create a download manager 
 * that enables you to view MPEG files or launch applications.
 * 
 * A download manager is implemented as a COM object that exposes the IUnknown and IDownloadManager interface. 
 * IDownloadManager has only one method, IDownloadManager::Download, 
 * which is called by Internet Explorer or a WebBrowser application to download a file. 
 * When a file is selected for download in a WebBrowser application, the custom download manager is accessed 
 * in one of two ways.
 * 
 *    1.
 *    If the IServiceProvider::QueryService method of the IServiceProvider interface is implemented, 
 *    the WebBrowser application first calls IServiceProvider::QueryService 
 *    to retrieve an IDownloadManager interface pointer. 
 *    The following example shows a possible implementation of the IServiceProvider::QueryService method.
 *    
 * @author t0007330
 * @since August 2012
 */
public class DownLoadManager {

	
}
