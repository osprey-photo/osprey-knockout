
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class FullImagePane extends VBox implements ChangeListener<Number> {
	private static Logger logger = Logger.getLogger(ImageViewer.class.getName());
  private Contestants contestants;
  private ImageView imgView;
  private Model model;

  public FullImagePane(Model model) {
    super();

    this.model = model;
    contestants = model.getCurrentContestants();
    contestants.bind(this);

    setStyle("-fx-background-color: #000000FF");
    this.imgView = new ImageView();   
    setAlignment(Pos.CENTER);
    getChildren().addAll(imgView);
    _setImg(contestants.getCurrentContestantImage());

  }

  private void _setImg(ImageWrapper wrapper) {
    imgView.setImage(wrapper.image);
    if (wrapper.getRatio() >= 1.0) {
      imgView.setFitHeight(model.getFullSizeHeight());
    } else {
      imgView.setFitWidth(model.getFullSizeWidth());
    }
    imgView.setPreserveRatio(true);
  }

  @Override
  public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
    logger.info("FullImagePane update event "+newValue);
    _setImg(this.contestants.getContestantImage(newValue));
  }

}
