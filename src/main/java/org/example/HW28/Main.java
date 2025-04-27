package org.example.HW28;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    static ArrayList<Integer> numbers = new ArrayList<>();
    public static void main(String[] args) {

        getText();
        System.out.println( numbers.toString());

        getPairSum();

        multipleByTwo();

        getMax();

        getNotPairSequence();
        average();

    }
    public static void getText(){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter some text (type 'added' to quit): ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("added")) {
                break;
            }

            System.out.println("You entered: " + userInput);
            numbers.add(Integer.parseInt(userInput));
        }

        scanner.close();
    }
    public static void getPairSum(){
        int sum= numbers.stream()
                .filter(number -> number % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("Sum: " + sum);
    }
    public static void multipleByTwo(){
        ArrayList<Integer> numeros = numbers;
            numeros= numeros.stream()
                .map(number -> number * 2)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println(numeros);
    }
    public static void getMax(){
        ArrayList<Integer> numeros = numbers;
        numeros= numeros.stream()
                .max(Comparator.naturalOrder())
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Max is " + numeros);
    }
    public static void getNotPairSequence(){


            String oddNumbers = numbers.stream()
                    .filter(number -> number % 2 != 0)  // get odd numbers
                    .map(String::valueOf)               // convert each number to String
                    .collect(Collectors.joining(", ")); // join with comma and space

            System.out.println("Odd numbers: " + oddNumbers);


    }
    public static void average(){
        ArrayList<Integer> numeros = numbers;
      int sum= numeros.stream()
                .mapToInt(Integer::intValue)
                .sum();
      double average = (double) sum /numeros.size();
      System.out.println("Average is " + average);


    }
}
