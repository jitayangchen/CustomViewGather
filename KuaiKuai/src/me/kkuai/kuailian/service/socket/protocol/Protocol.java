package me.kkuai.kuailian.service.socket.protocol;

public interface Protocol
{
	/**
	 * get command code
	 * @return
	 */
	public String getCmd();
	
	/**
	 * get command function
	 * @return
	 */
	public String getFunction();
	
	/**
	 * get content data in binary
	 * @return
	 */
	public byte[] getContentData();
	
	/**
	 * parse binary
	 */
	public void parseBinary(byte[] data);
}
