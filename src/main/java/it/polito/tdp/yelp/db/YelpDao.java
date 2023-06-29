package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	/**
	 * Primo punto:
	 * cerco i nodi che siano utenti che hanno pubblicato almeno * recensioni:
	 * @return
	 */
	public List<User> getAllUserWithReviews(int n){
		String sql = "SELECT COUNT(*) AS n_recensioni,u.* "
				+ "FROM reviews r, users u "
				+ "WHERE  r.user_id=u.user_id "
				+ "GROUP BY u.user_id "
				+ "HAVING n_recensioni>=? "
				+ "ORDER BY n_recensioni";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User u  = new User(res.getString("user_id"),res.getInt("votes_funny"),res.getInt("votes_useful"),res.getInt("votes_cool"),res.getString("name"),res.getDouble("average_stars"),res.getInt("review_count"));
				result.add(u);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Secondo punto:
	 * Analisi cammini: Utenti che hanno pubblicato una recensione in un anno specificato per uno stesso locale.
	 * @return
	 */
	public int getCountOfSimilarity(User u1,User u2,int year){
		String sql = "SELECT COUNT(*) AS grdsimilarity "
				+ "FROM reviews r1, reviews r2 "
				+ "WHERE YEAR(r1.review_date) = ? AND YEAR(r2.review_date)=? AND r1.business_id=r2.business_id AND r1.user_id=? AND r2.user_id=?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,year );
			st.setInt(2, year);
			st.setString(3, u1.getUserId());
			st.setString(4, u2.getUserId());
			ResultSet res = st.executeQuery();
			res.first();
			int sim = res.getInt("grdsimilarity");
			res.close();
			
			st.close();
			conn.close();
			return sim;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
