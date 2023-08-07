import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


@WebServlet({"/currencies", "/currency/*"})
public class CurrencyController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String uri = request.getRequestURI();
        if(uri.endsWith("/currencies")) {
            DBController db = new DBController();
            out.print(db.getCurrenciesSet().toString());
        }
        else {
            String input = uri.substring(uri.indexOf("currency") + 8);
            String code = parseCurrencyCode(input);
            if(Objects.isNull(code))
                response.sendError(400);
            else
                out.print(code);
        }
    }

    private String parseCurrencyCode(String input){
        if(input.matches("/[A-Z]{3}"))
            return input.substring(1);
        else
            return null;
    }
}
