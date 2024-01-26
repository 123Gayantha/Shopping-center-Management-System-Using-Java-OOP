package input;
// define a contract for validatiing user input
public interface Validate<T>{
    boolean validate(T input);
}
