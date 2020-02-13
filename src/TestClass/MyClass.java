package TestClass;

public class MyClass implements MyInterface {
    String initString;

    public MyClass(String stringToPrint) {
        this.initString = stringToPrint;
    }

    public MyClass() {
        this.initString = "default";
    }

    public void print() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(initString);
        Thread.sleep(100);
    }

    public void print(String s) throws InterruptedException {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            Thread.sleep(100);
            System.out.print(c);
        }
        System.out.println("");
    }
}