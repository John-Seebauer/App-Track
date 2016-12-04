package edu.illinois.logic.SingleRecommender;

import edu.illinois.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SELECT user.name, rating.movieID, rating.ratingID
 * FROM user, ratings
 * WHERE user.userID = rating.userID
 *
 * Index by a test user, and potentially for speed only get users around the user
 *
 * Created by Erik on 11/25/16.
 */
public class SingleRecommender {
	
	private HashMap<String, List<Pair<Integer, Float>>> dataset;
	private List<String> users;
	
	/**
	 * @param dataset "username" : [(movieIDs, ratings)]
	 * @param users   [users] (just the keyspace of the hashmap
	 */
	public SingleRecommender(HashMap<String, List<Pair<Integer, Float>>> dataset, List<String> users) {
		this.dataset = dataset;
		this.users = users;
	}
	
	public Float getSimilarity(String user1, String user2) {
		List<Pair<Integer, Float>> u1Movies = dataset.get(user1);
		List<Pair<Integer, Float>> u2Movies = dataset.get(user2);
		
		Float u1Sum = 0.0f;
		Float u1SqrSum = 0.0f;
		Float u2Sum = 0.0f;
		Float u2SqrSum = 0.0f;
		Float productSum = 0.0f;
		int N = 0; //number of common movies
		
		for (Pair<Integer, Float> r1 : u1Movies) {
			for (Pair<Integer, Float> r2 : u2Movies) {
				if (Objects.equals(r1.getOne(), r2.getOne())) { //are the movie IDs equal
					N++;
					u1Sum += r1.getTwo();
					u1SqrSum += r1.getTwo() * r1.getTwo();
					u2Sum += r2.getTwo();
					u2SqrSum += r2.getTwo() * r2.getTwo();
					productSum += r1.getTwo() * r2.getTwo();
				}
			}
		}
		
		if (0 == N) {
			return 0.0f;
		}
		
		Float Sxx = u1SqrSum - (u1Sum * u1Sum) / N;
		Float Syy = u2SqrSum - (u2Sum * u2Sum) / N;
		double Syx = productSum - (u1Sum * u2Sum) / N;
		
		if (0 == Sxx * Syy) {
			return 0.0f;
		}
		
		double product = Sxx * Syy;
		return (float) (Syx / Math.sqrt((product)));
	}
	
	/***
	 * Get a list of (user, similarity to current user)
	 * @param user1 - user to compare to
	 * @return list of (other user, similarity to current user)
	 */
	public List<Pair<String, Float>> getAllSimiliarities(String user1) {
		List<Pair<String, Float>> simUsers = users.stream()
				.map(other -> new Pair<>(other, getSimilarity(user1, other)))
				.sorted((u1, u2) -> {
					if (u1.getTwo() < u2.getTwo()) {
						return -1;
					} else {
						return 1;
					}
				})
				.collect(Collectors.toList());
		simUsers.forEach(p -> System.out.println(p.getOne()+":"+p.getTwo()));
		return simUsers;
	}
	
	/***
	 * Returns a list of recommended movies, sorted by recommended
	 * @param user to get ratings for
	 * @return a list of (movieID, probability of enjoyment)
	 */
	public List<Pair<Integer, Float>> getRecommendations(String user) {
		if (!dataset.containsKey(user)) {

			//throw (new Exception("User does not exist"));
			return null;
		}
		
		HashMap<Integer, Float> recommendedMoviesRec = new HashMap<>();
		HashMap<Integer, Integer> recommendedMoviesN = new HashMap<>();
		
		List<Pair<String, Float>> simUsers = getAllSimiliarities(user);
		Set<Integer> seenMovies = dataset.get(user).stream()
				.map(rating -> rating.getOne()).collect(Collectors.toSet());
		
		for (Pair<String, Float> userSim : simUsers) {
			if (0 == user.toString().compareTo(userSim.getOne().toString())) {
				continue;
			}
			
			if (0 == userSim.getTwo()) {
				continue;
			}
			
			for (Pair<Integer, Float> movieRating : dataset.get(userSim.getOne().toString())) {
				if (seenMovies.contains(movieRating.getOne())) { //ignore any seen movies
					continue;
				}
				
				if (!recommendedMoviesRec.containsKey(movieRating.getOne())) {
					recommendedMoviesRec.put(movieRating.getOne(), movieRating.getTwo());
					recommendedMoviesN.put(movieRating.getOne(), 1);
				} else {
					Float a = recommendedMoviesRec.get(movieRating.getOne()) + movieRating.getTwo();
					recommendedMoviesRec.put(movieRating.getOne(), a);
					Integer b = recommendedMoviesN.get(movieRating.getOne()) + 1;
					recommendedMoviesN.put(movieRating.getOne(), b);
				}
			}
		}
		
		List<Pair<Integer, Float>> weightedRecs = recommendedMoviesRec.keySet().stream()
				.map(movieID -> {
					Float rec = recommendedMoviesRec.get(movieID);
					System.out.println(movieID+":"+rec);
					Float N = (float) recommendedMoviesN.get(movieID);
					return new Pair<>(movieID, rec / N);
				})
				.sorted((r1, r2) -> {
					if (r1.getTwo() > r2.getTwo()) { //prioritize bigger one
						return -1;
					} else {
						return 1;
					}
				})
				.collect(Collectors.toList());
		return weightedRecs;
	}
}
