package lab02;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintableList<T> {

    private List<T> elements;

    public PrintableList() {
        elements = new ArrayList<T>();
    }

    public PrintableList(T... arr) {
        elements = Arrays.asList(arr);
    }

    public void print() {
        for (T e : elements) {
            System.out.printf("%s ", e);
        }
    }
}
