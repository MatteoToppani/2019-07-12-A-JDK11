package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Arco;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public Collection<? extends Food> getVertici(Map<Integer, Food> idMap, int porzioni) {
		String sql = "SELECT p.food_code, f.display_name, COUNT(p.portion_id) AS cnt "
				+ "FROM porzioni p, food f "
				+ "WHERE p.food_code = f.food_code "
				+ "GROUP BY p.food_code, f.food_code "
				+ "HAVING cnt <= ? "
				+ "ORDER BY f.display_name ASC";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			st.setInt(1, porzioni);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Food f = new Food(res.getInt("food_code"),
						res.getString("display_name"));
					
				idMap.put(f.getFood_code(), f);
				list.add(f);
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

	public List<Arco> getArchi(int porzioni, Map<Integer, Food> idMap) {
		String sql = "SELECT fc1.food_code AS id1, fc2.food_code AS id2, AVG(c1.condiment_calories) AS peso "
				+ "FROM food_condiment fc1, food_condiment fc2, condiment c1 "
				+ "WHERE fc1.condiment_code = c1.condiment_code AND fc2.condiment_code = c1.condiment_code "
				+ "AND fc1.food_code > fc2.food_code "
				+ "GROUP BY fc1.food_code, fc2.food_code";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Arco> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2")))
					list.add(new Arco(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")),res.getDouble("peso")));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
