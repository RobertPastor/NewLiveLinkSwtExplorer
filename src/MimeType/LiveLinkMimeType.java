package MimeType;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;


/**
 * 
 * @author t0007330
 * @since June 2009
 * 
 */
public class LiveLinkMimeType {

	private String LiveLinkMimeType = "";
	private String DocumentSuffix = "";
	private Icon icon = null;
	private ImageData imageData = null;
	
	public LiveLinkMimeType(String LLmimeType, String DocSuffix) {
		this.LiveLinkMimeType = LLmimeType;
		this.DocumentSuffix = DocSuffix;
		initIcon();
	}
	
	private void initIcon () {
		
		if (this.DocumentSuffix.length() > 0) {
			String suffix = "."+this.DocumentSuffix;
			File file = null;
			try {
				file = File.createTempFile("temp", suffix, new File("E:/"));
			}
			catch (IOException e) {
				System.err.println("LiveLinkMimeTypeSet: get Icon: "+e.getMessage());
				e.printStackTrace();
			}
			
			FileSystemView fsview = FileSystemView.getFileSystemView();
			this.icon = fsview.getSystemIcon(file);
			this.imageData = getImageData(file);
			file.delete();
		}	
	}

	public String getLiveLinkMimeType() {
		return this.LiveLinkMimeType;
	}

	public String getDocumentSuffix() {
		return this.DocumentSuffix;
	}

	public Icon getIcon() {
		return this.icon;
	}
	
	static ImageData getImageData(File file) {
	    ImageIcon systemIcon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);
	    java.awt.Image image = systemIcon.getImage();
	    if (image instanceof BufferedImage) {
	        return convertToSWT((BufferedImage)image);
	    }
	    int width = image.getWidth(null);
	    int height = image.getHeight(null);
	    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();
	    return convertToSWT(bufferedImage);
	}
	
	static ImageData convertToSWT(BufferedImage bufferedImage) {
	    if (bufferedImage.getColorModel() instanceof DirectColorModel) {
	        DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
	        PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
	        ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
	        for (int y = 0; y < data.height; y++) {
	                for (int x = 0; x < data.width; x++) {
	                        int rgb = bufferedImage.getRGB(x, y);
	                        int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF)); 
	                        data.setPixel(x, y, pixel);
	                        if (colorModel.hasAlpha()) {
	                                data.setAlpha(x, y, (rgb >> 24) & 0xFF);
	                        }
	                }
	        }
	        return data;            
	    } else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
	        IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
	        int size = colorModel.getMapSize();
	        byte[] reds = new byte[size];
	        byte[] greens = new byte[size];
	        byte[] blues = new byte[size];
	        colorModel.getReds(reds);
	        colorModel.getGreens(greens);
	        colorModel.getBlues(blues);
	        RGB[] rgbs = new RGB[size];
	        for (int i = 0; i < rgbs.length; i++) {
	                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
	        }
	        PaletteData palette = new PaletteData(rgbs);
	        ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
	        data.transparentPixel = colorModel.getTransparentPixel();
	        WritableRaster raster = bufferedImage.getRaster();
	        int[] pixelArray = new int[1];
	        for (int y = 0; y < data.height; y++) {
	                for (int x = 0; x < data.width; x++) {
	                        raster.getPixel(x, y, pixelArray);
	                        data.setPixel(x, y, pixelArray[0]);
	                }
	        }
	        return data;
	    }
	    return null;
	}

	public ImageData getImageData() {
		return imageData;
	}

	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}

}
