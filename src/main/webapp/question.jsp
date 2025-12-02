<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.techbarn.webapp.model.Question" %>
<html>
<head>
    <title>Question Details - Tech Barn</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Inter', sans-serif; }
        body { background: #f5f5f5; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; padding: 40px; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 3px solid #667eea; padding-bottom: 15px; }
        .back-buttons { display: flex; gap: 10px; }
        .back-btn, .dashboard-btn { padding: 10px 20px; text-decoration: none; border-radius: 8px; font-weight: 600; transition: all 0.3s; display: inline-block; }
        .back-btn { background: #6c757d; color: white; }
        .back-btn:hover { background: #5a6268; transform: translateY(-2px); }
        .dashboard-btn { background: #667eea; color: white; }
        .dashboard-btn:hover { background: #5568d3; transform: translateY(-2px); }
        h1 { color: #333; font-size: 2rem; margin-bottom: 15px; }
        .question-meta { color: #666; font-size: 0.9rem; margin-bottom: 20px; padding: 10px; background: #f8f9fa; border-radius: 6px; }
        .question-content { color: #555; line-height: 1.8; margin-bottom: 30px; padding: 20px; background: #f8f9fa; border-radius: 8px; font-size: 1.1rem; }
        .answer-section { margin-top: 30px; padding: 25px; background: #e3f2fd; border-radius: 8px; border-left: 4px solid #667eea; }
        .answer-section h3 { color: #667eea; margin-bottom: 15px; font-size: 1.3rem; }
        .answer-text { color: #555; line-height: 1.8; font-size: 1.05rem; padding: 15px; background: white; border-radius: 6px; }
        .no-answer { color: #999; font-style: italic; }
        .reply-form { margin-top: 30px; padding: 25px; background: #fff3cd; border-radius: 8px; border-left: 4px solid #ffc107; }
        .reply-form h3 { color: #856404; margin-bottom: 15px; }
        .reply-form textarea { width: 100%; padding: 15px; border: 2px solid #ffc107; border-radius: 8px; font-size: 1rem; font-family: 'Inter', sans-serif; resize: vertical; min-height: 150px; }
        .reply-form textarea:focus { outline: none; border-color: #ff9800; }
        .submit-btn { margin-top: 15px; padding: 12px 30px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; font-size: 1rem; transition: transform 0.2s; }
        .submit-btn:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4); }
        .error-state { text-align: center; padding: 60px 20px; color: #999; }
        .error-state a { color: #667eea; text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
<div class="container">
    <% 
        Question question = (Question) request.getAttribute("question");
        String role = (String) session.getAttribute("role");
        boolean isRepOrAdmin = "rep".equals(role) || "admin".equals(role);
        
        if (question == null) { 
    %>
        <div class="error-state">
            <h2>Question not found</h2>
            <p><a href="<%=request.getContextPath()%>/faq">← Back to FAQ</a></p>
        </div>
    <% } else { %>
        <div class="header">
            <h1>Question Details</h1>
            <div class="back-buttons">
                <% if (isRepOrAdmin) { %>
                    <a href="<%=request.getContextPath()%>/<%="rep".equals(role) ? "custrep" : "admin"%>" class="dashboard-btn">
                        Dashboard
                    </a>
                <% } %>
                <a href="<%=request.getContextPath()%>/faq" class="back-btn">← Back to FAQ</a>
            </div>
        </div>
        
        <h1><%=question.getTitle()%></h1>
        
        <div class="question-meta">
            Asked on: <%=question.getDateAsked()%> | 
            Status: <strong><%=question.getStatus()%></strong>
        </div>
        
        <div class="question-content">
            <%=question.getContents()%>
        </div>
        
        <div class="answer-section">
            <h3>Answer</h3>
            <div class="answer-text <%=question.getReply() != null && !question.getReply().isEmpty() ? "" : "no-answer"%>">
                <%=question.getReply() != null && !question.getReply().isEmpty() ? question.getReply() : "No answer provided yet."%>
            </div>
        </div>
        
        <% if (isRepOrAdmin) { %>
            <div class="reply-form">
                <h3>Post / Edit Reply</h3>
                <form method="post" action="<%=request.getContextPath()%>/question">
                    <input type="hidden" name="question_id" value="<%=question.getQuestionId()%>" />
                    <textarea name="reply" placeholder="Type your answer here..." required><%=question.getReply() != null ? question.getReply() : ""%></textarea>
                    <button type="submit" class="submit-btn">Save Reply</button>
                </form>
            </div>
        <% } %>
    <% } %>
</div>
</body>
</html>
