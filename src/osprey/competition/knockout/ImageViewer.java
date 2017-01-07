package osprey.competition.knockout;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ImageViewer extends Application {

	public enum STATE {
		ROUND_TITLE, FULL_IMAGE_1, FULL_IMAGE_2, VOTE, WINNER
	};

	STATE state;

	Image image1, image2;

	Model model;

	// preload the list of
	ArrayList<Combatants> list;
	Round currentRound;

	Combatants currentImages;
	int current = 0;
	int imagesInThisRound;
	int currentRoundNumber = 0;

	private static Logger logger = Logger.getLogger(ImageViewer.class.getName());

	@Override
	public void init() throws Exception {

		super.init();

		model = new Model();
		model.init();
		
		currentRound = model.getNextRound();

		list = currentRound.getList();
		imagesInThisRound = currentRound.getNumberImages();
		currentRoundNumber = 1;

		state = STATE.ROUND_TITLE;
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
	}

	ImageView selectedImage1;

	public VBox getRoundTitle() {
		VBox root = new VBox();

		Text bwps = new Text("Bishop's Waltham\nPhotographic Society\n");
		bwps.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		bwps.setFill(Color.WHITE);

		Text chartTitle = new Text( this.currentRound.getRoundTitle() );
//		if (imagesInThisRound == 1) {
//			chartTitle = new Text("Final");
//		} else if (imagesInThisRound == 2) {
//			chartTitle = new Text("Semi-Final");
//		} else {
//			chartTitle = new Text("Round " + currentRoundNumber);
//		}

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

		if (model.isWinner()) {
			logger.info("WINNER IS..." + model.getWinner());
			return getWinner();

		} else if (current == imagesInThisRound) {
			currentRound = model.getNextRound();
			
			list = currentRound.getList();
//			model.getNextRoundImages();
			imagesInThisRound = list.size();
			current = 0;
			currentRoundNumber++;
			state = STATE.ROUND_TITLE;
			return getRoundTitle();
		}

		currentImages = list.get(current);

		VBox root = new VBox();

		selectedImage1 = new ImageView();
		selectedImage1.setPreserveRatio(true);
		selectedImage1.setFitWidth(model.getFullSizeWidth());
		selectedImage1.setFitHeight(model.getFullSizeHeight());
		selectedImage1.setImage(currentImages.imageOne.image);

		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(selectedImage1);

		return root;
	}

	private VBox getWinner() {
		state = STATE.WINNER;

		VBox root = new VBox();

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

		Text subTitle = new Text(winner.author+" - " + winner.title);
		subTitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		subTitle.setFill(Color.WHITE);

		root.getChildren().add(chartTitle);
		root.getChildren().add(winnerView);
		root.getChildren().add(subTitle);

		return root;
	}

	@Override
	public void start(Stage stage) {
		try {
			stage.setTitle("Image Viewer");
			stage.setWidth(800);
			stage.setHeight(800);
			Scene scene = new Scene(new Group());

			scene.setFill(Color.BLACK);
			scene.setRoot(getRoundTitle());

			state = STATE.ROUND_TITLE;
			stage.setFullScreen(true);
			stage.setScene(scene);
			stage.show();

			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					try {
						KeyCode kc = ke.getCode();
						// System.out.println(kc);
						if (kc.equals(KeyCode.LEFT)) {

							switch (state) {
							case FULL_IMAGE_2:
								selectedImage1.setImage(currentImages.imageOne.image);
								state = STATE.FULL_IMAGE_1;
								break;
							default:
								break;
							}

						} else if (kc.equals(KeyCode.RIGHT)) {
							switch (state) {
							case FULL_IMAGE_1:
								selectedImage1.setImage(currentImages.imageTwo.image);
								state = STATE.FULL_IMAGE_2;
								break;
							case FULL_IMAGE_2:
								scene.setRoot(addGridPane());
								state = STATE.VOTE;
								break;
							default:
								break;
							}

						} else if (kc.equals(KeyCode.DIGIT1)) {
							switch (state) {
							case VOTE:
								model.markAsFailed(currentImages.imageTwo);
								current++;
								state = STATE.FULL_IMAGE_1;
								scene.setRoot(getFullImageView());
								break;
							default:
								break;
							}

						} else if (kc.equals(KeyCode.DIGIT2)) {
							switch (state) {
							case VOTE:
								model.markAsFailed(currentImages.imageOne);
								current++;
								state = STATE.FULL_IMAGE_1;
								scene.setRoot(getFullImageView());
								break;
							default:
								break;
							}
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
							switch (state) {
							case ROUND_TITLE:
								scene.setRoot(getFullImageView());
								state = STATE.FULL_IMAGE_1;
								break;
							default:
								break;
							}
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

	public static void main(String[] args) {
		launch(args);
	}
}

/**
 *
 * @author akouznet
 */
class ImageViewPane extends Region {

	private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<ImageView>();

	public ObjectProperty<ImageView> imageViewProperty() {
		return imageViewProperty;
	}

	public ImageView getImageView() {
		return imageViewProperty.get();
	}

	public void setImageView(ImageView imageView) {
		this.imageViewProperty.set(imageView);
	}

	public ImageViewPane() {
		this(new ImageView());
	}

	@Override
	protected void layoutChildren() {
		ImageView imageView = imageViewProperty.get();
		if (imageView != null) {

			imageView.setFitWidth(getWidth());
			imageView.setFitHeight(getHeight());
			layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
		}
		super.layoutChildren();
	}

	public ImageViewPane(ImageView imageView) {
		imageViewProperty.addListener(new ChangeListener<ImageView>() {

			@Override
			public void changed(ObservableValue<? extends ImageView> arg0, ImageView oldIV, ImageView newIV) {
				if (oldIV != null) {
					getChildren().remove(oldIV);
				}
				if (newIV != null) {
					getChildren().add(newIV);
				}
			}
		});
		this.imageViewProperty.set(imageView);
	}
}