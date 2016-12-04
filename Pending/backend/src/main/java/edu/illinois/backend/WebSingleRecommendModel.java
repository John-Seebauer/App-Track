package edu.illinois.backend;

import edu.illinois.logic.SingleRecommendModel;
import edu.illinois.util.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class WebSingleRecommendModel extends WebCommonModel implements SingleRecommendModel {
	private final static Logger logger = Logger.getLogger(WebSingleRecommendModel.class.getName());
	private ActionListener actionListener;
	

	@Override
	public void runGetRatingsTable() {
		//TODO: add NULL
		System.out.println(getProperty("GET_RATINGS_TABLE_QUERY"));
		storageService.runSELECTquery(getProperty("GET_RATINGS_TABLE_QUERY"), this::notifyGetRatingsTable, null);
	}

	@Override
	public void notifyGetRatingsTable(JDBCResult ratingsTableResult) {
		//translator
		HashMap<String, List<Pair<Integer, Float>>> dataset = new HashMap<>();
		if(ratingsTableResult.hadFailure()) {
			logger.log(Level.FINE, "get ratings table failed \n");
			return;
		}
		DatabaseTable dbTable = ratingsTableResult.getResult().get();
		dbTable.getRows().stream()
				.forEach(row -> {
					Integer movieID = row.getAttribute("movie_id", Integer.class);
					Float rating =  row.getAttribute("rating", Integer.class).floatValue();
					String username = row.getAttribute("username", String.class);
					if(!dataset.containsKey(username)) {
						List<Pair<Integer, Float>> mList = new LinkedList<>();
						mList.add(new Pair<>(movieID, rating));
						dataset.put(username, mList);
					} else {
						dataset.get(username).add(new Pair<>(movieID, rating));
					}
				});

		actionListener.createSingleRecommendationEngine(dataset);
	}


	public void setActionListener(ActionListener actionListener) {

		this.actionListener = actionListener;
	}


}
