package log_aggregation;

import java.time.LocalDateTime;

public class Access {

	private int LessThan500ms;
	private int LessThan2000ms;
	private int MoreThan2001ms;
	private LocalDateTime every5minuts;

	@Override
	public String toString() {
		return "時刻" + this.getEvery5minuts() + "," + LessThan500ms +"," + LessThan2000ms +"," + MoreThan2001ms;
	}

	public int getLessThan500ms() {
		return LessThan500ms;
	}
	public void setLessThan500ms(int lessThan500ms) {
		LessThan500ms = lessThan500ms;
	}
	public int getLessThan2000ms() {
		return LessThan2000ms;
	}
	public void setLessThan2000ms(int lessThan2000ms) {
		LessThan2000ms = lessThan2000ms;
	}
	public int getMoreThan2001ms() {
		return MoreThan2001ms;
	}
	public void setMoreThan2001ms(int moreThan2001ms) {
		MoreThan2001ms = moreThan2001ms;
	}
	public LocalDateTime getEvery5minuts() {
		return every5minuts;
	}
	public void setEvery5minuts(LocalDateTime every5minuts) {
		this.every5minuts = every5minuts;
	}

}
