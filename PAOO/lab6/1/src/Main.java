import java.util.List;

//fisierul Main.java
class FactoryMaker{
    private static AbstractFactory pfactory=null;
    static AbstractFactory getFactory(String opt){
        if(opt.equals("a")){
            pfactory = new Fabrica1();
        }
        else
        if(opt.equals("b")){
            pfactory = new Fabrica2();
        }
        return pfactory;
    }
}
//client

public class Main {
    public static void main(String[] args) {

        AbstractFactory pf=FactoryMaker.getFactory("a");
        AbstractMotoare product1=pf.CreazaMotor();
        AbstractCaroserie product2=pf.CreazaCaroserie();
        AbstractMotoare product3 = pf.CreazaMotor();
        AbstractCaroserie product4=pf.CreazaCaroserie();


        AbstractFactory pf2 = FactoryMaker.getFactory("b");
        AbstractMotoare product_1  = pf2.CreazaMotor();
        AbstractCaroserie product_2 = pf2.CreazaCaroserie();
        AbstractMotoare product_3  = pf2.CreazaMotor();
        AbstractCaroserie product_4 = pf2.CreazaCaroserie();



        System.out.println("//////////////////////////////////\n\n\n\n\n\n\n\n");

        List<AbstractMotoare> listaMotoare = new java.util.ArrayList<>(List.of());
        listaMotoare.add(product1);
        listaMotoare.add(product3);
        listaMotoare.add(product_1);
        listaMotoare.add(product_3);

        for (AbstractMotoare motor: listaMotoare){
            motor.operatiaM1();
        }

        List<AbstractCaroserie> listaCaroserii = new java.util.ArrayList<>(List.of());
        listaCaroserii.add(product2);
        listaCaroserii.add(product4);
        listaCaroserii.add(product_2);
        listaCaroserii.add(product_4);

        for (AbstractCaroserie caroserie: listaCaroserii){
            caroserie.operatiaC1();
        }

    }
}