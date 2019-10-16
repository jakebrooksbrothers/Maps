package edu.sdsu.cs;
import edu.sdsu.cs.datastructures.DirectedGraph;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
public class App {
    private static ArrayList<String> myList = new ArrayList<String>();
    private static Path thePath;
    private App(String args) {
        thePath = Paths.get(args);
    }
    public static void main(String[] args) {
        DirectedGraph<String> graph = new DirectedGraph<String>();
        Scanner scanner = new Scanner(System. in);
        String fileName = "layout.csv";
        File file = new File(fileName);
        if (!directoryExists(thePath) && !fileExists(file))
            System.out.println("Unable to open " + fileName + " Verify the file "
                    + "exists, is accessible, and meets the syntax requirements.");
        else {
            ArrayList<String> graphVals;
            if (directoryExists(thePath)) {
                System.out.println("dir exists");
                graphVals = readFile(thePath);
            } else {
                System.out.println("only file exists");
                Path p = Paths.get(fileName);
                graphVals = readFile(p);
            }
            for (String s : graphVals) {
                String[] tokens = s.split(",");
                if (tokens.length == 1)
                    graph.add(tokens[0]);
                else if (tokens.length == 2)
                    graph.connect(tokens[0], tokens[1]);
            }
            graph.toString();
            System.out.println("Enter starting vertex");
            String start = scanner.nextLine();
            System.out.println("Enter ending vertex");
            String end = scanner.nextLine();
            printShortestPath(graph, start, end);
        }
        System.out.println("Done");
    }
    private static ArrayList<String> readFile(Path filePathIn) {
        try {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(filePathIn, Charset.defaultCharset());
            return lines;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
    public static boolean fileExists(File file) {
        if (file == null)
            return false;
        return file.exists();
    }
    public static boolean directoryExists(Path path) {
        if (path == null)
            return false;
        return Files.exists(path);
    }
    public static void printShortestPath(DirectedGraph<String> graph, String start, String end){
        System.out.println(graph.shortestPath(start, end));
        System.out.println(graph.shortestPath(start, end).size());
    }
}


