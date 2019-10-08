package com.yevseienko.commands;

import com.yevseienko.ConsolePath;
import com.yevseienko.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public class Dir implements Command {
	private final static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
	private static Dir dir;

	private Dir() {
	}

	public static Dir get() {
		if (dir == null) {
			dir = new Dir();
		}
		return dir;
	}

	@Override
	public Result execute(String... args) {
		StringBuilder result = new StringBuilder();
		File[] files;
		files = ConsolePath.get().toFile().listFiles(); // TODO: Files.list()
		if (files != null) {
			for (File file : files) {
				try {
					BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
					result.append(df.format(attr.lastModifiedTime().toMillis())).append("    ");
					result.append(attr.isDirectory() ? "<DIR>  " : "       ");
					if (!attr.isDirectory()) {
						String separator = "\t\t\t";

						// числа для форматирования вывода
						final int firstTab = 999;
						// если размер файла > 999 убираю одну табуляцию
						final int secondTab = 9_999_999;
						// если размер файла > 9 999 999 убираю вторую табуляцию

						if (attr.size() > firstTab) {
							separator = separator.substring(1);
						}
						if (attr.size() > secondTab) {
							separator = separator.substring(1);
						}
						result.append(attr.size()).append(separator);
					} else {
						result.append("\t\t\t");
					}
					result.append(file.getName()).append("\n");
				} catch (IOException ignore) {
				}
			}
		} else {
			result.append("Ошибка при получении списка файлов.");
		}
		return new Result(true, result.toString());
	}
}
