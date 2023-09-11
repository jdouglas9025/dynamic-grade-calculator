package dynamicgradecalculator.servlets;

import dynamicgradecalculator.model.Calculator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//value attribute represents target URL that activates this controller
@WebServlet(name = "calculateServlet", value = "/calculate-servlet")
public class CalculateServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "Hello world!" + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Add to maps to pass to calculate method
        Map<String, String> mapOfStrings = new HashMap<>();
        Map<String, Double> mapOfDoubles = new HashMap<>();

        //Get data from request object (from form input)
        //Handle all the string attributes (e.g., targetGrade, responses)
        for (String stringAttributeName : Calculator.stringAttributeNames) {
            String currentAttribute = req.getParameter(stringAttributeName);

            mapOfStrings.put(stringAttributeName, currentAttribute);
        }

        //Handle all the attributes are of type double in Java
        for (String doubleAttributeName : Calculator.doubleAttributeNames) {
            String currentAttribute = req.getParameter(doubleAttributeName);

            if (currentAttribute != null && !currentAttribute.isEmpty()) {
                mapOfDoubles.put(doubleAttributeName, Double.parseDouble(currentAttribute));
            } else {
                mapOfDoubles.put(doubleAttributeName, 0.0);
            }
        }

        // Hello
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "Hello world!" + "</h1>");
        out.println("</body></html>");

        //Calculate
        Map<String, String> results = Calculator.calculateGrade(mapOfStrings, mapOfDoubles);

        //Set result attribute of this request to value of result
        req.setAttribute("results", results);

        //Pass control to the JSP (for dynamic output of this data)
        String url = "/dynamic-grade-calculator-result.jsp";
        //Create dispatcher based off this URL
        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        //Forward data to target url
        dispatcher.forward(req, resp);
    }
}
