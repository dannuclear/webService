package ru.mephi3.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Enumeration<String> headers = req.getHeaderNames();

		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			System.out.println(headerName + ": " + req.getHeader(headerName));
		}

		for (Map.Entry<String, String[]> e : req.getParameterMap().entrySet()) {
			System.out.println(e.getKey() + ": " + Arrays.deepToString(e.getValue()));
		}
		resp.setStatus(404);
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		try {
			writer.println("<h2>Hello from HelloServlet</h2>");
		} finally {
			writer.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		try {
			writer.println("<h2>Hello from HelloServlet</h2>");
		} finally {
			writer.close();
		}
	}
}
