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

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String currencyCode = getCodeFromInput(request.getPathInfo());
        if(currencyCode.isEmpty()) {
            response.sendError(400);
        } else {
            try {
                Mapper mapper = new Mapper();
                CurrencyDAO currencyDAO = new CurrencyDAOImpl();
                Currency currency = currencyDAO.get(currencyCode);
                if (Objects.nonNull(currency)) {
                    out.print(new JSONObject(mapper.toDTO(currency)));
                } else {
                    response.sendError(404);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(500);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    private String getCodeFromInput(String input) {
        if (Objects.nonNull(input) && input.matches("/[A-Z]{3}"))
            return input.substring(1);
        return "";
    }
}
