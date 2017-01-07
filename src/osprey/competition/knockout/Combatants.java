package osprey.competition.knockout;

public class Combatants {

	ImageWrapper imageOne;
	ImageWrapper imageTwo;
	
	public Combatants(ImageWrapper imageOne, ImageWrapper imageTwo){
		this.imageOne = imageOne;
		this.imageTwo = imageTwo;
	}

	public ImageWrapper getImageOne() {
		return imageOne;
	}

	public ImageWrapper getImageTwo() {
		return imageTwo;
	}
	
	public String toString(){
		return imageOne+" vs "+ imageTwo;
	}
	
}
