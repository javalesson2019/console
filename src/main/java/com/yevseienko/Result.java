package com.yevseienko;

public class Result {
	private final boolean print;
	private final String reply;

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
