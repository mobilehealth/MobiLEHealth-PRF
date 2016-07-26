package domain;

import java.util.ArrayList;
import java.util.Calendar;

public class Person {
	
	
	private int id;
	private int idade = 0;
	private Calendar age;
	private String name_first;
	private String name_last;
	private String gender;
	private String tempodoenca;
	private String afinidade;
	private String disease;
	private ArrayList knows = new ArrayList<Integer>();
	Integer sameFriends = null; // Usados apenas na população; Ingeter para poder iniciar com null
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return this.idade;
	}
	
	public void setAge(Calendar age) {
		
		
		Calendar calendar = Calendar.getInstance();
		this.idade = calendar.get(Calendar.YEAR) - age.get(Calendar.YEAR);

		if (calendar.get(Calendar.MONTH) < age.get(Calendar.MONTH)) {
			this.idade--;
		} else {
			if (calendar.get(Calendar.MONTH) == age.get(Calendar.MONTH)) {
				if (calendar.get(Calendar.DAY_OF_MONTH) < age.get(Calendar.DAY_OF_MONTH)) {
					this.idade--;
				}
			}
		}
	}
	
	public String getName_first() {
		return name_first;
	}
	public void setName_first(String name_first) {
		this.name_first = name_first;
	}
	public String getName_last() {
		return name_last;
	}
	public void setName_last(String name_last) {
		this.name_last = name_last;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTempodoenca() {
		return tempodoenca;
	}
	public void setTempodoenca(String tempodoenca) {
		this.tempodoenca = tempodoenca;
	}
	public String getAfinidade() {
		return afinidade;
	}
	public void setAfinidade(String afinidade) {
		this.afinidade = afinidade;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	
	public ArrayList getKnows() {
		return knows;
	}
	
	public void setKnows(int knows) {
		this.knows.add(knows);
	}
	public Integer getSameFriends() {
		return sameFriends;
	}
	public void setSameFriends(Integer sameFriends) {
		this.sameFriends = sameFriends;
	}
	
	public int getIdade() {
		
		return this.idade;
		
	}
	
	
	public void setIdade(int idade) {
		
		this.idade = idade;
		
	}

	
	
	
	
	
	

}
