package dao;

import model.Mensagens;

public class MensagensJpaDAO extends GenericJPADAO<Mensagens> implements MensagensDAO {

	public MensagensJpaDAO() {

		this.persistentClass = Mensagens.class;
		
	}
	
}
