package edu.illinois.logic.GroupRecomender;

import edu.illinois.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 12/3/16.
 */
public class GroupRecomenderTest {
	List<Pair<String, List<Pair<Integer, Float>>>> dataset;
	@Before
	public void setUp() throws Exception {

		String user1 = "Erik";
		String user2 = "Erick";

		List<Pair<Integer,Float>> u1Movies = Arrays.asList(
				new Pair<>(1, 5.0f),
				new Pair<>(2, 3.0f),
				new Pair<>(3, 2.0f)
		);

		List<Pair<Integer,Float>> u2Movies = Arrays.asList(
				new Pair<>(1, 4.0f),
				new Pair<>(2, 3.0f),
				new Pair<>(3, 3.0f)
		);
		dataset = Arrays.asList(
				new Pair<>(user1, u1Movies),
				new Pair<>(user2, u2Movies)
		);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void getMovieRatingsTest() throws Exception {

		GroupRecomender engine = new GroupRecomender(dataset);
		assert engine.getMovieRatings(1).get(0)==5.0;
		assert engine.getMovieRatings(1).get(1)==4.0;

	}


	@Test
	public void getEnjoymentTest() throws Exception {
		GroupRecomender engine = new GroupRecomender(dataset);
		assert engine.getEnjoyment(1) > engine.getEnjoyment(2);
		assert engine.getEnjoyment(2) > engine.getEnjoyment(3);
	}

	@Test
	public void getGroupRecomendationsTest() throws Exception {
		GroupRecomender engine = new GroupRecomender(dataset);
		engine.getGroupRecomendations();
	}

}
