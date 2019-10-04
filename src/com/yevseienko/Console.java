package com.yevseienko;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {
	public static void main(String[] args) {
	    // test implementation
		final Scanner sc = new Scanner(System.in);
		final Dir dir = new Dir();
        final Cd cd = new Cd();
        final Pwd pwd = new Pwd();
        final Cat cat = new Cat();

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
				String match = matcher.group(0);
				if (match.equalsIgnoreCase("exit")) {
					break;
				} else if (match.equalsIgnoreCase("dir")) {
					result = dir.execute();
				} else if (matcher.group(0).equalsIgnoreCase("cd")) {
					findArgs(matcher, arguments);
					result = cd.execute(arguments.toArray(String[]::new));
				} else if (match.equalsIgnoreCase("pwd")) {
					result = pwd.execute();
				} else if (match.equalsIgnoreCase("cat")) {
					findArgs(matcher, arguments);
					result = cat.execute(arguments.toArray(String[]::new));
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