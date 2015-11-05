package dao;

import model.Voo;

public class VooJpaDAO extends GenericJPADAO<Voo> implements VooDAO{

	public VooJpaDAO() {
		this.persistentClass = Voo.class;
	}
	
}
