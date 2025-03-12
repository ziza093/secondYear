package org.example;
import org.graalvm.polyglot.*;

//import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        try (Context context = Context.create()) {

            String randomList = """
                    import random
                    
                    def listGenerator(list):
                        for i in range(5):
                            list.append(random.randint(1,10))
                        return  list
                    """;

            String linearRegression = """
                    let slope = 0;
                    let intercept = 0;
                    
                    function fit(x, y) {
                        const n = x.length;
                        const sumX = x.reduce((acc, val) => acc + val, 0);
                        const sumY = y.reduce((acc, val) => acc + val, 0);
                        let sumXY = 0, sumXX = 0;
                    
                        for (let i = 0; i < n; i++) {
                            sumXY += x[i] * y[i];
                            sumXX += x[i] * x[i];
                        }
                    
                        slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
                        intercept = (sumY - slope * sumX) / n;
                    }
                    
                    function predict(x) {
                        return slope * x + intercept;
                    }
                    
                    // Exporting for GraalVM Polyglot
                    globalThis.fit = fit;
                    globalThis.predict = predict;
                    globalThis.getIntercept = () => intercept;
                    globalThis.getSlope = () => slope;
                    """;

            String plot = """
                    import numpy as np
                    import matplotlib.pyplot as plt
                    
                    def generateGraph(x, y, slope, intercept):
                        # Generate regression line points
                        #6 e nr pt care vr predictia
                        x_line = np.linspace(min(x)-1, max(6)+1, 100)  # Extend a bit for better visualization
                        y_line = slope * x_line + intercept
           
                        # Plot the data points
                        plt.scatter(x, y, color="blue", label="Data points")  # Original data
                        plt.plot(x_line, y_line, color="red", label=f"Regression line: y = {slope:.2f}x + {intercept:.2f}")  # Regression line
                   
                        # Plot the prediction for x=5
                        plt.scatter(x_pred, y_pred, color="green", s=100, label=f"Prediction (x=5, y={y_pred:.2f})")
                   
                        # Labels and title
                        plt.xlabel("X values")
                        plt.ylabel("Y values")
                        plt.title("Linear Regression Model")
                        plt.legend()
                   
                        # Save the plot as an image file
                        output_filename = "regression_plot.png"
                        plt.savefig(output_filename)
                        plt.show()
                   
                        print(f"Plot saved as {output_filename}")
                    """;

            //Execute the following scripts
            context.eval("python", randomList);
            context.eval("js", linearRegression);
            context.eval("python", plot);

            //Create a empty python list using GraalVM
            Value x_python = context.eval("python","[]");
            Value y_python = context.eval("python","[]");

            //Bind the empty list to python (kind of creating a variable in python)
            context.getBindings("python").putMember("x_python", x_python);
            context.getBindings("python").putMember("y_python", y_python);

            //Call the function and save the generated list
            x_python = context.eval("python", "listGenerator(x_python)");
            y_python = context.eval("python", "listGenerator(y_python)");


            //Convert the list from python to and java list of integers
            List<Integer> x_randomList = x_python.as(List.class);
            List<Integer> y_randomList = y_python.as(List.class);


            Value fit_js = context.getBindings("js").getMember("fit");
            Value predict_js = context.getBindings("js").getMember("predict");
            Value slope_js = context.getBindings("js").getMember("getSlope");
            Value intercept_js = context.getBindings("js").getMember("getIntercept");

            fit_js.execute(x_randomList, y_randomList);
            double prediction = predict_js.execute(6).asDouble();
            double intercept = intercept_js.execute().asDouble();
            double slope = slope_js.execute().asDouble();

            System.out.println("Ecuatia regresiei: y = " + slope + "x + " + intercept);
            System.out.println("Predictie pentru x=6: " + prediction);

            //Turn the slope from java to python
            context.getBindings("python").putMember("slope_python", slope);
            context.getBindings("python").putMember("intercept_python", intercept);

            context.eval("python", "generateGraph(x_python, y_python, slope_python, intercept_python)");

        }
    }
}
