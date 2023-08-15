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
import java.util.stream.Collectors;

@WebServlet("/currencies")
public class CurrenciesController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            CurrencyDAO currencyDAO = new CurrencyDAOImpl();
            Mapper mapper = new Mapper();
            String output = currencyDAO.getAll()
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (request.getContentType().contains("application/x-www-form-urlencoded") &&
                Objects.nonNull(request.getParameter("code")) &&
                Objects.nonNull(request.getParameter("name")) &&
                Objects.nonNull(request.getParameter("sign"))) {
            try {
                CurrencyDAO currencyDAO = new CurrencyDAOImpl();
                Currency currency = new Currency(0,
                        request.getParameter("code"),
                        request.getParameter("name"),
                        request.getParameter("sign"));
                response.setStatus(201);
                currencyDAO.insert(currency);
                String output = new JSONObject(currencyDAO.get(currency.getCode())).toString();
                out.print(output);
            } catch (SQLException e) {
                e.printStackTrace();
                if(e.getErrorCode() == 19) // SQLITE_CONSTRAINT  19   /* Abort due to constraint violation */
                    response.sendError(409);
                else
                    response.sendError(500);
            }
        } else {
            response.sendError(400);
        }
    }
}