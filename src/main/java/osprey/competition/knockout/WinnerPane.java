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

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class WinnerPane extends VBox {
    
    public WinnerPane(Model model){
        setStyle("-fx-background-color: #000000FF");
		ImageView winnerView = new ImageView();

		ImageWrapper winner = model.getWinner();
		winnerView.setPreserveRatio(true);
		winnerView.setImage(winner.image);
		winnerView.setFitWidth(model.getWinnerWidth());
		winnerView.setFitHeight(model.getWinnerHeight());

		setAlignment(Pos.CENTER);

		Text chartTitle = new Text("WINNER");
		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		chartTitle.setFill(Color.WHITE);

		Text subTitle = new Text(winner.author + " - " + winner.title);
		subTitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		subTitle.setFill(Color.WHITE);

		getChildren().add(chartTitle);
		getChildren().add(winnerView);
		getChildren().add(subTitle);
    }
}
