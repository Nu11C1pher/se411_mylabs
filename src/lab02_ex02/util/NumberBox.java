package lab02_ex02.util;

import java.util.List;

public class NumberBox<T extends Number> {

    private T element;

    private NumberBox() {
        // Hides the default empty constructor
    }

    public NumberBox(T data) {
        element = data;
    }

    public double add(NumberBox<?> other) {
        return element.doubleValue() + other.getElement().doubleValue();
    }

    public static double sum(List<NumberBox<Double>> lst)
            throws IllegalArgumentException {

        if (lst == null)
            throw new IllegalArgumentException("List is null");

        double sum = 0;

        for (NumberBox n : lst) {
            sum += n.getElement().doubleValue();
        }

        return sum;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
}