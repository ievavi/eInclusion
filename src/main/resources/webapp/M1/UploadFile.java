import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.annotation.WebServlet;

public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 312L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (valid(request)) {
			setResponseHeader(response);
			PrintWriter out = response.getWriter();
			out.append("<html><head><body>"
					+ "<script>"
					+ "function upload() {"
					+ "  document.getElementById('name').value = document.getElementById('file').value;"
					+ "  if(document.getElementById('name').value!='') "
					+ "    document.getElementById('form').submit();"
					+ "}"
					+ "</script>"
					+ ""
					+ "<form method='POST' id='form' enctype='multipart/form-data' action='/upload'>"
					+ "File: <input type='file' id='file' name='file'>&nbsp;"
					+ "<input type='hidden' id='name' name='name'>"
					+ "<input type='hidden' id='sessid' name='sessid' value='"
					+ sessid
					+ "'>"
					+ "<input type='button' value='Upload' onclick='upload();'>"
					+ "</form></body></html>");
		}
	}
	@WebServlet("/upload")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			System.out.println("hello from doPost");
		if (valid(request)) {
			filename = request.getParameter("name");
			Part filePart = request.getPart("file"); // Retrieves input "file"
			filePart.write(filename);
			response.sendRedirect("/?sessid=" + sessid);
		}
	}
