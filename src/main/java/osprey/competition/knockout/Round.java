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

public class Round {
	
	ArrayList<Contestants> list;
	String roundTitle;
	int roundNumber=0;

	public ArrayList<Contestants> getList() {
		return list;
	}

	public Contestants get(int index){
		return list.get(index);
	}

	public void setList(ArrayList<Contestants> list) {
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
		} else if (getNumberCombatants() == 4) {
			return "Quarter-Final";
		} else {
			return ("Round " + roundNumber);
		}
		
	}
	
	
}
