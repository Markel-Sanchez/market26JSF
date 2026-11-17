package eredua;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Eskaintzen argazkiak nabigatzaileari bidaltzen dizkio: /irudiak/izena.jpg
 *
 * Negozioaren logikak (Sale klaseak) argazkiak "src/main/resources/images/"
 * direktorioan gordetzen ditu, zerbitzariaren lan-direktorioarekiko.
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
		// Fitxategiaren izena bakarrik hartu (../ bezalako bideak saihesteko)
		String izena = new File(bidea).getName();
		File fitxategia = new File(IRUDIEN_KARPETA, izena);
		if (!fitxategia.isFile()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// Sale klaseak PNG formatuan gordetzen ditu irudi guztiak
		resp.setContentType("image/png");
		resp.setContentLengthLong(fitxategia.length());
		Files.copy(fitxategia.toPath(), resp.getOutputStream());
	}
}
