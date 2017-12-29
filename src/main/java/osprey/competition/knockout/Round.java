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

	public Combatants get(int index){
		return list.get(index);
	}

	public void setList(ArrayList<Combatants> list) {
		this.list = list;
	}

	public Round(int round) {
		this.roundNumber = round;
	}

	public int getNumberCombatants(){
		return list.size();
	}
	
	public String getRoundTitle(){
		
		if (roundNumber == 0){
			return "Elimination";
		} else if (getNumberCombatants() == 1) {
			return "Final";
		} else if (getNumberCombatants() == 2) {
			return "Semi-Final";
		} else {
			return ("Round " + roundNumber);
		}
		
	}
	
	
}
