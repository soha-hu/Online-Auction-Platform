package com.techbarn.webapp;

import com.techbarn.webapp.dao.QuestionDAO;
import com.techbarn.webapp.model.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/question")
public class QuestionServlet extends HttpServlet {

    // view question
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idS = req.getParameter("id");
        if (idS == null) {
            resp.sendRedirect(req.getContextPath() + "/faq");
            return;
        }
        int id = Integer.parseInt(idS);
        Question q = QuestionDAO.getById(id);
        req.setAttribute("question", q);
        req.getRequestDispatcher("/question.jsp").forward(req, resp);
    }

    // post answer (rep/admin)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        String role = (s != null && s.getAttribute("role") != null) ? (String) s.getAttribute("role") : null;
        if (role == null || !(role.equals("rep") || role.equals("admin"))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only customer reps or admins can answer questions.");
            return;
        }
        Integer repId = (Integer) s.getAttribute("rep_id");
        Integer adminId = (Integer) s.getAttribute("admin_id");
        int actorId = (repId != null) ? repId : (adminId != null ? adminId : -1);

        int qid = Integer.parseInt(req.getParameter("question_id"));
        String reply = req.getParameter("reply");
        if (reply == null) reply = "";
        QuestionDAO.answerQuestion(qid, actorId, reply);
        resp.sendRedirect(req.getContextPath() + "/question?id=" + qid);
    }
}
