package eredua.bean;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

/**
 * Produktuen egoerak. Sale klaseak zenbaki bat gordetzen du (0..3), eta hemen
 * dago zenbaki bakoitzari dagokion izena (gui.Utils klaseak JVM-ren Locale-a
 * erabiltzen du, eta Tomcat-en ez du "eus" itzultzen).
 */
public final class Egoerak {

	private static final String[] IZENAK = { "Berria", "Oso ona", "Egokia", "Oso zaharra" };

	private Egoerak() {
	}

	public static String izena(int egoera) {
		if (egoera < 0 || egoera >= IZENAK.length)
			return "?";
		return IZENAK[egoera];
	}

	public static List<SelectItem> aukerak() {
		List<SelectItem> aukerak = new ArrayList<>();
		for (int i = 0; i < IZENAK.length; i++)
			aukerak.add(new SelectItem(i, IZENAK[i]));
		return aukerak;
	}
}
