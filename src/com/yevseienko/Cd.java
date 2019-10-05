package com.yevseienko;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cd implements Command {
	private static Cd cd;

	private Cd() {
	}

	public static Cd get() {
		if (cd == null) {
			cd = new Cd();
		}
		return cd;
	}

	@Override
	public Result execute(String... args) {
		if (args.length > 0) {
			String pathString = args[0];
			Path newPath = null;
			try {
				if (pathString.contains(":" + File.separator) || pathString.equals("/") || pathString.equals("\\")) {
					// :\ - есть указание буквы диска
					// \ или / переносят в корень
					newPath = Paths.get(pathString);
				} else {
					newPath = Paths.get(ConsolePath.get().toString(), pathString);
				}
				if (!newPath.toFile().exists()) {
					throw new InvalidPathException(newPath.toString(), "Файл или папка не найдены.");
				}
				ConsolePath.move(newPath);
			} catch (InvalidPathException ex) {
				return new Result(true, "Системе не удается найти указанный путь.");
			} catch (Exception c) {
				c.printStackTrace();
				return new Result(true, "Откзано в доступе.");
			}

		}
		return new Result(false, null);
	}
}
