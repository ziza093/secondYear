class Stack{
    private int varf;
    private int[] nr;

    Stack(){
        this.varf = -1;
        this.nr = null;
    }

    Stack(int size){
        this.varf = -1;
        this.nr = new int[size];
    }

    void push(int nr){
        try {
            this.varf++;
            this.nr[this.varf] = nr;
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Stack overflow");
        }
    }

    int pop(){

        try {
            int nr = this.nr[this.varf];
            this.varf--;
            return nr;

        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Stack underflow");
        }
        return -1;
    }

    boolean isEmpty(){
        return this.varf == -1;
    }
}

public class Main {
    public static void main(String[] args) {

        Stack stack = new Stack(6);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(20);
        System.out.println(stack.isEmpty());
//        int a = stack.pop();
        System.out.println(stack.pop());
    }
}