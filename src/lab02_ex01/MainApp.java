package lab02_ex01;

import lab02.PrintableList;

public class MainApp {

    public static void main(String[] args) {

        PrintableList<String> s1 =
                new PrintableList<String>("ert", "try");

        s1.print();

        PrintableList<Integer> i1 =
                new PrintableList<Integer>(45, 86);

        i1.print();
    }
}