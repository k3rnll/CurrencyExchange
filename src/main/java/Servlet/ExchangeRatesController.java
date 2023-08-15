package Servlet;

import DAO.CurrencyDAO;
import DAO.CurrencyDAOImpl;
import DAO.ExchangeRateDAO;
import DAO.ExchangeRateDAOImpl;
import DTO.Mapper;
import Model.Currency;
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
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet ("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    public static final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();
    public static final CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    public static final Mapper mapper = new Mapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String output = exchangeRateDAO.getAll()
                            .stream()
                            .map(mapper::toDTO)
                            .map(JSONObject::new)
                            .map(JSONObject::toString)
                            .collect(Collectors.joining(","));
            out.print(output);
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (request.getContentType().contains("application/x-www-form-urlencoded") &&
                Objects.nonNull(request.getParameter("baseCurrencyCode")) &&
                Objects.nonNull(request.getParameter("targetCurrencyCode")) &&
                Objects.nonNull(request.getParameter("rate"))) {
            // need input validation
            try {
                Currency baseCurrency = currencyDAO.get(request.getParameter("baseCurrencyCode"));
                Currency targetCurrency = currencyDAO.get(request.getParameter("targetCurrencyCode"));
                if (Objects.isNull(baseCurrency) || Objects.isNull(targetCurrency)) {
                    response.setStatus(SC_NOT_FOUND);
                } else {
                    double rate = Double.parseDouble(request.getParameter("rate"));
                    ExchangeRate exchangeRate = new ExchangeRate(0L, baseCurrency, targetCurrency, rate);
                    Long id = exchangeRateDAO.insert(exchangeRate);
                    ExchangeRate insertedExchangeRate = new ExchangeRate(id, baseCurrency, targetCurrency, rate);
                    String output = new JSONObject(insertedExchangeRate).toString();
                    out.print(output);
                    out.flush();
                    response.setStatus(SC_CREATED);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if(e.getMessage().contains("CONSTRAINT_UNIQUE"))
                    response.setStatus(SC_CONFLICT);
                else
                    response.setStatus(SC_INTERNAL_SERVER_ERROR);
            } catch (NumberFormatException e) {
                response.setStatus(SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(SC_BAD_REQUEST);
        }
    }
}
