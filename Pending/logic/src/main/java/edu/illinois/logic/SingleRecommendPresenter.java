package edu.illinois.logic;

import edu.illinois.logic.SingleRecommender.SingleRecommender;
import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class SingleRecommendPresenter<V extends SingleRecommendView, M extends SingleRecommendModel> extends CommonPresenter<V,M>
	implements  SingleRecommendView.ActionListener, SingleRecommendModel.ActionListener {
	private final static Logger logger = Logger.getLogger(SingleRecommendPresenter.class.getName());

	SingleRecommender engine = null;

	public void setupRecommendationEngine(){
		model.runGetRatingsTable();
	}
	public void getRecommendation(){


	}

	@Override
	public void notifyFailure(JDBCResult result) {

	}

	@Override
	public void createSingleRecommendationEngine(HashMap<String, List<Pair<Integer, Float>>> dataset) {
		engine = new SingleRecommender(dataset,  new ArrayList(dataset.keySet()) );
		logger.info("maybe worked for user: " + model.getUser());
		view.showMessage(engine.getRecommendations(model.getUser()).get(0).getOne().toString());
	}
}
