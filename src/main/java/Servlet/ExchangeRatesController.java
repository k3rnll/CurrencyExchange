package Servlet;

import DAO.ExchangeRateDAO;
import DAO.ExchangeRateDAOImpl;
import DTO.Mapper;
import org.json.JSONObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet ("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Mapper mapper = new Mapper();
            ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();
            String output = exchangeRateDAO.getAll()
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
}
