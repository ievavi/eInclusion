package org.webengine;

import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.einclusion.model.ModelManager;

@MultipartConfig
@WebServlet("/fileupload")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = -9164530828669301284L;
	private static final Logger LOG = Logger.getLogger(FileUploadServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO rewrite all to use loggers
		System.out.println("doPost() workdir:" + System.getProperty("user.dir"));
		Part filePart = request.getPart("file");
		String fileName = getFileName(filePart);
		// System.out.println("filename " + fileName);
		String fileLocation = ".";
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File file = new File(fileLocation + "/" + fileName);
		System.out.println("File name: " + file.getAbsolutePath());
		//EditDatabase edDB = new EditDatabase();
		//edDB.actionPerformed(file);
		
		/*class StupidButton extends JPanel implements ActionListener{
			Object eventSource;
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton chooseFile = new JButton("Choose a file");					// creates a new button for choosing file path
				chooseFile.setFont(new Font("Arial", Font.BOLD, 12));		// sets button font
				chooseFile.addActionListener(this);							// adds actionlistener to button
				chooseFile.setToolTipText("Choose a valid .xlsx file");		// adds tooltip to button
				chooseFile.setBounds(50, 10, 190, 30);						// set location and size of button
				this.add(chooseFile);
				eventSource = e.getSource();
			}
		}
		StupidButton button = new StupidButton();
		System.out.println("Button source: " + button.eventSource);
		*/
		org.einclusion.GUI.EditDatabasePanel edDB = new org.einclusion.GUI.EditDatabasePanel();
		edDB.FILE = file;
		edDB.actionPerformed(new ActionEvent(edDB, 0, "chooseFile"));
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

		response.setContentType("text/html;UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write("File upload completed");
		writer.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("doGet() workdir:" + System.getProperty("user.dir"));
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