package Objects;

public enum ID {

	Void( 0, 0, 0 ),
	Terrain( 0, 0, 0 ),
	Minerals( 0, 0, 0 ),
	Gold( 0, 0, 0 ),
	Nexus( 1, 1000, 0 ),
	Barracks( 1, 400, 0 ),
	Marine( 2, 350, 100 ),
	Worker( 2, 200, 0 ),
	Camera( 3, 0, 0 );
	
	private int type;	/* 0 = terrain
						* 1 = building
						* 2 = unit
						* 3 = others
						*/
	private int mineralCost;
	private int goldCost;
	
	private ID( int type, int minCost, int golCost ) {
		this.type = type;
		this.mineralCost = minCost;
		this.goldCost = golCost;
	}
	
	public boolean isBuilding() {
		if( type == 1 )
			return true;
		else
			return false;
	}
	
	public boolean isUnit() {
		if( type == 2 ) 
			return true;
		else
			return false;
	}

	public int getMineralCost() {
		return mineralCost;
	}

	public void setMineralCost(int mineralCost) {
		this.mineralCost = mineralCost;
	}

	public int getGoldCost() {
		return goldCost;
	}

	public void setGoldCost(int goldCost) {
		this.goldCost = goldCost;
	}
	
}
