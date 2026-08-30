package lab02_ex02;

import java.util.Arrays;
import java.util.List;

import lab02_ex02.util.NumberBox;

public class MainApp {

    public static void main(String[] args) {

        NumberBox<Double> nb1 = new NumberBox(45.2);
        NumberBox<Integer> nb2 = new NumberBox(42);
        NumberBox<Double> nb3 = new NumberBox(55.6);

        System.out.printf("%s%n", nb1.add(nb2));

        List<NumberBox<Double>> lst = Arrays.asList(nb1, nb3);

        System.out.printf("%s%n", NumberBox.sum(lst));
    }
}