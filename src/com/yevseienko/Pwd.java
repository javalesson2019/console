package com.yevseienko;

public class Pwd implements Command {
	private static Pwd pwd;

	private Pwd() {
	}

	public static Pwd get() {
		if (pwd == null) {
			pwd = new Pwd();
		}
		return pwd;
	}

	@Override
	public Result execute(String... args) {
		return new Result(true, ConsolePath.get().toString());
	}
}
