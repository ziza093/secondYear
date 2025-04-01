// fisierul AbstractMotoare.java
public abstract class AbstractMotoare {
    public abstract void operatiaM1();
    public abstract void operatieM2();
}

class Motoare1 extends AbstractMotoare{

    String motor;

    Motoare1(String arg)
    {System.out.println("Model motor1: "+arg); motor = arg;}
    // Implementare cod pentru:
    public void operatiaM1() { System.out.println("Model motor1: " + motor); };
    public void operatieM2() { };
}
class Motoare2 extends AbstractMotoare{

    String motor;

    Motoare2(String arg)
    {System.out.println("Model motor2: "+arg); motor = arg;}
    // Implementare cod pentru:
    public void operatiaM1() { System.out.println("Model motor2: " + motor);};
    public void operatieM2() { };
}