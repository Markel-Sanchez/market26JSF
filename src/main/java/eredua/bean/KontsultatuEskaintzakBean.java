package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Sale;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 * "Eskaintzak Kontsultatu" erabilpen kasuaren bean kudeatua.
 */
@Named("kontsultatu")
@ViewScoped
public class KontsultatuEskaintzakBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bilaketa = "";
	private List<Sale> eskaintzak = new ArrayList<>();
	private Sale aukeratua;

	public void bilatu() {
		aukeratua = null;
		BLFacade facadeBL = FacadeBean.getBusinessLogic();
		if (facadeBL == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Negozioaren logika ez dago eskuragarri (begiratu config.xml)", null));
			return;
		}
		String testua = bilaketa == null ? "" : bilaketa;
		eskaintzak = facadeBL.getPublishedSales(testua, UtilDate.trim(new Date()));
	}

	public String getEgoeraIzena() {
		return aukeratua == null ? "" : Egoerak.izena(aukeratua.getStatus());
	}

	public String getBilaketa() {
		return bilaketa;
	}

	public void setBilaketa(String bilaketa) {
		this.bilaketa = bilaketa;
	}

	public List<Sale> getEskaintzak() {
		return eskaintzak;
	}

	public Sale getAukeratua() {
		return aukeratua;
	}

	public void setAukeratua(Sale aukeratua) {
		this.aukeratua = aukeratua;
	}
}
