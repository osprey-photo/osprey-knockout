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

import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class VotingPane extends GridPane{
    public VotingPane(Model model){
        super();

        setHgap(10);
		setVgap(10);
		setPadding(new Insets(0, 10, 0, 10));
		setStyle("-fx-background-color: #000000FF");

        Contestants contestants = model.getCurrentContestants();

        ImageView imageHouse = new ImageView(contestants.getImageOne().image);
		imageHouse.setPreserveRatio(true);
		imageHouse.setFitWidth(500);
		add(new ImageViewPane(imageHouse), 0, 0);

		ImageView imageChart = new ImageView(contestants.getImageTwo().image);		
		imageChart.setPreserveRatio(true);
		imageChart.setFitWidth(500);
		add(new ImageViewPane(imageChart), 1, 0);


		// Category in column 2, row 1
		Text category = new Text("[1] " + contestants.getImageOne().title);
		category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		category.setFill(Color.WHITE);
		add(category, 0, 1);

		// Title in column 3, row 1
		Text chartTitle = new Text("[2] " + contestants.getImageTwo().title);
		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		chartTitle.setFill(Color.WHITE);
		add(chartTitle, 1, 1);

		for (int j = 0; j < 2; j++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setHgrow(Priority.ALWAYS);
			getColumnConstraints().add(cc);
		}

		for (int j = 0; j < 2; j++) {
			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			getRowConstraints().add(rc);
		}


    }
    
}
