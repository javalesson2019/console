package com.yevseienko;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Dir dir = new Dir("dir");
        Cd cd = new Cd("cd");
        while (true) {
            System.out.printf("%s>", ConsolePath.get().toString());
            String line = sc.nextLine();

            if (line.startsWith("dir")) {
                dir.execute();
            }
            else if(line.startsWith("cd")){
                //cd.execute(line.);
            }

        }

    }
}

/*enum Commands {
    Dir(new Dir());

    Command command;

    Commands(Command command) {
        this.command = command;
    }
}*/


abstract class Command {
    private String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract public Result execute(String... args);
}

class Result {
    private boolean print;
    private String reply;

    public Result(boolean print, String reply) {
        this.print = print;
        this.reply = reply;
    }

    public boolean isPrint() {
        return print;
    }

    public String getReply() {
        return reply;
    }
}

class ConsolePath {
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

    public static void move(Path newPath) {
        cPath.path = newPath;
    }
}

class Dir extends Command {
    public Dir(String name) {
        super(name);
    }

    @Override
    public Result execute(String... args) {
        StringBuilder result = new StringBuilder();
        File myFolder = new File(ConsolePath.get().toString());
        File[] files = myFolder.listFiles();
        Arrays.stream(files).forEach(f -> result.append(f.getName()).append(" "));
        return new Result(true, result.toString());
    }
}

class Cd extends Command {
    public Cd(String name) {
        super(name);
    }

    @Override
    public Result execute(String... args) {
        if (args.length > 0) {
            String pathString = args[0];
            Path newPath = null;
            boolean isAbsolute = false;

            try {
                if (pathString.contains(":" + File.pathSeparator) || pathString.equals(File.pathSeparator)) {
                    // :\ - есть указание буквы диска
                    isAbsolute = true;
                    newPath = Paths.get(pathString).normalize();
                } else {
                    newPath = Paths.get(ConsolePath.get().toString(), pathString).normalize();
                }
                if (!newPath.toFile().exists()) {
                    return new Result(true, "Файл не найден.");
                }
            } catch (InvalidPathException ex) {
                return new Result(true, "Системе не удается найти указанный путь.");
            } catch (Exception c) {
                return new Result(true, "Откзано в доступе.");
            }
            ConsolePath.move(newPath);
        }
        return new Result(false, null);
    }
}
