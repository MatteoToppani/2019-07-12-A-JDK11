package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private SimpleWeightedGraph<Food,DefaultWeightedEdge> grafo;
	private Map<Integer,Food> idMap;
	private PriorityQueue<Event> queue;
	private int numeroPiatti;
	private double tempoTotale;
	private List<Food> preparati;
	
	public Model() {
		dao = new FoodDao();
	}
	
	public void creaGrafo(int porzioni) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		Graphs.addAllVertices(grafo, dao.getVertici(idMap,porzioni));
		
		for(Arco a : dao.getArchi(porzioni,idMap)) {
			Graphs.addEdge(grafo, a.getF1(), a.getF2(), a.getPeso());
		}
		
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());

	}

	public Set<Food> getVertici() {
		return grafo.vertexSet();
	}
	
	public List<Arco> getCalorie(Food food){
		List<Arco> list = new ArrayList<>();
		
		for(Food e : Graphs.neighborListOf(grafo, food)) {
			DefaultWeightedEdge d = grafo.getEdge(food, e);
			list.add(new Arco(food,e,grafo.getEdgeWeight(d)));
		}
		
		Collections.sort(list);
		if(list.size()>5)
			return list.subList(0, 5);
		else
			return list;
	}
	
	public List<Arco> getTutteCalorie(Food food){
		List<Arco> list = new ArrayList<>();
		
		for(Food e : Graphs.neighborListOf(grafo, food)) {
			DefaultWeightedEdge d = grafo.getEdge(food, e);
			list.add(new Arco(food,e,grafo.getEdgeWeight(d)));
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	public Food getPrimoCalorie(Food food){
		List<Arco> list = new ArrayList<>();
		
		for(Food e : Graphs.neighborListOf(grafo, food)) {
			DefaultWeightedEdge d = grafo.getEdge(food, e);
			list.add(new Arco(food,e,grafo.getEdgeWeight(d)));
		}
		
		Collections.sort(list);
		
		return list.get(0).getF2();
	}
	
	public void init(int k, Food f) {
		queue = new PriorityQueue<Event>();
		preparati = new ArrayList<Food>();
		this.numeroPiatti = 0;
		this.tempoTotale = 0;
		
		List<Arco> list = this.getTutteCalorie(f);
		queue.add(new Event(f,0.0,list.get(0).getPeso()));
		preparati.add(f);
		for(int i = 1; i < k; i++) {
			if(list.get(i)!=null) {
				queue.add(new Event(list.get(i).getF2(),0.0,list.get(i).getPeso()));
			}
		}
		
		simula();
	}

	private void simula() {
		Event e;
		while((e = queue.poll()) != null) {
			Food f = this.getPrimoCalorie(e.getFood());
			this.numeroPiatti++;
			this.tempoTotale = e.getDurata()+e.getTempo();
			preparati.add(e.getFood());
			if(f != null && !preparati.contains(f)) {
				DefaultWeightedEdge edge = grafo.getEdge(e.getFood(), f);
				queue.add(new Event(f,e.getDurata()+e.getTempo(),grafo.getEdgeWeight(edge)));
			}
		}
		
		System.out.println(this.numeroPiatti);
		System.out.println(this.tempoTotale);
	}

}
