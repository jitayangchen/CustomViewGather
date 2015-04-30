package com.cn.lyric.bean;

public class Music {

	private String title;//歌曲名字
	private String singer;//歌手
	private String album;//专辑名称
	private String url;//歌曲的地址
	private long size;//歌曲的大小
	private long time;//时长
	private long albumid;//专辑id
	private long songid;//歌曲id
	public long getSongid() {
		return songid;
	}

	public void setSongid(long songid) {
		this.songid = songid;
	}

	private String name;

	public String getName() {
		return name;
	}

	public long getAlbumid() {
		return albumid;
	}

	public void setAlbumid(long albumid) {
		this.albumid = albumid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
