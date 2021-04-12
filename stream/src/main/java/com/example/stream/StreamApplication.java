package com.example.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootApplication
public class StreamApplication {

    public static void main(String[] args) throws IOException {

        // 1. Integer Stream
        IntStream
                .range(1, 10)
                .forEach(System.out::print); // 123456789
        System.out.println();

        // 2. Integer Stream with skip
        IntStream
                .range(1, 10)
                .skip(5)
                .forEach(x -> System.out.print(x)); // 6789
        System.out.println();

        // 3. Integer Stream with skip with sum
        System.out.println(
            IntStream
            .range(1, 5)
            .sum()
        ); // 10

        // 4. Stream.of, sorted and findFirst
        Stream.of("Ava", "Aneri", "Alberto")
            .sorted()
            .findFirst()
            .ifPresent(System.out::println); // Alberto

        // 5. Stream from Array, sort, filter and print
        String[] names = {"Al", "Ankit", "Kushal", "Brent", "Sarika", "amanda", "Hans", "Shivika", "samanda"};
        Arrays.stream(names)
            .filter(x -> x.startsWith("S"))
            .sorted()
            .forEach(System.out::println); // Sarika, Shivika

        // 6. average of squares of an int array
        Arrays.stream(new int[] {2, 4, 6, 8, 10})
            .map(x -> x * x) // 4, 16, 36, 64, 100
            .average() // 4 + 16 + 36 + 64 + 100 = 220/5 = 44
            .ifPresent(System.out::println); // 44.0

        // 7. Stream from List, filter and print
        List<String> people = Arrays.asList("Al", "Ankit", "Kushal", "Brent", "Sarika", "amanda", "Hans", "Shivika", "samanda");
        people
            .stream()
            .map(String::toLowerCase)
            .filter(x -> x.startsWith("a"))
            .forEach(System.out::println); // al, ankit, amanda

        // 8. Stream rows from text file, sort, filter, and print
        Stream<String> bands = Files.lines(Paths.get(System.getProperty("user.dir") + "\\stream\\src\\main\\resources\\bands.txt"));
        bands
            .sorted()
            .filter(x -> x.length() > 33)
            .forEach(System.out::println); // Grandmaster Flash and the Furious Five
        bands.close();

        // 9. Stream rows from text file and save to List
        List<String> bands2 = Files.lines(Paths.get(System.getProperty("user.dir") + "\\stream\\src\\main\\resources\\bands.txt"))
            .filter(x -> x.contains("Rage"))
            .collect(Collectors.toList());
        bands2.forEach(x -> System.out.println(x)); // Rage Against the Machine

        // 10. Stream rows from CSV file and count
        Stream<String> rows1 = Files.lines(Paths.get(System.getProperty("user.dir") + "\\stream\\src\\main\\resources\\data.txt"));
        int rowCount = (int) rows1
                .map(x -> x.split(","))
                .filter(x -> x.length == 3)
                .count();
        System.out.println(rowCount + " rows."); // 5 rows.
        rows1.close();


        SpringApplication.run(StreamApplication.class, args);

    }

}
