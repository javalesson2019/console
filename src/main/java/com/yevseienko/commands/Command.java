package com.yevseienko.commands;

import com.yevseienko.Result;

public interface Command {
	Result execute(String... args);
}
