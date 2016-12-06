package edu.illinois.logic;

import edu.illinois.logic.SingleRecommender.SingleRecommender;
import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class SingleRecommendPresenter<V extends SingleRecommendView, M extends SingleRecommendModel> extends AbstractRecommendPresenter<V, M>
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
		
		//ints = movie ids, float = probability youll like it
		List<Pair<Integer,Float>> recs = engine.getRecommendations(model.getUser());//currently recs is null
		
		
		
		if(recs==null || recs.size()==0) {
			view.showMessage("sorry, no movies could be recommended");
		} else {
			
			ArrayList<Integer> movieID = new ArrayList<Integer>();
			
			for(Pair<Integer,Float> rec : recs){
				movieID.add(rec.getOne());
			}
			
			model.convertIDtoTitle(movieID,this::populateUIPass);
			
			
			
			//view.populateUI(temp);

			/*
			String displayString = recs.stream()
					.map(p -> p.getOne().toString())
					.reduce(" ", (acc,b)-> acc+ "\n"+b);
			view.showMessage(displayString);
			*/
		}
	}
	
	public void populateUIPass(List<String> movies){
		
		String[] moviesArr = new String[movies.size()];
		int i = 0;
		for(String movie : movies){
			moviesArr[i] = movie;
			i++;
		}
		
		view.populateUI(moviesArr);
	}
}