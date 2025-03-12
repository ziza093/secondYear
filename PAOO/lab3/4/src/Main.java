import java.util.ArrayList;

class IntSet{
    private int[] vect;
    private int nr;

    IntSet(int size){
        int nr = -1;
        vect = new int[size];
    }

    void add(int i){
        for(int j=0;j<nr;j++){
            if(i==vect[j]){
                return;
            }
        }
        try{
            vect[nr++] = i;
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("ArrayIndexOutOfBoundsException");
        }
    }

    boolean contains(int i){
        for(int j=0;j<vect.length;j++){
            if(i==vect[j]){
                return true;
            }
        }
        return false;
    }

    String toStringInt(){
        String s = "";

        for(int j=0;j<vect.length;j++){
            s += vect[j] + " ";
        }

        return s;
    }
}

public class Main {
    public static void main(String[] args) {
        IntSet set = new IntSet(10);
        set.add(1);
        set.add(2);
        set.add(6);
        set.add(6);
        set.add(6);
        System.out.println(set.toStringInt());
        System.out.println(set.contains(2));
    }
}