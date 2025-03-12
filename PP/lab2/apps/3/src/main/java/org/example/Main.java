package org.example;

import org.graalvm.polyglot.*;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        try(Context context = Context.create()){
            String pythonCode = """
                    import  random
                    
                    def listGenerator(list):                    
                        for i in range(20):
                            list.append(random.randint(1,100))
                        return  list 
                    """;
            String javascriptCode = """
                    function writeArray(list){
                        console.log("The list is: " + list);
                    }
                    """;

            String js_rCode = """
                    function listManipulation(numArray){
                        var remove = 0.2*numArray.length;
                        numArray = numArray.sort(function (a, b) {  return a - b;  });
                        numArray.splice(0, remove);
                        numArray.splice(numArray.length-remove, remove);
                        
                        const average = numArray => numArray.reduce((a, b) => a + b) / numArray.length;
                        console.log("The list after the removal: " + numArray);
                        console.log("The average of the list is: " + average(numArray));
                    }
                    """;
            //Execute the following scripts
            context.eval("python", pythonCode);
            context.eval("js", javascriptCode);
            context.eval("js", js_rCode);

            //Create a python empty list using GraalVM
            Value python_list = context.eval("python","[]");
            context.getBindings("python").putMember("python_list", python_list);  //Bind the empty list to python (kind of creating a variable in python)

            //Call the function and save the generated list
            python_list = context.eval("python", "listGenerator(python_list)");

            //Convert the list from python to and java list of integers
            List<Integer> randomList = python_list.as(List.class);

            //Create an empty javaScript list
            //You need to create these lists in their "script" language, because if you
            //try to parse a Java list to a python script GraalVM won't make the
            //conversion and you'll get an error
            Value jsList = context.eval("js", "[]");

            //Transfer the Java list (the random nr list from python) to a Js list
            for(int num : randomList){
                jsList.setArrayElement(jsList.getArraySize(), num);
            }

            //Create a js variable which is my jsList and name it jsArray (in the script) and print the list
            context.getBindings("js").putMember("jsArray", jsList);
            context.eval("js", "writeArray(jsArray);");

            //Create another list in js and parse it as a parameter to the listManipulation function
            context.getBindings("js").putMember("numArray", jsList);
            context.eval("js", "listManipulation(numArray);");
            }
    }
}