package FileExplorerTab;

import java.io.File;
import java.util.Observable;

public class FileObservable extends Observable {
	
	private File file = null;
	private String filePath = "";
	
	public FileObservable(File file) {
		this.file = file;
		
	}
	
	public File getFile() {
		return this.file;
	}
	
	
	public void setValue(final String filePath) {
		
		this.setFilePath(filePath);
		// mark as value changed
        setChanged();
        // trigger notification
        notifyObservers();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
