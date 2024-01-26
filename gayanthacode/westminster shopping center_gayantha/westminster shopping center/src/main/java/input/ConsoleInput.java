package input;

import java.util.Scanner;

// dispose must be called to avoid resource leak
public class ConsoleInput<T> {
    private final Parse<T> converter;
    private final static Scanner scanner = new Scanner(System.in);
    public ConsoleInput(Parse<T> converter){
        this.converter = converter;
    }

    public T readUserInput(String prompt) {
        System.out.print(prompt);

        String input = scanner.nextLine();
        try {
            return converter.convert(input);
        } catch (Exception e) {
            System.out.println("Invalid input");
            return readUserInput(prompt);
        }
    }

    public T readUserInput(String prompt, Validate<T> validator, String invalidMessage) {
        T input = readUserInput(prompt);
        while (!validator.validate(input)) {
            System.out.println(invalidMessage);
            input = readUserInput(prompt);
        }

        return input;
    }

    public T readUserInput(String prompt, Validate<T> validator) {
        return readUserInput(prompt, validator, "Invalid input");
    }
}
