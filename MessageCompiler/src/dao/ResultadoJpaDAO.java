package dao;

import model.Resultado;

public class ResultadoJpaDAO extends GenericJPADAO<Resultado> implements ResultadoDAO{

	public ResultadoJpaDAO() {
		this.persistentClass = Resultado.class;
	}
	
}
