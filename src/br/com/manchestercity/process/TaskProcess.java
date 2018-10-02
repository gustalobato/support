
package br.com.manchestercity.process;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class TaskProcess {

	private Timer timer;
	private TimerTask task;

	private int hour;
	private int minute;
	private int second;
	private int millisecondsToRepeat;

	public TaskProcess(int hour, int minute, int second, int millisecondsToRepeat, TimerTask task) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millisecondsToRepeat = millisecondsToRepeat;
		this.task = task;
	}

	public void start() {
		timer = new Timer();
		timer.schedule(this.task, getNextDate(this.hour, this.minute, this.second), this.millisecondsToRepeat);
	}

	private Date getNextDate(int hour, int minute, int second) {

		GregorianCalendar currentDate = new GregorianCalendar();
		GregorianCalendar nextTime = new GregorianCalendar(0, 0, 0, hour, minute, second);

		nextTime.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH));
		nextTime.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
		nextTime.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));

		if (nextTime.getTimeInMillis() < currentDate.getTimeInMillis()) {
			nextTime.add(Calendar.DAY_OF_MONTH, 1);
		}

		return nextTime.getTime();
	}
}
