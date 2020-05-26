package Starhammer;

import Objects.ID;

public class MapVertex {
	public int x;
 	public int y;
 	public ID id;
 	public int cost;
 	public int g_score;
    public int h_score;
    public int f_score;
    public MapVertex prev;
}
