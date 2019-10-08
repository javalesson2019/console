package com.yevseienko;

import com.yevseienko.commands.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {
	public static void main(String[] args) {
		final String configPath = "config.ini";
		final Properties props = new Properties();
		final HashMap<String, Command> commands = new HashMap<>();
		try {
			props.load(new FileInputStream(new File(configPath)));
			List<String> configLines = Files.readAllLines(Paths.get(configPath));
			for (String line : configLines) {
				String command = line.substring(0, line.indexOf('='));
				String className = props.getProperty(command);
				Class propClass = Class.forName(className);
				commands.put(command, (Command) propClass.getDeclaredMethod("get").invoke(null));
			}
		} catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			System.out.println("Не удалось найти конфиг или конфиг содержит ошибки");
			return;
		}

		final Scanner sc = new Scanner(System.in);
		final String doNotFindCommandReply = "не является внутренней или внешней командой, исполняемой программой или пакетным файлом.";
		final ArrayList<String> arguments = new ArrayList<>();
		final String regex = "(\".+\"|\\S+)";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Result result = null;
		while (true) {
			if (result != null && result.isPrint()) {
				System.out.println(result.getReply());
			}
			arguments.clear();
			System.out.printf("%s>", ConsolePath.get().toString());

			final String line = sc.nextLine();
			final Matcher matcher = pattern.matcher(line);

			if (matcher.find()) {
				String command = matcher.group(0).toLowerCase();
				if (command.equals("exit")) {
					break;
				}
				if (props.containsKey(command)) {
					if (!commands.containsKey(command)) {
						result = new Result(true, String.format("\"%s\" %s", command, doNotFindCommandReply));
						continue;
					}
					findArgs(matcher, arguments);
					result = commands.get(command).execute(arguments.toArray(String[]::new));
				} else {
					result = new Result(true, String.format("\"%s\" %s", command, doNotFindCommandReply));
				}
			}
		}
	}

	private static void findArgs(Matcher matcher, Collection<String> arguments) {
		while (matcher.find()) {
			arguments.add(matcher.group(0).replace("\"", ""));
		}
	}
}

