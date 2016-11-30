package SingleRecommender;


import edu.illinois.logic.SingleRecommender.SingleRecommender;
import edu.illinois.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John Seebauer (seebaue2) on 11/25/16.
 */
public class SingleRecommenderTest {
	@Before
	public void setUp() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void getSimilarityOffsetTest() throws Exception {
		String user1 = "Erik";
		String user2 = "Eric";
		List<String> simUserList = Arrays.asList(user1, user2);
		HashMap<String, List<Pair<Integer, Float>>> dataset = new HashMap<>();

		List<Pair<Integer,Float>> u1Movies = Arrays.asList(
				new Pair<>(1, 5.0f),
				new Pair<>(2, 2.0f)
		);
		dataset.put(user1, u1Movies);

		List<Pair<Integer,Float>> u2Movies = Arrays.asList(
				new Pair<>(1, 4.0f),
				new Pair<>(3, 3.0f),
				new Pair<>(2, 1.0f)
		);
		dataset.put(user2, u2Movies);

		SingleRecommender engine = new SingleRecommender(dataset, simUserList);
		assert engine.getSimilarity(user1,user2)==1.0;

	}
	@Test
	public void getSimilarityOppositeTest() throws Exception {
		String user1 = "Erik";
		String user2 = "Eric";
		List<String> simUserList = Arrays.asList(user1, user2);
		HashMap<String, List<Pair<Integer, Float>>> dataset = new HashMap<>();

		List<Pair<Integer,Float>> u1Movies = Arrays.asList(
				new Pair<>(1, 5.0f),
				new Pair<>(2, 1.0f)
		);
		dataset.put(user1, u1Movies);

		List<Pair<Integer,Float>> u2Movies = Arrays.asList(
				new Pair<>(1, 1.0f),
				new Pair<>(2, 5.0f)
		);
		dataset.put(user2, u2Movies);

		SingleRecommender engine = new SingleRecommender(dataset, simUserList);
		assert engine.getSimilarity(user1,user2)==-1.0;

	}

	@Test
	public void getAllSimilarities() throws Exception {
		String user1 = "Erik";
		String user2 = "Eric";
		List<String> simUserList = Arrays.asList(user1, user2);
		HashMap<String, List<Pair<Integer, Float>>> dataset = new HashMap<>();

		List<Pair<Integer,Float>> u1Movies = Arrays.asList(
				new Pair<>(1, 5.0f),
				new Pair<>(2, 2.0f)
		);
		dataset.put(user1, u1Movies);

		List<Pair<Integer,Float>> u2Movies = Arrays.asList(
				new Pair<>(1, 4.0f),
				new Pair<>(2, 1.0f)
		);
		dataset.put(user2, u2Movies);

		SingleRecommender engine = new SingleRecommender(dataset, simUserList);
		List<Pair<String,Float>> simUsers = engine.getAllSimiliarities(user1);
		Pair<String,Float> p0 = simUsers.get(0);
		assert p0.getTwo() == 1.0f;
		Pair<String,Float> p1 = simUsers.get(1);
		assert p1.getTwo() == 1.0f;
	}
	
	@Test
	public void getRecommendations() throws Exception {
		
		String user1 = "Erik";
		String user2 = "Eric";
		List<String> simUserList = Arrays.asList(user1, user2);
		HashMap<String, List<Pair<Integer, Float>>> dataset = new HashMap<>();

		List<Pair<Integer,Float>> u1Movies = Arrays.asList(
				new Pair<>(1, 5.0f),
				new Pair<>(2, 2.0f)
		);
		dataset.put(user1, u1Movies);

		List<Pair<Integer,Float>> u2Movies = Arrays.asList(
				new Pair<>(1, 4.0f),
				new Pair<>(3, 3.0f),
				new Pair<>(2, 1.0f)
		);
		dataset.put(user2, u2Movies);

		SingleRecommender engine = new SingleRecommender(dataset, simUserList);
		List<Pair<Integer, Float>> recs = engine.getRecommendations(user1);
		/*
		recs.stream().forEach(a->{
			System.out.print(a.getOne()+":"+a.getTwo()+"\n");
		});
		*/
		Pair<Integer, Float> p0 = recs.get(0);
		assert p0.getTwo() == 3.0;
		assert 1 == recs.size();
	}
	
}