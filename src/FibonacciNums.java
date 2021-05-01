import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Scanner;

public class FibonacciNums {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);
        String message = "\nВведите число, равное тому, сколько чисел Фибоначчи хотите вывести: ";

        System.out.println("Чтобы выйти, напечатайте \"выход\"");
        System.out.println(message);

        String input = scanner.nextLine();

        while (!checkNumType(input)) {
            System.out.println(message);
            input = scanner.nextLine();
        }
    }

    private static boolean checkNumType(String input) {
        boolean exit = false;
        if (!input.matches("\\d+")) {
            checkExit(input);
            System.out.println("Вводить можно только целые положительные числа!");
        }else exit = checkNumMaxValue(input);
        return exit;
    }

    private static boolean checkNumMaxValue(String input) {
        //For 93 Fibonacci sequence member of Long type goes beyond Long max value
        if (Integer.parseInt(input) > 93) {
            System.out.println("Введеное число выходит за допустимое значения последнего члена.");
            return false;
        }else{
            showFibonacciSequence(input);
            return true;
        }
    }

    private static void checkExit(String input){
        if((input).equalsIgnoreCase("выход")){
            System.exit(0);
        }
    }

    private static void showFibonacciSequence(String input){
        ArrayList<Long> fibNums = new ArrayList<>();
        fibNums.add((long) 0);
        fibNums.add((long) 1);
        for (int i = 2; i < Integer.parseInt(input); i++) {
            fibNums.add(fibNums.get(i - 1) + fibNums.get(i - 2));
        }
        System.out.println(fibNums.toString());
    }
}