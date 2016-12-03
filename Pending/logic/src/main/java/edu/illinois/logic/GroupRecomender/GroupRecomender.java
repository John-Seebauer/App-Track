package edu.illinois.logic.GroupRecomender;

import edu.illinois.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by admin on 12/3/16.
 */
public class GroupRecomender {
	List<Pair<String, List<Pair<Integer, Float>>>> dataset;
	final Float C1 = 0.2f;
	final Float C2 = 0.2f;
	final int LIMIT =3;

	/***
	 *
	 * @param dataset of the format [username, [(MovieID, rating)]]
	 */
	public GroupRecomender(List<Pair<String, List<Pair<Integer, Float>>>> dataset) {
		this.dataset = dataset;

	}

	/***
	 * Could be modified to take a genere or other options
	 *
	 * @return MovieID of recomended movies
	 */
	public Integer getGroupRecomendations() {
		PriorityQueue<Pair<Integer,Float>> mrCue = getRatingsQueue(LIMIT);
		Pair<Integer,Float> current;
		Pair<Integer,Float> max = new Pair<>(0,0.0f);
		current = mrCue.poll();
		int numUsers = dataset.size();
		while(null != current)  {
			//if N*maxrating<the current max enjoyment we done
			if(current.getTwo()*numUsers< max.getTwo()) {
				break;
			}
			Float Eg = getEnjoyment(current.getOne());
			if(Eg > max.getTwo()) {//replace it if we like it more
				max = new Pair<>(current.getOne(), Eg);
			}
			current = mrCue.poll();
		}
		return max.getOne();

	}

	private class ratingCompatator implements Comparator<Pair<Integer, Float>> {
		public int compare(Pair<Integer, Float> first, Pair<Integer, Float> second) {
			if(first.getTwo() > second.getTwo()) {
				return -1;
			} else {
				return 1;
			}
		}

	}

	public PriorityQueue<Pair<Integer,Float>> getRatingsQueue(int limit) {
		Comparator<Pair<Integer,Float>> comparator = new ratingCompatator();
		PriorityQueue retQ = new PriorityQueue(limit*dataset.size(), comparator);
		Set<Integer> addedSet = new HashSet<>();
		Pair<Integer,Float> current;
		for(int j = 0; j< limit; j++) {
			for(int i=0;i<dataset.size(); i++) {
				if(j>=dataset.get(i).getTwo().size()) {
					continue;
				}
				current = dataset.get(i).getTwo().get(j); // get the jth movie
				if(!addedSet.contains(current.getOne())) {
					retQ.add(current);
					addedSet.add(current.getOne());
				}
			}
		}
		return retQ;
	}

	/***
	 * given a movieID returns a list of its ratings, sorted high to low
	 */
	public List<Float> getMovieRatings(Integer movieID) {

	    List<Float> rList = dataset.stream().flatMap( user ->
				user.getTwo().stream()
				.filter(mr -> mr.getOne()==movieID)
				.map(mr -> mr.getTwo())
	    ).sorted(Collections.reverseOrder())
			    .collect(Collectors.toList());
		return rList;
	}


	public Float getEnjoyment(Integer movieID) {

		List<Float> ratings = getMovieRatings(movieID);
		int size = ratings.size();
		Float sum = ratings.stream().reduce(0.0f, (a,r) ->a+r)/size;
		List<Float> correctionFactors = new LinkedList<>();
		Float range, ignoredFactor;
		Float greatest = ratings.get(0);
		for(int i = 0; i<size; i++) {
			range = greatest - ratings.get(size-i-1);
			ignoredFactor = ((float) (i*size/(size-i)));
			correctionFactors.add(range*C1 + ignoredFactor*C2);
		}
		Float correctionFactor =correctionFactors.stream().min(Float::compare).get();
		return sum-correctionFactor;
	}
}
