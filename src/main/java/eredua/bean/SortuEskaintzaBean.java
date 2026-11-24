package eredua.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Sale;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

/**
 * "Eskaintza Sortu" erabilpen kasuaren bean-a.
 */
@Named("sortu")
@SessionScoped
public class SortuEskaintzaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SaioaBean saioa;

	private String titulua;
	private String deskripzioa;
	private Float prezioa;
	private int egoera;
	private Date data = getGaur();
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
			if (fitxategia != null)
				Files.createDirectories(Path.of("src/main/resources/images"));
			Sale sale = facadeBL.createSale(titulua, deskripzioa, egoera, prezioa, UtilDate.trim(data),
					saioa.getSaltzailea(), fitxategia);
			if (sale == null) {
				errorea(context, "Ezin izan da eskaintza sortu: saltzailea ez da existitzen");
				return null;
			}
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, sortutaMezua(sale), null));
			garbitu();
		} catch (ParamNullException e) {
			errorea(context, "Datu guztiak bete behar dira");
		} catch (MustBeLaterThanTodayException e) {
			errorea(context, "Argitarapen eguna ezin da gaur baino lehenagokoa izan");
		} catch (SaleAlreadyExistException e) {
			errorea(context, "Badago izenburu bereko eskaintza bat saltzaile honentzat");
		} catch (IOException e) {
			errorea(context, "Ezin izan da argazkia igo: fitxategiak irudi bat izan behar du");
		} finally {
			if (fitxategia != null)
				fitxategia.delete();
		}
		return null;
	}

	// Igotako argazkia aldi baterako fitxategi batean gorde (negozioaren logikak File bat behar du)
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

	private String sortutaMezua(Sale sale) {
		Date argitarapena = sale.getPublicationDate();
		if (argitarapena != null && argitarapena.after(getGaur())) {
			String eguna = new SimpleDateFormat("yyyy/MM/dd").format(argitarapena);
			return "Eskaintza sortuta. " + eguna + " egunean argitaratuko da (orduan agertuko da eskaintzetan)";
		}
		return "Eskaintza sortuta eta argitaratuta";
	}

	public String itxi() {
		garbitu();
		return "hasiera";
	}

	private void garbitu() {
		titulua = null;
		deskripzioa = null;
		prezioa = null;
		egoera = 0;
		data = getGaur();
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
