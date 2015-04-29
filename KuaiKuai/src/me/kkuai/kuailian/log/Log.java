package me.kkuai.kuailian.log;

public interface Log {
	
	public void verbose(String message, Throwable t);
	
	public void verbose(String message);

	public void debug(String message, Throwable t);
	
	public void debug(String message);
	
	public void info(String message, Throwable t);
	
	public void info(String message);
	
	public void warn(String message, Throwable t);
	
	public void warn(String message);
	
	public void error(String message, Throwable t);
	
	public void error(String message);
}
