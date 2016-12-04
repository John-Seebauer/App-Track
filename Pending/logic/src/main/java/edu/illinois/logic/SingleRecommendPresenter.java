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


	public void init(V view, M model) {

		this.view = view;
		this.model = model;
		this.model.init();
		this.view.setActionListener(this);
		this.model.setActionListener(this);
	}

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
		logger.info( model.getUser()+" asked for a recomendation");
		List<Pair<Integer,Float>> recs = engine.getRecommendations(model.getUser());
		if(recs.size()==0) {
			view.showMessage("Cloud Atlas is quite enjoyable");
		} else {
			String displayString = recs.stream()
					.map(p -> convertIDtoTitle(p.getOne()))
					.reduce(" ", (acc,b)-> acc+ "\n"+b);
			view.showMessage(displayString);
		}
	}

	private String convertIDtoTitle(Integer movieID) {
		return movieID.toString();
	}
}
