package eredua.bean;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

/**
 * Negozioaren logikaren (BLFacadeImplementation) instantzia bakarra sortu eta
 * itzultzen du (Singleton). Bean kudeatu guztiek instantzia bera erabiltzen dute.
 *
 * Ez da JSFetatik zuzenean erabiltzen, beraz ez du @Named behar.
 */
public class FacadeBean {

	private static FacadeBean singleton = new FacadeBean();

	private static BLFacade facadeInterface;

	private FacadeBean() {
		try {
			facadeInterface = new BLFacadeImplementation(new DataAccess());
		} catch (Exception e) {
			System.out.println("FacadeBean: negozioaren logika sortzean errorea: " + e.getMessage());
		}
	}

	public static BLFacade getBusinessLogic() {
		return facadeInterface;
	}
}
