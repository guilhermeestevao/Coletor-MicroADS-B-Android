package dao;

import model.Captura;

public class CapturaJpaDao extends GenericJPADAO<Captura> implements CapturaDAO{
	
	public CapturaJpaDao() {
		this.persistentClass = Captura.class;
	}
	
}
