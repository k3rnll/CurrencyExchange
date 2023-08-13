import org.json.JSONObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;


@WebServlet({"/currencies", "/currency/*"})
public class CurrencyController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String uri = request.getRequestURI();
        DBController db = new DBController();
        if(uri.endsWith("/currencies")) {
            try {
                Mapper mapper = new Mapper();
                String output =
                        db.getCurrenciesSet()
                        .stream()
                        .map(mapper::toDTO)
                        .map(JSONObject::new)
                        .map(JSONObject::toString)
                        .collect(Collectors.joining(","));
                out.print(output);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(500);
            }
        }
        else {
            String input = uri.substring(uri.indexOf("currency") + 8);
            String code = parseCurrencyCode(input);
            if(Objects.isNull(code))
                response.sendError(400);
            else {
                try {
                    Currency currency = db.getCurrency(code);
                    if (Objects.isNull(currency))
                        response.sendError(404);
                    else
                        out.print(db.getCurrency(code).toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendError(500);
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    private String parseCurrencyCode(String input){
        if(input.matches("/[A-Z]{3}"))
            return input.substring(1);
        else
            return null;
    }
}
