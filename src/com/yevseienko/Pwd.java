package com.yevseienko;

public class Pwd implements Command {
	@Override
	public Result execute(String... args) {
		return new Result(true, ConsolePath.get().toString());
	}
}
