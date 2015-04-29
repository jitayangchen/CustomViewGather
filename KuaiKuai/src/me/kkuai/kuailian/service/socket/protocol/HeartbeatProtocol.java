package me.kkuai.kuailian.service.socket.protocol;


/**
 * heartBeat listen
 * @author eyeshot
 *
 */
public class HeartbeatProtocol implements Protocol{


	public String getCmd() {
		return "1";
	}

	public void parseBinary(byte[] data) {
		
	}

	public byte[] getContentData() {
	//	String heartbeatStr = "{\"b\":14,\"data\":\"hello\"}";
		String heartbeatStr = "h"; //define heartbeat str
		return heartbeatStr.getBytes();
	}

	@Override
	public String getFunction() {
		return "heartbeat";
	}

}
