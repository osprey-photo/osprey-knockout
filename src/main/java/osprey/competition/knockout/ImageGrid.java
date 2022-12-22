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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class ImageGrid<T> extends GridPane {

	public ImageGrid(Model model) {
		super();
		setFocusTraversable(true);

		setHgap(10);
		setVgap(10);

		ArrayList<Image> imgs = new ArrayList<Image>();

		model.getNextRound().getList().forEach((c)->{
			imgs.add(c.getImageOne().image);
			imgs.add(c.getImageTwo().image);
		});;

		double _s = Math.sqrt(imgs.size());
		int columns =(int) Math.ceil(_s);
		int rows =(int) Math.round(_s);
		
		setDimensions(columns,rows);
		for (int j = 0; j < columns; j++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setHgrow(Priority.ALWAYS);
			getColumnConstraints().add(cc);
		}

		for (int j = 0; j < rows; j++) {
			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			getRowConstraints().add(rc);
		}

		int row=0;
		int column=0;

		for (Image img : imgs){
			ImageView imageHouse = new ImageView(img);
			imageHouse.setStyle("-fx-background-color: #000000FF");
			imageHouse.setPreserveRatio(true);
			add(new ImageViewPane(imageHouse),column,row);
			
			column++;
			if (column>columns){
				column=0;
				row++;
			} 
		 }
		

	}
	
	private int columns = 0, rows = 0;

	public void setRowCount(int rows) {
		this.rows = rows;
		setDimensions(columns, rows);
	}

	public void setColumnCount(int columns) {
		this.columns = columns;
		setDimensions(columns, rows);
	}

	private Dimension2D cellSize = null;
	
	public void setCellSize(int width, int height) {
		cellSize = new Dimension2D(width, height);
	}

	private String[][] cellColors = null;
	
	public void setCellColors(int colCount, String... colors) {
		cellColors = new String[colors.length / colCount][];
		for (int row = 0; row < colors.length / colCount; row++) {
			cellColors[row] = new String[colCount];
			for (int col = 0; col < colCount; col++) {
				cellColors[row][col] = colors[row * colCount + col];
			}
		}
	}
	
	private ImageView[] imageViews = null;

	public void setDimensions(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		imageViews = new ImageView[columns * rows];
		getChildren().clear();
	}

	
	private List<Map.Entry<T, String>> imageKeyMapEntries = null;
	
	public List<Map.Entry<T, String>> getImageKeyMapEntries() {
		if (imageKeyMapEntries == null) {
			imageKeyMapEntries = new ArrayList<Map.Entry<T,String>>();
		}
		return imageKeyMapEntries;
	}
	
	private Map.Entry<T, String> lastImageKeyMapEntry = null;
	private Map<T, String> imageKeyMap;
	
	private Map<T, String> getImageKeyMap() {
		if (imageKeyMap == null) {
			imageKeyMap = new HashMap<T, String>();
		}
		if (imageKeyMapEntries != null) {
			int start = imageKeyMapEntries.indexOf(lastImageKeyMapEntry) + 1;
			for (int i = start; i < imageKeyMapEntries.size(); i++) {
				Entry<T, String> entry = imageKeyMapEntries.get(i);
				imageKeyMap.put(entry.getKey(), entry.getValue());
				lastImageKeyMapEntry = entry;
			}
		}
		return imageKeyMap;
	}
	
	public List<T> getImageKeys() {
		return new ArrayList<T>(getImageKeyMap().keySet());
	}

	private Map<Object, Image> images = new HashMap<Object, Image>();

	public void setImage(T imageKey, Image image) {
		images.put(imageKey, image);
	}

	private String imageUrlFormat = null;

	public String getImageUrlFormat() {
		return imageUrlFormat;
	}

	public void setImageUrlFormat(String imageUrlFormat) {
		this.imageUrlFormat = imageUrlFormat;
	}

	ImageView getImageView(T imageKey, int column, int row) {
		int imagePos = row * columns + column;
		ImageView imageView = imageViews[imagePos];
		if (imageView == null) {
			imageView = new ImageView();
			imageViews[imagePos] = imageView;
			Node node = imageView;
			if (cellSize != null) {
				StackPane pane = new StackPane();
				pane.setPrefSize(cellSize.getWidth(), cellSize.getHeight());
				pane.getChildren().add(node);
				if (cellColors != null) {
					String[] rowColors = cellColors[row % cellColors.length];
					String color = rowColors[column % rowColors.length];
					pane.setStyle("-fx-background-color: " + color + ";");
				}
				StackPane.setAlignment(imageView, Pos.CENTER);
				node = pane;
			}
			add(node, column, row);
		}
		return imageView;
	}
	
	public Image setImage(T imageKey, int column, int row) {
		ImageView imageView = getImageView(imageKey, column, row);
		Image image = images.get(imageKey);
		imageView.setImage(image);
		return image;
	}
}