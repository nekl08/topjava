package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        Meal meal = new Meal(
                id == null || id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.valueOf(req.getParameter("calories")));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal);
        resp.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "" : action) {
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                log.info("Delete {}", id);
                repository.delete(id);
                resp.sendRedirect("meals");
                break;

            case "create":
            case "update":
                Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 666) :
                        repository.get(Integer.parseInt(req.getParameter("id")));
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("mealForm.jsp").forward(req, resp);
                break;
            default:
                log.info("get all");
                req.setAttribute("meals", MealsUtil.getWithExceeded(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                req.getRequestDispatcher("meals.jsp").forward(req, resp);
        }
    }
}
