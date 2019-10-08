package com.yevseienko.commands;

import com.yevseienko.Console;
import com.yevseienko.ConsolePath;
import com.yevseienko.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Dir implements Command {
	private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy  HH:mm"); // TODO: м.б. final
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
		File[] files = ConsolePath.get().toFile().listFiles(); // TODO: Files.list()
		for (File file : Objects.requireNonNull(files)) {
			try {
				BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				result.append(df.format(attr.lastModifiedTime().toMillis())).append("    ");
				result.append(attr.isDirectory() ? "<DIR>  " : "       ");
				if (!attr.isDirectory()) {
					String separator = "\t\t\t";
					if (attr.size() > 999) { // TODO: Магические числа?
						separator = separator.substring(1);
					}
					if (attr.size() > 9_999_999) { // TODO: Магические числа?
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
		return new Result(true, result.toString());
	}
}
