package Servlet;

import DAO.CurrencyDAO;
import DAO.CurrencyDAOImpl;
import DTO.Mapper;
import Model.Currency;
import org.json.JSONObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {
    public static final CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    public static final Mapper mapper = new Mapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String currencyCode = getCodeFromInput(request.getPathInfo());
        if(currencyCode.isEmpty()) {
            response.setStatus(SC_BAD_REQUEST);
        } else {
            try {
                Currency currency = currencyDAO.get(currencyCode);
                if (Objects.nonNull(currency)) {
                    out.print(new JSONObject(mapper.toDTO(currency)));
                    out.flush();
                } else {
                    response.setStatus(SC_NOT_FOUND);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private String getCodeFromInput(String input) {
        if (Objects.nonNull(input) && input.matches("/[A-Z]{3}"))
            return input.substring(1);
        return "";
    }
}
