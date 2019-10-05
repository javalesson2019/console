package com.yevseienko;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {
	public static void main(String[] args) {
		final Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File("config.ini")));
		} catch (IOException e) {
			System.out.println("Не удалось найти конфиг");
			return;
		}

		final Scanner sc = new Scanner(System.in);
		final String doNotFindCommandReply = "не является внутренней или внешней командой, исполняемой программой или пакетным файлом.";
		final ArrayList<String> arguments = new ArrayList<>();
		final String regex = "(\".+\"|\\S+)";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Result result = null;
		while (true) {
			arguments.clear();
			System.out.printf("%s>", ConsolePath.get().toString());

			final String line = sc.nextLine();
			final Matcher matcher = pattern.matcher(line);

			if (matcher.find()) {
				String match = matcher.group(0).toLowerCase();

				if(match.equalsIgnoreCase("exit")){
					break;
				}

				if (props.containsKey(match)) {
					String className = props.getProperty(match);
					Class propClass = null;
					try {
						propClass = Class.forName(className);
					} catch (ClassNotFoundException e) {
						result = new Result(true, String.format("\"%s\" %s", match, doNotFindCommandReply));
					}
					if (propClass != null) {
						try {
							Command command = (Command) propClass.getDeclaredMethod("get").invoke(null);
							findArgs(matcher, arguments);
							result = command.execute(arguments.toArray(String[]::new));
						} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				} else {
					result = new Result(true, String.format("\"%s\" %s", match, doNotFindCommandReply));
				}

				if (Objects.requireNonNull(result).isPrint()) {
					System.out.println(result.getReply());
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