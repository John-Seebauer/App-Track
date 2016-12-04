package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 12/4/16.
 */
public class AbstractRecommendPresenter<V extends AbstractRecommendView, M extends AbstractRecommendModel> extends CommonPresenter<V, M> {
	
	protected String convertIDtoTitle(Integer movieID) {
		return movieID.toString();
	}
}
