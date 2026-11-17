package eredua.bean;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**
 * Saioaren informazioa: une honetan lan egiten ari den saltzailea.
 * Login erabilpen kasua egin arte, Swing aplikazioko saltzaile bera erabiltzen da.
 */
@Named("saioa")
@SessionScoped
public class SaioaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String saltzailea = "seller3@gmail.com";

	public String getSaltzailea() {
		return saltzailea;
	}

	public void setSaltzailea(String saltzailea) {
		this.saltzailea = saltzailea;
	}
}
