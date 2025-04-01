import java.util.*;

class SetAsVector extends Vector<Object>{
    @Override
    public boolean remove(Object x){
        return super.remove(x);
    }

    public boolean add(Object x){
        if(super.contains(x))
            return false;
        else
            return super.add(x);
    }

    public boolean contains(Object x){
        return super.contains(x);
    }

    public String toString(){
        return super.toString();
    }
}

class SetAsVector2{
    Vector<Object> vector = new Vector<>();

    boolean remove(Object x){
        return vector.remove(x);
    }

    boolean add(Object x){
        if(vector.contains(x))
            return false;
        else
            return vector.add(x);
    }

    public boolean contains(Object x){
        return vector.contains(x);
    }

    public String toString(){
        return vector.toString();
    }
}


public class Main {
    public static void main(String[] args) {

         SetAsVector set1 = new SetAsVector();
         SetAsVector2 set2 = new SetAsVector2();

         set1.add(2);
         set1.add(3);
         set1.add(4);
         set1.add(4);
         set1.add(5);
         System.out.println(set1.toString());
         set1.remove((Object)2);
         System.out.println("Is the nr 4 insidie? " + set1.contains(4));
         System.out.println(set1.toString());

         System.out.println("\n");

         set2.add(2);
         set2.add(3);
         set2.add(4);
         set2.add(4);
         set2.add(5);
         System.out.println(set2.toString());
         set2.remove(2);
         System.out.println("Is the nr 4 insidie? " + set2.contains(4));
         System.out.println(set2.toString());

    }
}