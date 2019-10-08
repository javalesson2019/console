package com.yevseienko;

public class Result {
	private boolean print; // TODO: м.б. final
	private String reply; // TODO: м.б. final

	public Result(boolean print, String reply) {
		this.print = print;
		this.reply = reply;
	}

	public boolean isPrint() {
		return print;
	}

	public String getReply() {
		return reply;
	}
}
