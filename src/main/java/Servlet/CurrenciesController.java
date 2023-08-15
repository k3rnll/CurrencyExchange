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

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("/currencies")
public class CurrenciesController extends HttpServlet {
    private static final CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Mapper mapper = new Mapper();
            String output = currencyDAO.getAll()
                            .stream()
                            .map(mapper::toDTO)
                            .map(JSONObject::new)
                            .map(JSONObject::toString)
                            .collect(Collectors.joining(","));
            out.print(output);
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
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
                Currency currency = new Currency(0L,
                        request.getParameter("code"),
                        request.getParameter("name"),
                        request.getParameter("sign"));
                Long id = currencyDAO.insert(currency);
                response.setStatus(SC_CREATED);
                Currency insertedCurrency = new Currency(id, currency.getCode(),
                        currency.getFullName(), currency.getSign());
                String output = new JSONObject(insertedCurrency).toString();
                out.print(output);
                out.flush();
            } catch (SQLException e) {
                e.printStackTrace();
                if(e.getMessage().contains("CONSTRAINT_UNIQUE"))
                    response.setStatus(SC_CONFLICT);
                else
                    response.setStatus(SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(SC_BAD_REQUEST);
        }
    }
}