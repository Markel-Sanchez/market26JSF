package eredua.bean;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**
 * Saioko saltzailea (Login erabilpen kasua egin arte, finkoa).
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
