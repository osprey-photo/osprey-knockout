
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

import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

public class Contestants {

	ArrayList<ImageWrapper> imageWrappers;
	int currentContestant = 0;
	SimpleIntegerProperty currentProperty = new SimpleIntegerProperty();
	Model model;

	@Deprecated
	public Contestants(Model model, ImageWrapper imageOne, ImageWrapper imageTwo){
		imageWrappers = new ArrayList<>(2);
		imageWrappers.add(imageOne);
		imageWrappers.add(imageTwo);

		this.model=model;
	}

	public Contestants(Model model, ImageWrapper[] images){
		this.imageWrappers = (ArrayList<ImageWrapper>) Arrays.asList(images);
		this.model = model;
	}

	@Deprecated
	public ImageWrapper getImageOne() {
		return this.imageWrappers.get(0);
	}

	@Deprecated
	public ImageWrapper getImageTwo() {
		return this.imageWrappers.get(1);
	}

	public int getNumberContestants(){
		return this.imageWrappers.size();
	}

	public ImageWrapper getCurrentContestantImage(){
		return this.imageWrappers.get(currentContestant);
	}

	public ImageWrapper getContestantImage(Number contestant){
		return this.imageWrappers.get(contestant.intValue());
	}

	public ImageWrapper nextContestantImage(){
		currentContestant++;
		if (currentContestant==imageWrappers.size()){
			return null;
		}

		currentProperty.set(currentContestant);	
		return this.imageWrappers.get(currentContestant);
	}

	public ImageWrapper previousContestantImage(){
		if (currentContestant != 0){
			currentContestant--;
		}
		currentProperty.set(currentContestant);
		return this.imageWrappers.get(currentContestant);
	}
	
	public String toString(){
		return getImageOne() +" vs "+ getImageTwo();
	}
	
	public void bind(ChangeListener<Number> listener){
		currentProperty.addListener(listener);
	}

	/** Mark which contestant is the winner (zero based)
	 * 
	 * @return true if mark succesful, false if outside number of contestants
	 */
	public boolean markVotedImage(int winner){
		if (winner >= imageWrappers.size()){
			return false;
		}
		imageWrappers.remove(winner);
		imageWrappers.forEach((image)->{
			this.model.markAsFailed(image);
		});
		return true;
	}



}
