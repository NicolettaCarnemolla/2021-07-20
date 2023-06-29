package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	Graph<User,DefaultWeightedEdge> grafo;
	YelpDao dao;
	List<User> user;
	
	public Model() {
		dao = new YelpDao();
	}
	
	public void creaGrafo(int n,int anno) {
		user = dao.getAllUserWithReviews(n);
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//punto B: abbiamo inserito i vertici del grafo:
		Graphs.addAllVertices(grafo, user);
		
		//punto C: Sistemiamo gli archi:
		for(User u1:grafo.vertexSet()) {
			for(User u2: grafo.vertexSet()) {
				if(u1.getUserId().compareTo(u2.getUserId())!=0 && !u1.equals(u2)) {
					int peso = dao.getCountOfSimilarity(u1,u2,anno);
					if(peso > 0) {
						Graphs.addEdge(grafo, u1, u2,peso);
					}
				}
			}
		}
		
		
		
	}

	public int getVertex() {
		return grafo.vertexSet().size();
		// TODO Auto-generated method stub
		
	}
	
	public int getEdges() {
		return grafo.edgeSet().size();
	}
	
	public List<User> users(){
		return user;
	}
	
	
	//Punto D: 
	//Stampare l'utente più simile ad u cliccato, quello con grado di similarità=pesoi grafo maggiore:
	public List<User> userssimili(User u) {
		int maxpeso = 0;
		for(DefaultWeightedEdge e : this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)>maxpeso) {
				maxpeso = (int) this.grafo.getEdgeWeight(e);
			}
		}
		
		List<User> users = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)== maxpeso) {
				User u2 = Graphs.getOppositeVertex(grafo, e, u);
				users.add(u2);
			}
		}
		return users;
	}
	
	
	
	
	
	
}
