package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	private Food food;
	private Double tempo;
	private Double durata;
	
	
	
	public Event(Food food, double tempo, double durata) {
		super();
		this.food = food;
		this.tempo = tempo;
		this.durata = durata;
	}





	public Food getFood() {
		return food;
	}



	public void setFood(Food food) {
		this.food = food;
	}



	public Double getTempo() {
		return tempo;
	}



	public void setTempo(Double tempo) {
		this.tempo = tempo;
	}



	public Double getDurata() {
		return durata;
	}



	public void setDurata(Double durata) {
		this.durata = durata;
	}



	@Override
	public int compareTo(Event o) {
		return this.tempo.compareTo(o.tempo);
	}
	
}
