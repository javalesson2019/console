package com.yevseienko;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class Find implements Command, Runnable {
	private static Find find;
	private static File[] disks;
	private String findMe;
	private boolean isInterrupted;
	private static final Object locker = new Object();
	private StringBuilder result;

	private Find() {
	}

	static {
		disks = File.listRoots();
	}

	public void interrupt(){
		isInterrupted = true;
	}

	public static Find get() {
		if (find == null) {
			find = new Find();
		}
		return find;
	}

	@Override
	public Result execute(String... args) {
		if(args.length > 0)
		{
			findMe = args[0];
			boolean async = Arrays.stream(args).anyMatch(s -> s.equals(ConsolePath.ASYNC_ARGUMENT));
			result = new StringBuilder();
			if(async){
				ConsolePath.EXECUTOR.execute(this);
				return new Result(false, null);
			}
			else{
				System.out.println("Начался поиск файла..");
				findFile();
				return new Result(true, result.toString());
			}
		}
		else{
			return new Result(true, "Укажите файл который нужно искать.");
		}
	}

	private void findFile() {
		for (File disk : disks) {
			try {
				Files.walkFileTree(disk.toPath(), new SimpleFileVisitor<>() {
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						if (isInterrupted) {
							return FileVisitResult.TERMINATE;
						}
						if (!dir.toRealPath().toString().equals(dir.toString())) {
							return FileVisitResult.SKIP_SUBTREE;
							// избегаю рекурсивных ссылок
						}
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						if (isInterrupted) {
							return FileVisitResult.TERMINATE;
						}
						//todo check
						if(file.getFileName().toString().contains(findMe)){
							synchronized (locker){
								result.append(file.toRealPath().toString()).append("\r\n");
							}
						}
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				System.out.println("Нет доступа к диску");
			}
		}
	}
	// result.append(file.toRealPath().toString()).append("\r\n");

	@Override
	public void run() {
		findFile();
	}
}
