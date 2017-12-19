package osprey.competition.knockout;

import java.util.ArrayList;

import javafx.scene.text.Text;

public class Round {
	
	ArrayList<Combatants> list;
	String roundTitle;
	int roundNumber=0;

	public ArrayList<Combatants> getList() {
		return list;
	}


	public void setList(ArrayList<Combatants> list) {
		this.list = list;
	}


	public Round(int round) {
		this.roundNumber = round;
	}

	
	public int getNumberImages(){
		return list.size();
	}
	
	public String getRoundTitle(){
		
		if (roundNumber == 0){
			return "Elimination";
		} else if (getNumberImages() == 1) {
			return "Final";
		} else if (getNumberImages() == 2) {
			return "Semi-Final";
		} else {
			return ("Round " + roundNumber);
		}
		
	}
	
	
}
