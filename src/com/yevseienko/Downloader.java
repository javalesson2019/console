package com.yevseienko;

import java.net.URI;
import java.util.Arrays;

public class Downloader implements Command {
	private static Downloader downloader;

	private Downloader() {
	}

	public static Downloader get() {
		if (downloader == null) {
			downloader = new Downloader();
		}
		return downloader;
	}

	@Override
	public Result execute(String... args) {
		if (args.length > 0) {
			String path = args[0];
			boolean async = Arrays.stream(args).anyMatch(s -> s.equals(ConsolePath.ASYNC_ARGUMENT));
			Download download = new Download(URI.create(path), ConsolePath.get().toString());
			if (async) {
				ConsolePath.EXECUTOR.execute(download);
			} else {
				download.run();
			}
		}
		return new Result(true, "Загрузка завершена.");
	}
}