package com.yevseienko;

import java.io.FileReader;
import java.io.IOException;

public class Cat implements Command {
	@Override
	public Result execute(String... args) {
		if (args.length > 0) {
			String pathString = args[0];
			StringBuilder stringBuilder = new StringBuilder();
			try (FileReader reader = new FileReader(pathString)) {
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
