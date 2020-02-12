package TestClass;

public class MyClass implements MyInterface  {
    String initString;

    public MyClass(String stringToPrint) {
        this.initString = stringToPrint;
    }
    public MyClass() {
        this.initString = "default";
    }

    public void print() throws InterruptedException {
        char[] chars = initString.toCharArray();
        for (char c : chars) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println("");
    }

    public void print(String s) throws InterruptedException {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println("");
    }
}