package org.webengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.junit.rules.TemporaryFolder;

@MultipartConfig
@WebServlet("/fileupload")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = -9164530828669301284L;
	private static final Logger LOG = Logger.getLogger(FileUploadServlet.class);
	private static File file;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO rewrite all to use loggers
		// System.out.println("doPost() workdir:" +
		// System.getProperty("user.dir"));
		String str = null;
		if ( request.getPart("file") != null){
			Part filePart = request.getPart("file");
			String fileName = getFileName(filePart);
			String fileLocation = ".";
			InputStream inputStream = null;
			OutputStream outputStream = null;
			FileUploadServlet.file = new File(fileLocation + "/" + fileName);
			EditDatabase edDB = new EditDatabase();
			edDB.actionPerformed(FileUploadServlet.file, str);
			try {
				File outputFilePath = new File(fileName);
				inputStream = filePart.getInputStream();
				outputStream = new FileOutputStream(outputFilePath);
				int read = 0;
				final byte[] bytes = new byte[1024];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} catch (FileNotFoundException fne) {
				fne.printStackTrace();

			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
		}	
		}
		if ( request.getPart("upload") != null){
			
		}
		// response.setContentType("text/html;UTF-8");
		// PrintWriter writer = response.getWriter();
		// writer.write("File upload completed");
		// writer.close();
		response.sendRedirect("/M1/databaseEdit.jsp");


	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("button1") != null) {
			EditDatabase edDB = new EditDatabase();
			edDB.actionPerformed(FileUploadServlet.file, "OpenFile");
			response.sendRedirect("/M1/databaseEdit.jsp");
		} else if(request.getParameter("button2") != null){
			EditDatabase edDB = new EditDatabase();
			edDB.actionPerformed(FileUploadServlet.file, "createTemplateXlsx");
			response.sendRedirect("/M1/databaseEdit.jsp");
		} else if(request.getParameter("button3") != null){
			EditDatabase edDB = new EditDatabase();
			edDB.actionPerformed(FileUploadServlet.file, "openTemplateXlsx");
			response.sendRedirect("/M1/databaseEdit.jsp");
		}else if(request.getParameter("button4") != null){
			EditDatabase edDB = new EditDatabase();
			edDB.actionPerformed(FileUploadServlet.file, "updateDatabase");
			response.sendRedirect("/M1/databaseEdit.jsp");
		}else if(request.getParameter("button5") != null){
			EditDatabase edDB = new EditDatabase();
			edDB.actionPerformed(FileUploadServlet.file, "exampleFile");
			response.sendRedirect("/M1/databaseEdit.jsp");
		}
		
	}
	
	
	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}