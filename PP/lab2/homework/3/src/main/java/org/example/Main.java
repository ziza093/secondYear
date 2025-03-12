package org.example;
import org.graalvm.polyglot.*;

public class Main {
    public static void main(String[] args) {

        Context context = Context.create();

        String pythonScript = """
                import os
                
                a = int(input('Read the first number: '))
                x = int(input('Read the second number: '))
                """;
        context.eval("python", pythonScript);

        //a is the number of throws and x
        int a = context.getBindings("python").getMember("a").asInt();
        int x = context.getBindings("python").getMember("x").asInt();

        String jsScript = """
                function factorial(val){
                    if(val == 0 || val == 1){
                        return 1;
                    }else{
                        return val*factorial(val-1);
                    }
                }
                function bino(n,x,p){
                    let ans = 0;
                    for(let k = 0; k <= x; k++){
                        let comb_nk = factorial(n) / (factorial(k) * factorial(n - k));
                        ans += comb_nk * Math.pow(p, k) * Math.pow(1 - p, n - k);
                    }
                    return ans;
                }
                """  + "bino(" + a + ", " + x + ", 0.5);";



        context.eval("js", jsScript);

        Value prob = context.eval("js", jsScript);
        double probability = prob.asDouble();

        System.out.println("The prob is: " + probability);
    }
}