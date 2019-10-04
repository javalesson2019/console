package com.yevseienko;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConsolePath {
	private static ConsolePath cPath;
	private Path path;

	private ConsolePath() {
		try {
			path = Paths.get(".").toRealPath(LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Path get() {
		if (cPath == null) {
			cPath = new ConsolePath();
		}
		return cPath.path;
	}

	public static void move(Path newPath) throws IOException {
		cPath.path = newPath.normalize().toRealPath();
	}
}
