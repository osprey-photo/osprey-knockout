package osprey.competition.knockout;

import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ImageViewer implements Controller {

	/**
	 */
	public enum STATE {
		ROUND_TITLE, FULL_IMAGE_1, FULL_IMAGE_2, VOTE, WINNER
	};

	STATE state;

	Image image1, image2;
	Model model;

	Round currentRound;

	Combatants currentImages;
	Scene scene;
	ImageView selectedImage1;
	// Server restServer;

	private static Logger logger = Logger.getLogger(ImageViewer.class.getName());

	public ImageViewer() {

		logger.info("<init>");
	}


	public void init(Model model)  {
		// super.init();
		logger.info("init");


		this.model = model;
		// get the controller, and start up the rest server
		// restServer = new Server((Controller) this, model);
		// restServer.go();

		currentRound = model.getNextRound();

		// currentRoundNumber = 1;
		state = STATE.ROUND_TITLE;
	}

	// @Override
	// public void stop() throws Exception {
	// 	super.stop();
	// }

	public VBox getRoundTitle() {
		VBox root = new VBox();
		
		root.setStyle("-fx-background-color: #000000FF");
		logger.info("getRoundTtitle");

		Text bwps = new Text(model.getMainTitle());
		bwps.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		bwps.setFill(Color.WHITE);

		Text chartTitle = new Text(this.currentRound.getRoundTitle());
		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		chartTitle.setFill(Color.WHITE);

		Text subTitle = new Text("S to start");
		subTitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		subTitle.setFill(Color.WHITE);

		root.setAlignment(Pos.CENTER);
		root.getChildren().add(bwps);
		root.getChildren().add(chartTitle);
		root.getChildren().add(subTitle);
		return root;
	}

	public VBox getFullImageView() {

		logger.info("> getFullImageView");

		if (model.isWinner()) {
			logger.info("WINNER IS..." + model.getWinner());
			return getWinner();

		} else if (model.isRoundComplete()) {
			logger.info("getting next round");
			currentRound = model.getNextRound();

			state = STATE.ROUND_TITLE;
			return getRoundTitle();
		}

		currentImages = model.moveToNext(); // list.get(current);
		logger.info("moving to the next images " + currentImages);
		VBox root = new VBox();
		root.setStyle("-fx-background-color: #000000FF");
		selectedImage1 = new ImageView();
		_setImg(currentImages.imageOne);

		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(selectedImage1);
		logger.info("< getFullImageView");
		return root;
	}

	private void _setImg(ImageWrapper wrapper) {
		selectedImage1.setImage(wrapper.image);
		if (wrapper.getRatio() >= 1.0) {
			selectedImage1.setFitHeight(model.getFullSizeHeight());
		} else {
			selectedImage1.setFitWidth(model.getFullSizeWidth());
		}
		selectedImage1.setPreserveRatio(true);
	}

	private VBox getWinner() {
		state = STATE.WINNER;

		VBox root = new VBox();
		root.setStyle("-fx-background-color: #000000FF");
		ImageView winnerView = new ImageView();

		ImageWrapper winner = model.getWinner();
		winnerView.setPreserveRatio(true);
		winnerView.setImage(winner.image);
		winnerView.setFitWidth(model.getWinnerWidth());
		winnerView.setFitHeight(model.getWinnerHeight());

		root.setAlignment(Pos.CENTER);

		Text chartTitle = new Text("WINNER");
		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		chartTitle.setFill(Color.WHITE);

		Text subTitle = new Text(winner.author + " - " + winner.title);
		subTitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		subTitle.setFill(Color.WHITE);

		root.getChildren().add(chartTitle);
		root.getChildren().add(winnerView);
		root.getChildren().add(subTitle);

		return root;
	}


	public void start(Stage stage) {
		try {
			stage.setTitle("Image Viewer");
			stage.setWidth(800);
			stage.setHeight(800);
			scene = new Scene(new Group());
			
			// scene.setFill(Color.BLACK);
			scene.setRoot(getRoundTitle());

			state = STATE.ROUND_TITLE;
			stage.setFullScreen(true);
			stage.setScene(scene);
			stage.show();

			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					try {
						KeyCode kc = ke.getCode();

						if (kc.equals(KeyCode.LEFT)) {
							back();
						} else if (kc.equals(KeyCode.RIGHT)) {
							next();
						} else if (kc.equals(KeyCode.DIGIT1)) {
							vote_one();
						} else if (kc.equals(KeyCode.DIGIT2)) {
							vote_two();
						} else if (kc.equals(KeyCode.M)) {
							switch (state) {
							case FULL_IMAGE_2:
								scene.setRoot(addGridPane());
								state = STATE.VOTE;
								break;
							default:
								break;
							}

						} else if (kc.equals(KeyCode.S)) {
							start();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GridPane addGridPane() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));
		grid.setStyle("-fx-background-color: #000000FF");
		ImageView imageHouse = new ImageView(currentImages.imageOne.image);
		imageHouse.setPreserveRatio(true);
		// imageHouse.autosize();
		imageHouse.setFitWidth(500);
		grid.add(new ImageViewPane(imageHouse), 0, 0);

		ImageView imageChart = new ImageView(currentImages.imageTwo.image);
		// imageChart.autosize();
		imageChart.setPreserveRatio(true);
		imageChart.setFitWidth(500);
		grid.add(new ImageViewPane(imageChart), 1, 0);

		// Category in column 2, row 1
		Text category = new Text("[1] " + currentImages.imageOne.title);
		category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		category.setFill(Color.WHITE);

		grid.add(category, 0, 1);

		// Title in column 3, row 1
		Text chartTitle = new Text("[2] " + currentImages.imageTwo.title);
		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		chartTitle.setFill(Color.WHITE);
		grid.add(chartTitle, 1, 1);

		for (int j = 0; j < 2; j++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().add(cc);
		}

		for (int j = 0; j < 2; j++) {
			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			grid.getRowConstraints().add(rc);
		}

		return grid;
	}

	// controller interface methods

	public void next() {
		switch (state) {
		case FULL_IMAGE_1:
			// selectedImage1.setImage(currentImages.imageTwo.image);
			_setImg(currentImages.imageTwo);
			state = STATE.FULL_IMAGE_2;
			break;
		case FULL_IMAGE_2:
			scene.setRoot(addGridPane());
			state = STATE.VOTE;
			break;
		default:
			break;
		}
	}

	public void back() {
		switch (state) {
		case FULL_IMAGE_2:
			// selectedImage1.setImage(currentImages.imageOne.image);
			_setImg(currentImages.imageOne);
			state = STATE.FULL_IMAGE_1;
			break;
		default:
			break;
		}
	}

	public void vote_one() {
		switch (state) {
		case VOTE:
			model.markAsFailed(currentImages.imageTwo);
			state = STATE.FULL_IMAGE_1;
			scene.setRoot(getFullImageView());
			break;
		default:
			break;
		}
	}

	public void vote_two() {
		switch (state) {
		case VOTE:
			model.markAsFailed(currentImages.imageOne);

			state = STATE.FULL_IMAGE_1;
			scene.setRoot(getFullImageView());
			break;
		default:
			break;
		}
	}

	public void start() {
		switch (state) {
		case ROUND_TITLE:
			scene.setRoot(getFullImageView());
			state = STATE.FULL_IMAGE_1;
			break;
		default:
			break;
		}
	}

}
