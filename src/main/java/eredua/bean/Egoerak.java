package eredua.bean;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

/**
 * Produktuen egoeren izenak (Sale klaseak 0..3 zenbakia gordetzen du).
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
