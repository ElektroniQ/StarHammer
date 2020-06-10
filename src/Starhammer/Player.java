package Starhammer;

public class Player {
	
	private int minerals;
	private int gold;
	private int supply;
	private int team;
	private boolean winner;
	
	public Player( int team ) {
		this.team = team;
		this.winner = false;
	}

	public int getMinerals() {
		return minerals;
	}

	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}
	
	public void addMinerals(int minerals) {
		this.minerals = this.minerals + minerals;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	 public void addGold(int gold) {
		 this.gold = this.gold + gold;
	 }

	public int getSupply() {
		return supply;
	}

	public void setSupply(int supply) {
		this.supply = supply;
	}

	public int getTeam() {
		return team;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean won) {
		this.winner = won;
	}
	
	
}

