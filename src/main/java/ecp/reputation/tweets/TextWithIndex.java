package ecp.reputation.tweets;

public class TextWithIndex implements Comparable<TextWithIndex> {
	public String text;
	public int index;
	public int groupIndex;

	public TextWithIndex(String txt, int idx, int gidx) {
		this.text = txt;
		this.index = idx;
		this.groupIndex = gidx;
	}

	public int compareTo(TextWithIndex txt) {

		int compareQuantity = ((TextWithIndex) txt).index;

		// ascending order
		return this.index - compareQuantity;

		// descending order
		// return compareQuantity - this.quantity;

	}
}
