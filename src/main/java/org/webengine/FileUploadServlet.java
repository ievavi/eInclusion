package org.webengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

@MultipartConfig
@WebServlet("/fileupload")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = -9164530828669301284L;
	private static final Logger LOG = Logger.getLogger(FileUploadServlet.class);
	private static File file;
	String message=null;

	EditDatabase edDB = new EditDatabase();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("BEEEEEEEEEEEEEEEEEEEEEEEEP");
		LOG.debug(FileUploadServlet.class.getName());
		LOG.info("loggertest");
		String str = null;
		if (request.getPart("file") != null) {
<<<<<<< HEAD
			// TODO
			// use System.getProperty("java.io.tmpdir") for place
			// and random name for filname
			//LOG.debug("BEEEEEEEEEEEEEEEEEEEEEEEEP");
=======

			// System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEP");
>>>>>>> 1688c074326f6e8d2ef9d39a3fbf53009c3d320d
			Part filePart = request.getPart("file");
			String fileName = getFileName(filePart);
			String fileLocation = ".";
			InputStream inputStream = null;
			OutputStream outputStream = null;
			FileUploadServlet.file = new File(fileLocation + "/" + fileName);

			edDB.actionPerformed(FileUploadServlet.file, "uploadFile");
			try {
				File outputFilePath = new File(fileName);
				inputStream = filePart.getInputStream();
				outputStream = new FileOutputStream(outputFilePath);
				int read = 0;
				final byte[] bytes = new byte[1024];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				if (!edDB.getExt().equals(".xlsx")) {
					message="Wrong data type "+edDB.getExt()+", but need .xlsx!";
				}else {
				message="File "+file.getName()+" uploaded and saved into database";
				}
			} catch (FileNotFoundException fne) {
				message="File upload failed";
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
		if (request.getPart("upload") != null) {
			String str2 = "updateDatabase";
			Part filePart = request.getPart("upload");
			String fileName = getFileName(filePart);
			String fileLocation = ".";
			InputStream inputStream = null;
			OutputStream outputStream = null;
			FileUploadServlet.file = new File(fileLocation + "/" + fileName);
			EditDatabase edDB2 = new EditDatabase();
			edDB2.actionPerformed2(FileUploadServlet.file, str2);
			try {
				File outputFilePath = new File(fileName);
				inputStream = filePart.getInputStream();
				outputStream = new FileOutputStream(outputFilePath);
				int read = 0;
				final byte[] bytes = new byte[1024];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				String ex2=outputFilePath.getName().substring(outputFilePath.getName().indexOf("."), outputFilePath.getName().length());
				if (ex2.equals(".xls")) {
					message="File "+outputFilePath.getName()+" uploaded and saved into database";
					
					}else{
						message="Wrong data type "+edDB.getExt()+", but need .xls!";
					}
				} catch (FileNotFoundException fne) {
					message="File upload failed";
				
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
		
		request.setAttribute("Message", message);
		
		getServletContext().getRequestDispatcher("/M1/databaseEdit2.jsp").forward(request, response);
		//request.getRequestDispatcher("/M1/databaseEdit.jsp");
		response.sendRedirect("/M1/databaseEdit.jsp");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("createOPENTemplate") != null) {
			edDB.actionPerformed(FileUploadServlet.file, "createTemplateXlsx");
			edDB.actionPerformed(FileUploadServlet.file, "openTemplateXlsx");
			response.sendRedirect("/M1/databaseEdit.jsp");
		}  else if (request.getParameter("exportData") != null) {
			edDB.actionPerformed(FileUploadServlet.file, "exportAllData");
			edDB.actionPerformed(FileUploadServlet.file, "openExported");
			response.sendRedirect("/M1/databaseEdit.jsp");
		} else if (request.getParameter("deleteButton") != null) {
			String parameter = request.getParameter("topic");
			edDB.topicParameter = parameter;
			edDB.actionPerformed(FileUploadServlet.file, "deleteFromDB");
<<<<<<< HEAD
//			LOG.debug("////////////////////////////"+parameter);
			response.setHeader("Refresh","10;url=/fileupload?topic=" + parameter);
=======
			// System.out.println("////////////////////////////"+parameter);
			response.setHeader("Refresh", "10;url=/fileupload?topic=" + parameter);
>>>>>>> 1688c074326f6e8d2ef9d39a3fbf53009c3d320d
			response.sendRedirect("/M1/databaseEdit.jsp");
		} else if (request.getParameter("deleteFromDatabase") != null) {
			String parameter = request.getParameter("specific");
			edDB.specificParameter = parameter;
			edDB.actionPerformed(FileUploadServlet.file, "delete");
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