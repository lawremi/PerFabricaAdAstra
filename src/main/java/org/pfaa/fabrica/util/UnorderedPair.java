package org.pfaa.fabrica.util;

public class UnorderedPair<T> {
	public final T a;
	public final T b;
	
	protected UnorderedPair(T a, T b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UnorderedPair)) {
			return false;
		}
		UnorderedPair<?> other = (UnorderedPair<?>)obj;
		return (memberEquals(this.a, other.a) && memberEquals(this.b, other.b)) || 
				(memberEquals(this.a, other.b) && memberEquals(this.b, other.a));  		
	}

	private boolean memberEquals(Object member1, Object member2) {
		return (member1 == null && member2 == null) || (member1 != null && member1.equals(member2));
	}
	
	@Override
	public int hashCode() {
		int hashA = this.a == null ? 0 : this.a.hashCode();
		int hashB = this.b == null ? 0 : this.b.hashCode();
		return Math.max(hashA, hashB) ^ Math.min(hashA, hashB);
	}

	@Override
	public String toString() {
		return "{" + this.a + "," + this.b + "}";
	}
	
	public static <T> UnorderedPair<T> of(T a, T b) {
		return new UnorderedPair<T>(a, b);
	}
}
