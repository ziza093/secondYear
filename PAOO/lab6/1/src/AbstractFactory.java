//fisierul AbstractFactory.java
abstract class AbstractFactory {
    abstract AbstractMotoare CreazaMotor();
    abstract AbstractCaroserie CreazaCaroserie();
}
class Fabrica1 extends AbstractFactory{
    AbstractMotoare CreazaMotor()
    {return new Motoare1("MotorBenzina ");}
    AbstractCaroserie CreazaCaroserie()
    {return new Caroserie1("Masinateren ");}
}
class Fabrica2 extends AbstractFactory {
    AbstractMotoare CreazaMotor()
    {return new Motoare2 ("MotorJet ");}
    AbstractCaroserie CreazaCaroserie()
    {return new Caroserie2 ("Avion ");}
}