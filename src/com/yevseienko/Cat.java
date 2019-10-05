package com.yevseienko;

import java.io.FileReader;
import java.io.IOException;
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
		if (args.length > 0) {
			String pathString = args[0];
			StringBuilder stringBuilder = new StringBuilder();
			try (FileReader reader = new FileReader(Paths.get(ConsolePath.get().toString(), pathString).toString())) {
				char[] buffer = new char[1024];
				while (reader.read(buffer) != -1) {
					stringBuilder.append(buffer);
				}
				return new Result(true, stringBuilder.toString());
			} catch (IOException ex) {
				return new Result(true, "Не удалось прочитать файл.");
			}
		}
		return new Result(true, "Использование: cat <filename>");
	}
}
