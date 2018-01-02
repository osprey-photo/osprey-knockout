package osprey.competition.knockout;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

import javafx.scene.image.Image;

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
	
	
}
