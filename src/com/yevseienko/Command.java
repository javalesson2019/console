package com.yevseienko;

public interface Command {
    Result execute(String... args);
}
