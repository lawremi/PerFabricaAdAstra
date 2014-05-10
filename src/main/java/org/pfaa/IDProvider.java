package org.pfaa;


/* So this gives out (default) IDs automatically. One might worry about 
 * introducing sequential coupling here, but the fact is that Configuration dynamically 
 * assigns defaults anyway upon conflict. Moreover, the worse coupling is that different
 * modules can so easily come into conflict in the first place.
 * */

public interface IDProvider {
	public int nextTerrainBlockID(String name);
	public int nextBlockID(String name);
	public int nextItemID(String name);
}