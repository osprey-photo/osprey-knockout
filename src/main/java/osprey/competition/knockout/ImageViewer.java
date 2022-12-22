// Copyright 2018-2022 Matthew B White

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package osprey.competition.knockout;

import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageViewer implements Controller {

	/**
	 */
	public enum STATE {
		ROUND_TITLE, FULL_IMAGE_1, FULL_IMAGE_2, FULL_IMAGE, VOTE, WINNER
	};

	STATE state;

	Image image1, image2;
	Model model;

	Scene scene;
	ImageView selectedImage1;

	private static Logger logger = Logger.getLogger(ImageViewer.class.getName());

	public ImageViewer() {
		logger.info("<init>");
	}


	public void init(Model model)  {
		logger.info("init");
		this.model = model;
		this.model.getNextRound(); // swap to init
		state = STATE.ROUND_TITLE;
	}

	/**
	 * Create a view of a single image
	 * 
	 * @return
	 */
	public VBox getFullImageView() {

		logger.info("> getFullImageView");

		Contestants currentImages = model.moveToNext(); // list.get(current);
		FullImagePane fullImage = new FullImagePane(model);
		
		logger.info("< getFullImageView");
		return fullImage;
	}

	private VBox getWinner() {
		state = STATE.WINNER;
		return new WinnerPane(model);
	}

	

	public void start(Stage stage) {
		try {
			stage.setTitle("Image Viewer");
			stage.setWidth(800);
			stage.setHeight(800);
			scene = new Scene(new Group());
			
			scene.setRoot(new TitlePane(model));
			
			state = STATE.ROUND_TITLE;
			stage.setFullScreen(true);
			stage.setScene(scene);
			stage.show();

			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					try {
						KeyCode kc = ke.getCode();
						logger.info("Key Event "+kc);
						if (kc.equals(KeyCode.LEFT)) {
							back();
						} else if (kc.equals(KeyCode.RIGHT)) {
							next();
						} else if (kc.equals(KeyCode.DIGIT1)) {
							vote(0);
						} else if (kc.equals(KeyCode.DIGIT2)) {
							vote(1);
						} else if (kc.equals(KeyCode.DIGIT3)) {
							vote(2);
						} else if (kc.equals(KeyCode.DIGIT4)) {
							vote(3);
						}else if (kc.equals(KeyCode.S)) {
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

	// controller interface methods
	public void next() {
		switch (state) {
		case FULL_IMAGE:
		case FULL_IMAGE_1:
		case FULL_IMAGE_2:
			ImageWrapper next = this.model.getCurrentContestants().nextContestantImage();
			if (next!=null){
				state = STATE.FULL_IMAGE;
			}
			else {
			 	scene.setRoot(new VotingPane(model));
			 	state = STATE.VOTE;
			}
			break;
		default:
			break;
		}
	}

	public void back() {
		switch (state) {
		case FULL_IMAGE:
			this.model.getCurrentContestants().previousContestantImage();
			state = STATE.FULL_IMAGE;
			break;
		default:
			break;
		}
	}

	@Deprecated
	public void vote_one() {
		switch (state) {
		case VOTE:
			model.getCurrentContestants().markVotedImage(0);			
			state = STATE.FULL_IMAGE_1;

			
			break;
		default:
			break;
		}
	}

	@Deprecated
	public void vote_two() {
		switch (state) {
		case VOTE:
			model.getCurrentContestants().markVotedImage(1);			
			state = STATE.FULL_IMAGE_1;
			scene.setRoot(getFullImageView());
			break;
		default:
			break;
		}
	}

	public void vote(int winner) {
		switch (state) {
		case VOTE:
			model.getCurrentContestants().markVotedImage(winner);			

			if (model.isWinner()){
				state = STATE.WINNER;
				logger.info("WINNER IS..." + model.getWinner());
				scene.setRoot(getWinner());
			} else if (model.isRoundComplete()){
				state = STATE.ROUND_TITLE;
				logger.info("getting next round");
				model.getNextRound();
				state = STATE.ROUND_TITLE;
				scene.setRoot(new TitlePane(model));
			} else {
				state = STATE.FULL_IMAGE;
				scene.setRoot(getFullImageView());
			}

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
