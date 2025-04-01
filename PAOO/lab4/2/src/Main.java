import java.util.*;

class SortedVector extends Vector<Object> {

    public void addElement(Object x){
        super.add(x);
        super.sort(null);
    }

    public void insertElementAt(Object x, int index){
        super.insertElementAt(x, index);
        super.sort(null);
    }
}


public class Main {
    public static void main(String[] args) {
        SortedVector s1 =new SortedVector();
        s1.add(3);
        s1.add(4);
        s1.add(2);
        s1.add(1);
        s1.addElement(-1);
        s1.insertElementAt(25, 3);
        System.out.println(s1);

    }
}