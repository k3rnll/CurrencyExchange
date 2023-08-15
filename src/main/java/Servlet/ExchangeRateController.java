package Servlet;

import DAO.ExchangeRateDAO;
import DAO.ExchangeRateDAOImpl;
import DTO.Mapper;
import Model.ExchangeRate;
import org.json.JSONObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet ("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String currencyCodes = getCodesFromInput(request.getPathInfo());
        if(currencyCodes.isEmpty()) {
            response.sendError(400);
        } else {
            try {
                Mapper mapper = new Mapper();
                ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();
                String baseCurrencyCode = currencyCodes.substring(0, 3);
                String targetCurrencyCode = currencyCodes.substring(3);
                ExchangeRate exchangeRate = exchangeRateDAO.get(baseCurrencyCode, targetCurrencyCode);
                if (Objects.nonNull(exchangeRate)) {
                    out.print(new JSONObject(mapper.toDTO(exchangeRate)));
                } else {
                    response.sendError(404);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(500);
            }
        }
    }

    private String getCodesFromInput(String input) {
        if (Objects.nonNull(input) && input.matches("/[A-Z]{6}"))
            return input.substring(1);
        return "";
    }
}
