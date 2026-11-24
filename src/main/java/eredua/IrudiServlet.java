package eredua;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Eskaintzen argazkiak erakusten ditu (/irudiak/izena). Sale klaseak
 * "src/main/resources/images/" karpetan gordetzen ditu.
 */
@WebServlet("/irudiak/*")
public class IrudiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String IRUDIEN_KARPETA = "src/main/resources/images/";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String bidea = req.getPathInfo();
		if (bidea == null || bidea.length() <= 1) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String izena = new File(bidea).getName();
		File fitxategia = new File(IRUDIEN_KARPETA, izena);
		if (!fitxategia.isFile()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		resp.setContentType("image/png");
		resp.setContentLengthLong(fitxategia.length());
		Files.copy(fitxategia.toPath(), resp.getOutputStream());
	}
}
