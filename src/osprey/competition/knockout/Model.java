package osprey.competition.knockout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

public class Model {

	static String imageDir = "src.image.dir";

	static String propsFile = "props.file";

	Properties props;

	private static Logger logger = Logger.getLogger(Model.class.getName());

	public Model() throws Exception {
		logger.info("Model <init>");

	}

	public void init() throws Exception {

		String d = System.getProperty(propsFile);

		if (d == null || d.trim().equals("")) {
			d="./imageviewer.props";
			logger.info("No specified props file -D" + propsFile +" assuming "+d);
		}

		props = new Properties();
		FileInputStream inStream = new FileInputStream(d);
		props.load(inStream);
		inStream.close();

		images = new ArrayList<ImageWrapper>();

		scanDirectory();
	}

	ArrayList<ImageWrapper> images;
	int round = 0;

	int currentImage = 0;

	

	public Round getNextRound(){
		
		round++;
		
		System.out.println(round);
		
		Round r = new Round(round);
		r.setList(getNextRoundImages());
		
		return r;
		
	}
	
	public ArrayList<Combatants> getNextRoundImages() {
	

		// scan the images and remove the ones that not selected
		// need to clean out the failed images
		for (int count = images.size() - 1; count > 0; count--) {
			if (!images.get(count).success) {
				images.remove(count);
			}
		}
		
		// mix things up a bits
		Collections.shuffle(images);

		ArrayList<Combatants> nextRounds = new ArrayList<>();

		// need to work out how many images to get in this round
		int numberImages = images.size();
		int numberToTrim = numberImages - hob(numberImages);
		
		int numberThisRound = (numberToTrim==0) ? numberImages : (numberToTrim*2);
		logger.info("number this round "+numberThisRound);
		// loop over and get the correct set of images
		for (int loop = 0; loop < numberThisRound; loop += 2) {
			Combatants c = new Combatants(images.get(loop), images.get(loop + 1));
			nextRounds.add(c);
		}

		return nextRounds;

	}

	public void markAsFailed(ImageWrapper imageWrapper) throws Exception {
		imageWrapper.success = false;
		boolean result = images.remove(imageWrapper);
		if (!result) {
			throw new Exception("Image not in list");
		}
	}

	// get the list of image files in the specified directory
	private void scanDirectory() throws Exception {
		String d = props.getProperty(imageDir);
		if (d == null || d.trim().equals("")) {
			throw new Exception("No specified img directory -D" + imageDir);
		}

		String files[] = new File(d).list(new OnlyJpgs());

		if (!isPowerOf2(files.length)) {
			logger.info("Will need knockout round = Incorrect number of images");
			this.round = -1;
//			throw new Exception("Incorrect number of images :" + files.length);
		}
		int uid = 0;
		logger.info("Loading files from " + d);

		for (String f : files) {
			try {
				ImageWrapper im = new ImageWrapper(new File(d, f));
				im.uid = uid++;

				int i0 = f.indexOf(".");
				int i1 = f.indexOf("_");
				i1 = (i1 == -1) ? f.indexOf("-") : i1;
				
				im.title = f.substring(i1 + 1, i0).trim();
				im.author = f.substring(0, i1).trim();

				images.add(im);
			} catch (Exception e) {
				logger.warning(e.getMessage() + ":" + f);
				Exception newException = new Exception("unable to parse the file name " + f);
				newException.initCause(e);
				throw newException;
			}
		}

		logger.info("Loaded files " + images.size());

		// write out a summary of all the files with author and title
		for (ImageWrapper im : images) {
			System.out.println(im.author+","+im.title);
		}
		
	}

	protected boolean isPowerOf2(int value) {
		if (value == 2) {
			return true;
		} else if ((value < 2) || (value % 2) != 0) {
			return false;
		} else {
			return isPowerOf2(value / 2);
		}
	}
	
	protected int hob(int num)
	{
	    int ret = 1;
	    while ((num >>= 1) !=0 ){
	        ret <<= 1;
	    }
	    return ret;
	}

	public boolean isWinner() {
		return (images.size() == 1);
	}

	public ImageWrapper getWinner() {
		return images.get(0);
	}

	public double getFullSizeWidth() {
		return Double.parseDouble(props.getProperty("fullsize.width"));
	}

	public double getFullSizeHeight() {
		// TODO Auto-generated method stub
		return Double.parseDouble(props.getProperty("fullsize.height"));
	}

	public double getWinnerWidth() {
		return Double.parseDouble(props.getProperty("winner.width"));
	}

	public double getWinnerHeight() {
		return Double.parseDouble(props.getProperty("winner.height"));
	}

}

class OnlyJpgs implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")) {
			return true;
		}

		return false;
	}

}