import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@WebServlet ({"/exchangeRates", "/exchangeRate/*"})
public class ExchangeRateController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String uri = request.getRequestURI();
        DBController db = new DBController();
        if(uri.endsWith("/exchangeRates")){
            out.print("do smthng");
        } else {
            String input = uri.substring(uri.indexOf("exchangeRate") + 12);
            String currenciesCodes = parseCurrenciesCodes(input);
            if(Objects.isNull(currenciesCodes)) {
                response.sendError(400);
            } else {
                ExchangeRate exchangeRate = db.getExchangeRate(currenciesCodes.substring(0,3),
                        currenciesCodes.substring(3));
                if(Objects.isNull(exchangeRate))
                    response.sendError(404);
                else
                    out.print(exchangeRate);
            }
        }
    }

    private String parseCurrenciesCodes(String input){
        if(input.matches("/[A-Z]{6}"))
            return input.substring(1);
        else
            return null;
    }
}
