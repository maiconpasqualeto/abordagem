/**
 * 
 */
package br.com.skylane.dibo;

import com.mongodb.BasicDBObject;

/**
 * @author maicon
 *
 */
public class Cadastro {
	
	private String nome;
	private String email;
	private String telefone;	
	private String prefixo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
	
	public static Cadastro fromDBObject(BasicDBObject db) {
		Cadastro c = new Cadastro();
		c.setEmail(db.getString("email"));
		c.setNome(db.getString("nome"));
		c.setTelefone(db.getString("telefone"));
		c.setPrefixo(db.getString("prefixo"));
		return c;
	}

}
