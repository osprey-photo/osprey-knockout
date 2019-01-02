package osprey.competition.knockout;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;

public class ImageWrapper {

	String filename;
	String basename;
	int uid;
	String title;
	String author;
	
	Image image;
	
	boolean success = true;

	public double getRatio(){
		return image.getHeight()/image.getWidth();
	}

	public int getHeight(){
		return (int)image.getHeight();
	}
	
	public int getWidth(){
		return (int)image.getWidth();
	}

	public String getBasename(){
		return this.basename;
	}

	public String getFilename(){
		return this.filename;
	}

	public String getTitle(){
		return this.title;
	}

	public String getAuthor(){
		return this.author;
	}
	
	private static Logger logger = Logger.getLogger(ImageWrapper.class.getName());
	
	public ImageWrapper(File filename) throws Exception{
		this.filename = filename.getAbsolutePath();
		this.basename = filename.getName();
		logger.info("Loading for "+filename);
		FileInputStream fis = new FileInputStream(filename);
		image = new Image(fis,1400,1050,true,false);
		logger.info("loaded file "+filename);
	}
	
	public String toString(){
		return basename+":"+success;
	}

	@Override
	public boolean equals(Object obj) {
		return 	((ImageWrapper)obj).uid == this.uid;
	}
	
    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }	
	
}
