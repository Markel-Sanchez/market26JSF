package eredua.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Sale;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

/**
 * "Eskaintza Sortu" erabilpen kasuaren bean kudeatua.
 */
@Named("sortu")
@ViewScoped
public class SortuEskaintzaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SaioaBean saioa;

	private String titulua;
	private String deskripzioa;
	private Float prezioa;
	private int egoera;
	private Date data;
	private transient Part argazkia;

	public String sortu() {
		FacesContext context = FacesContext.getCurrentInstance();
		BLFacade facadeBL = FacadeBean.getBusinessLogic();
		if (facadeBL == null) {
			errorea(context, "Negozioaren logika ez dago eskuragarri (begiratu config.xml)");
			return null;
		}

		File fitxategia = null;
		try {
			fitxategia = gordeArgazkia();
			Sale sale = facadeBL.createSale(titulua, deskripzioa, egoera, prezioa, UtilDate.trim(data),
					saioa.getSaltzailea(), fitxategia);
			if (sale == null) {
				errorea(context, "Ezin izan da eskaintza sortu: saltzailea ez da existitzen");
				return null;
			}
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eskaintza sortuta", null));
			garbitu();
		} catch (ParamNullException | MustBeLaterThanTodayException | SaleAlreadyExistException e) {
			errorea(context, e.getMessage());
		} catch (IOException e) {
			errorea(context, "Errorea argazkia igotzean: " + e.getMessage());
		} finally {
			if (fitxategia != null)
				fitxategia.delete();
		}
		return null;
	}

	/**
	 * Igotako argazkia aldi baterako fitxategi batean gordetzen du, negozioaren
	 * logikak File bat behar duelako. Fitxategiaren izena mantentzen da, Sale
	 * klaseak izen hori erabiltzen duelako irudia gordetzeko.
	 */
	private File gordeArgazkia() throws IOException {
		if (argazkia == null || argazkia.getSize() == 0)
			return null;
		String izena = Path.of(argazkia.getSubmittedFileName()).getFileName().toString();
		Path karpeta = Files.createTempDirectory("marketjsf");
		Path helburua = karpeta.resolve(izena);
		try (InputStream in = argazkia.getInputStream()) {
			Files.copy(in, helburua, StandardCopyOption.REPLACE_EXISTING);
		}
		File fitxategia = helburua.toFile();
		fitxategia.deleteOnExit();
		karpeta.toFile().deleteOnExit();
		if (ImageIO.read(fitxategia) == null)
			throw new IOException("fitxategia ez da irudi bat");
		return fitxategia;
	}

	private void errorea(FacesContext context, String mezua) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mezua, null));
	}

	private void garbitu() {
		titulua = null;
		deskripzioa = null;
		prezioa = null;
		egoera = 0;
		data = null;
		argazkia = null;
	}

	public List<SelectItem> getEgoerak() {
		return Egoerak.aukerak();
	}

	public Date getGaur() {
		return UtilDate.trim(new Date());
	}

	public String getTitulua() {
		return titulua;
	}

	public void setTitulua(String titulua) {
		this.titulua = titulua;
	}

	public String getDeskripzioa() {
		return deskripzioa;
	}

	public void setDeskripzioa(String deskripzioa) {
		this.deskripzioa = deskripzioa;
	}

	public Float getPrezioa() {
		return prezioa;
	}

	public void setPrezioa(Float prezioa) {
		this.prezioa = prezioa;
	}

	public int getEgoera() {
		return egoera;
	}

	public void setEgoera(int egoera) {
		this.egoera = egoera;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Part getArgazkia() {
		return argazkia;
	}

	public void setArgazkia(Part argazkia) {
		this.argazkia = argazkia;
	}
}
