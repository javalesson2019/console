package com.yevseienko.commands;

import com.yevseienko.ConsolePath;
import com.yevseienko.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Cat implements Command {
    private static Cat cat;

    private Cat() {
    }

    public static Cat get() {
        if (cat == null) {
            cat = new Cat();
        }
        return cat;
    }

    @Override
    public Result execute(String... args) {
        // TODO: Небольшой рефакторинг кода
        if (args.length == 0) {
            return new Result(true, "Использование: cat <filename>");
        }
        try {
            return new Result(true, Files.readString(Paths.get(ConsolePath.get().toString(), args[0])));
        } catch (IOException ex) {
            return new Result(true, "Не удалось прочитать файл.");
        }
    }
}

