package org.pfaa.chemica.processing;

public class CompoundForm implements Form {

	private Form first, second;
	
	public CompoundForm(Form first, Form second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public String name() {
		return first + "_" + second;
	}
	
	@Override
	public int getNumberPerBlock() {
		return this.first.getNumberPerBlock();
	}

	@Override
	public Form stack() {
		Form stacked = this.first.stack();
		if (stacked != null) {
			stacked = stacked.of(this.second);
		}
		return stacked;
	}

	@Override
	public Form unstack() {
		Form unstacked = this.first.unstack();
		if (unstacked != null) {
			unstacked = unstacked.of(this.second);
		}
		return unstacked;
	}

	@Override
	public Form communite() {
		return this.second.communite();
	}

	@Override
	public Form reduce() {
		return this.second.reduce();
	}

	@Override
	public Form compact() {
		Form compacted = this.second.compact();
		if (compacted != null)
			compacted = this.first.of(compacted);
		return compacted;
	}

	@Override
	public Form dry() {
		Form dried = this.second.dry();
		if (dried != null)
			dried = this.first.of(dried);
		return dried;
	}
}
