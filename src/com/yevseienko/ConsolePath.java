package com.yevseienko;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsolePath {
	private static ConsolePath cPath;
	private Path path;
	public static String ASYNC_ARGUMENT = "&";
	public static ExecutorService EXECUTOR = Executors.newCachedThreadPool();

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
