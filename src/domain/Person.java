package domain;

import java.util.ArrayList;

public class Person {
	
	
	private int id;
	private int age;
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
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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

	
	
	
	
	
	

}
