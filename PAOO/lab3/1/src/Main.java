class Complex{
    private double real;
    private double imag;

    Complex(){
        this.real = 0;
        this.imag = 0;
    }

    Complex(double real, double imag){
        this.real = real;
        this.imag = imag;
    }

    public void print(){
        System.out.println(this.real + "+" + this.imag + "i\n");
    }

    public Complex sum(Complex c){
        Complex res = new Complex();

        res.real = this.real + c.real;
        res.imag = this.imag + c.imag;

        return res;
    }

    public Complex mul(Complex c){
        Complex res = new Complex();

        res.real = this.real * c.real;
        res.imag = this.imag * c.imag;

        return res;
    }

    public Complex pow(int power){

        Complex res = new Complex(1,1);
        res = this;

        for(int i=1; i < power; i++){
                res = res.mul(this);
        }
        return res;
    }

    public String toString(){
        return "(" + real + "," + imag + ")";
    }

    public boolean Equals(Complex c){
        return this.real == c.real && this.imag == c.imag;
    }
}

public class Main {
    public static void main(String[] args) {

        Complex a = new Complex(2,3);
        Complex b = new Complex(4,5);
        Complex c = new Complex();

        b = b.sum(a);
        b.print();

        c = c.mul(b);
        c.print();

        b = b.mul(a);
        b.print();

        a = a.pow(3);
        a.print();

        System.out.println(a.Equals(c) ? "equal" : "not equal");

        b.print();
        System.out.println(b.toString());

    }
}