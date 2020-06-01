package Objects;

public enum ID {

	Void( 0 ),
	Terrain( 0 ),
	Minerals( 0 ),
	Gold( 0 ),
	Nexus( 1 ),
	Marine( 2 ),
	Worker( 2 ),
	Camera( 3 );
	
	private int type;  /* 0 = terrain
						* 1 = building
						* 2 = unit
						* 3 = others
						*/
	
	private ID( int type ) {
		this.type = type;
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
	
}
