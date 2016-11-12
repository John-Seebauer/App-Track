package edu.illinois.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by John Seebauer (seebaue2) on 11/11/16.
 */
public class LogFormatter extends Formatter {
	private DateFormat date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
	
	@Override
	public String format(LogRecord logRecord) {
		StringBuilder builder = new StringBuilder(1000)
				.append('[').append(logRecord.getLevel()).append(']').append('\t')
				.append(date.format(new Date(logRecord.getMillis()))).append('\t')
				.append('[').append(logRecord.getSourceClassName()).append("::").append(logRecord.getSourceMethodName()).append(']')
				.append('\t').append(logRecord.getMessage()).append('\n');
		if(logRecord.getThrown() != null) {
			builder.append(logRecord.getThrown().getLocalizedMessage()).append('\n');
			for(StackTraceElement elem : logRecord.getThrown().getStackTrace()) {
				builder.append('\t').append(elem.toString()).append('\n');
			}
		}
		return builder.toString();
	}
}
