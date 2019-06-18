package DownLoadTab;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import JExcelApi.ReadableWorkbook;
import LiveLinkCore.ShellErrorMessage;

public class FileDropTargetListener implements DropTargetListener {

	private static final Logger logger = Logger.getLogger(FileDropTargetListener.class.getName()); 
	private Composite parent = null;
	private Text locationText = null;
	private List listBox = null;

	public FileDropTargetListener(Composite _parent,Text _locationText) {
		this.parent = _parent;
		this.locationText = _locationText;
		this.listBox = null;
	}

	public void drop(DropTargetEvent event) {

		//System.out.println("Dropped Over");
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else if ((event.operations & DND.DROP_LINK) != 0) {
				event.detail = DND.DROP_LINK;
			} else if ((event.operations & DND.DROP_MOVE) != 0) {
				event.detail = DND.DROP_MOVE;
			}
		}

		logger.log(Level.INFO,event.toString());
		FileTransfer ft = FileTransfer.getInstance();
		logger.log(Level.INFO, event.currentDataType.getClass().getCanonicalName());
		if (ft.isSupportedType(event.currentDataType)) {

			if (event.data instanceof String[]) {
				String strFileName[] = (String[])event.data;
				if ( strFileName.length > 0) {
					logger.log(Level.INFO,strFileName[0]);
					// set the content of the location text 
					FileDropTargetListener.this.locationText.setText(strFileName[0]);
					// get the dropped file
					File file = new File(strFileName[0]);
					if (file.exists() == true) {
						logger.log(Level.INFO,getSystemTypeDescription(file));
						logger.log(Level.INFO,file.getName());

						if (file.getName().endsWith("xls")) {
							logger.log(Level.INFO,"file is Microsoft EXCEL");
							ReadableWorkbook wb = new ReadableWorkbook(file);
							if (wb.open()==true) {
								String[] sheetNames = wb.getSheetNames();
								if (sheetNames != null) {
									this.listBox.removeAll();
									for (String strSheetName : sheetNames) {
										this.listBox.add(strSheetName);
									}
								}
							}
							wb.close();
						}
						else {
							new ShellErrorMessage(FileDropTargetListener.this.parent.getDisplay(),
									FileDropTargetListener.this.parent.getShell(),"Only EXCEL file are dealt with !!!");

						}
					}
					else {
						new ShellErrorMessage(FileDropTargetListener.this.parent.getDisplay(),
								FileDropTargetListener.this.parent.getShell(),"file: "+strFileName[0]+" does not exists!!!");

					}
				}
			}
		}

	}

	public void dragEnter(DropTargetEvent event) {
		logger.finest("Dropped Enter");

		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else if ((event.operations & DND.DROP_LINK) != 0) {
				event.detail = DND.DROP_LINK;
			} else if ((event.operations & DND.DROP_MOVE) != 0) {
				event.detail = DND.DROP_MOVE;
			}
		}
	}

	@Override
	public void dragLeave(DropTargetEvent event) {

	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {

	}

	@Override
	public void dragOver(DropTargetEvent event) {

	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// TODO Auto-generated method stub

	}

	private String getSystemTypeDescription(File file) {
		String strMimeType = "";
		try {

			FileSystemView fsview = FileSystemView.getFileSystemView();
			strMimeType = fsview.getSystemTypeDescription(file);
			logger.log(Level.INFO,strMimeType);
		}  
		catch (InternalError e2) {
			strMimeType = "FileSystemView.getShellFolder: InternalError f= "+file.getAbsolutePath();
			System.err.println("FileSystemView.getShellFolder: InternalError f= "+file.getAbsolutePath());
			e2.printStackTrace();
		}
		return strMimeType;
	}

	public void setListBox(List _listBox) {
		// TODO Auto-generated method stub
		this.listBox = _listBox;
	}

}
