package osprey.competition.knockout;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Model {

	static String imageDir = "src.image.dir";
	static String propsFile = "props.file";

	StateStore props;

	Round currentRound;
	int currentCombatantsIndex;

	Consumer<String> feedbackSupplier;

	// logger for debug output
	private static Logger logger = Logger.getLogger(Model.class.getName());

	public Model() throws Exception {
		logger.info("Model <init>");
	}

	public void setFeedbackSupplier(Consumer<String> s){
		this.feedbackSupplier = s;
	}

	public void init() throws Exception {

		props = StateStore.getStateStore();
		images = new ArrayList<ImageWrapper>();

		scanDirectory();
	}

	ArrayList<ImageWrapper> images;
	int round = 0;
	int currentImage = 0;

	public Round getCurrentRound() {
		return this.currentRound;
	}

	public int getCurrentIndex() {
		return this.currentCombatantsIndex;
	}

	public Round getNextRound() {
		logger.info("> getNextRound");
		round++;
		roundDone = false;
		logger.info("current round (zero based)=" + round);

		Round r = new Round(round);
		r.setList(getNextRoundImages());
		this.currentRound = r;
		this.currentCombatantsIndex = -1;
		logger.info("< getNextRound");
		return r;
	}

	private ArrayList<Combatants> getNextRoundImages() {
		logger.info("> getNextRoundImages ");
		// scan the images and remove the ones that not selected
		// need to clean out the failed images
		for (int count = images.size() - 1; count > 0; count--) {
			if (!images.get(count).success) {
				images.remove(count);
			}
		}
		logger.info("image list size " + images.size());

		// mix things up a bits
		Collections.shuffle(images);

		ArrayList<Combatants> nextRounds = new ArrayList<>();

		// need to work out how many images to get in this round
		int numberImages = images.size();
		int numberToTrim = numberImages - hob(numberImages);
		logger.info("number to trim is " + numberToTrim);

		int numberThisRound = (numberToTrim == 0) ? numberImages : (numberToTrim * 2);
		logger.info("number this round " + numberThisRound);
		// loop over and get the correct set of images
		for (int loop = 0; loop < numberThisRound; loop += 2) {
			Combatants c = new Combatants(images.get(loop), images.get(loop + 1));
			nextRounds.add(c);
		}
		logger.info("number in this new round is " + nextRounds.size());

		logger.info("< getNextRoundImages ");
		return nextRounds;
	}

	boolean roundDone = false;

	/**
	 * Move to the next pair of images.
	 * 
	 */
	public Combatants moveToNext() {
		this.currentCombatantsIndex++;
		roundDone = ((currentCombatantsIndex + 1) == this.currentRound.getNumberCombatants());
		return this.currentRound.get(this.currentCombatantsIndex);
	}

	public boolean isRoundComplete() {
		logger.info("Current index = " + this.currentCombatantsIndex + " round number="
				+ this.currentRound.getNumberCombatants());
		return (roundDone);
	}

	/**
	 * Mark an image as having not been voted in
	 */
	public void markAsFailed(ImageWrapper imageWrapper) {
		imageWrapper.success = false;
		boolean result = images.remove(imageWrapper);
		System.out.println(images);
		if (!result) {
			throw new RuntimeException("Image not in list");
		}
	}

	// get the list of image files in the specified directory
	private void scanDirectory() throws Exception {
		String d = props.getProperty(imageDir);
		if (d == null || d.trim().equals("")) {
			throw new Exception("No specified img directory -D" + imageDir);
		} else {
			logger.info("Reading images from "+imageDir);
		}

		//  TODO: Check the cache directory exists
		String files[] = new File(d).list(new OnlyJpgs());
		File dataDir = java.nio.file.Paths.get(props.getFileCache(), "public", "imgs","thumbnails").toFile();
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}

		String dest = java.nio.file.Paths.get(props.getFileCache(), "public", "imgs").toString();
		String destThumbail =dataDir.toString();
		// copy them to the local cache

		// overwrite existing file, if exists
		CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };

		if (!isPowerOf2(files.length)) {
			logger.info("Will need knockout round = Incorrect number of images");
			this.feedbackSupplier.accept("Will need knockout round");
			this.round = -1;
		}
		int uid = 0;
		logger.info("Loading files from " + d);

		for (String f : files) {
			try {
				Path from = new File(d, f).toPath();
				Path to = new File(dest, f).toPath();
				Files.copy(from, to, options);
				this.feedbackSupplier.accept(f.toString());
				ImageWrapper im = new ImageWrapper(new File(dest, f));
				im.uid = uid++;

				int i0 = f.indexOf(".");
				int i1 = f.indexOf("_");
				i1 = (i1 == -1) ? f.indexOf("-") : i1;

				im.title = f.substring(i1 + 1, i0).trim();
				im.author = f.substring(0, i1).trim();
				logger.fine("Image ratio = " + im.getRatio());
				images.add(im);

				// do the thumbnail resize
				double r =  im.getRatio();
				int scaledHeight,scaledWidth;
				if (r>=1){
					scaledWidth = 300;
					scaledHeight = (int)(300 * r);
				} else {
					scaledHeight = 600;
					scaledWidth = (int)(600 * r);
				}
				String tbName = new File(destThumbail, f).toPath().toString();
				ImageWrapper.resize(to.toString(), tbName, scaledWidth, scaledHeight);

			} catch (Exception e) {
				logger.warning(e.getMessage() + ":" + f);
				Exception newException = new Exception("unable to parse the file name " + f);
				newException.initCause(e);
				throw newException;
			}
		}

		logger.info("Loaded files " + images.size());
		this.feedbackSupplier.accept("Loaded files " + images.size());
		this.feedbackSupplier.accept("Good to run competition");
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

	protected int hob(int num) {
		int ret = 1;
		while ((num >>= 1) != 0) {
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