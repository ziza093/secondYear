//fisierul AbstractCaroserie.java
abstract class AbstractCaroserie {
    public abstract void operatiaC1();
    public abstract void operatieC2();
}

class Caroserie1 extends AbstractCaroserie{

    String caroserie;

    Caroserie1(String arg)
    {System.out.println("Model caroserie1: "+arg); caroserie = arg;}
    // Implementare cod pentru:
    public void operatiaC1(){ System.out.println("Model caroserie1: " + caroserie); };
    public void operatieC2() { };
}
class Caroserie2 extends AbstractCaroserie{

    String caroserie;

    Caroserie2(String arg)
    {System.out.println("Model caroserie2: " + arg); caroserie = arg;}
    // Implementare cod pentru:
    public void operatiaC1() { System.out.println("Model caroserie2: " + caroserie);}
    public void operatieC2() { };
}