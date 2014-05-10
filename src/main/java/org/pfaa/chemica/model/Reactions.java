package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reactions {
	private static final Reactions instance = new Reactions();
	
	private List<Reaction> reactions = new ArrayList<Reaction>();
	
	public static Reactions getInstance() {
		return instance;
	}
	
	private Reactions() {
	}
	
	public void addReaction(Reaction reaction) {
		reactions.add(reaction);
	}
	
	public void addReactions(Reaction... reactions) {
		Collections.addAll(this.reactions, reactions);
	}
	
	static {
		Reactions.getInstance().addReactions();
	}
}
