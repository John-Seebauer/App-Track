package edu.illinois.logic;

import edu.illinois.logic.GroupRecomender.GroupRecomender;
import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;
import edu.illinois.logic.SingleRecommender.SingleRecommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class GroupRecommendPresenter<V extends GroupRecommendView, M extends GroupRecommendModel>
		extends AbstractRecommendPresenter<V,M>
	implements  GroupRecommendView.ActionListener, GroupRecommendModel.ActionListener {
	private final static Logger logger = Logger.getLogger(GroupRecommendPresenter.class.getName());

	SingleRecommender singleRecEngine = null;
	List<String> users;


	public void init(V view, M model) {

		this.view = view;
		this.model = model;
		this.model.init();
		this.view.setActionListener(this);
		this.model.setActionListener(this);
	}

	public void setupRecommendationEngine(List<String> users){
		model.runGetRatingsTable();
		this.users = users;
	}



	@Override
	public void getGroupRecomendation(HashMap<String, List<Pair<Integer, Float>>> dataset) {
		
		singleRecEngine = new SingleRecommender(dataset,  new ArrayList(dataset.keySet()) );
		GroupRecomender groupRecEngine = new GroupRecomender(singleRecEngine, users);
		Integer movieRec = groupRecEngine.getGroupRecomendations();
		//view.showMessage(convertIDtoTitle(movieRec));
		
		String movieRecStr = convertIDtoTitle(movieRec);
		String[] movieRecStrs = new String[1];
		movieRecStrs[0] = movieRecStr;
		
		view.populateUI(movieRecStrs);


	}

	private String convertIDtoTitle(Integer movieID) {
		return movieID.toString();
	}

	@Override
	public void notifyFailure(JDBCResult result) {

	}

}
