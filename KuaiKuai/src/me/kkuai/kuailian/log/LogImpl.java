package me.kkuai.kuailian.log;

public class LogImpl implements Log {
	
	private String name;
	
	public LogImpl(String name) {
		this.name = name;
	}

	@Override
	public void verbose(String message, Throwable t) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.v(name, message, t);
		}
	}

	@Override
	public void verbose(String message) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.v(name, message);
		}
	}

	@Override
	public void debug(String message, Throwable t) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.d(name, message, t);
		}
	}

	@Override
	public void debug(String message) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.d(name, message);
		}
	}

	@Override
	public void info(String message, Throwable t) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.i(name, message, t);
		}
	}

	@Override
	public void info(String message) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.i(name, message);
		}
	}

	@Override
	public void warn(String message, Throwable t) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.w(name, message, t);
		}
	}

	@Override
	public void warn(String message) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.w(name, message);
		}
	}

	@Override
	public void error(String message, Throwable t) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.e(name, message, t);
		}
	}

	@Override
	public void error(String message) {
		if (LogFactory.isDebugEnable) {
			android.util.Log.e(name, message);
		}
	}

}
