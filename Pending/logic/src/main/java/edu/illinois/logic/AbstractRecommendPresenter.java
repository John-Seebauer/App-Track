package edu.illinois.logic;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by John Seebauer (seebaue2) on 12/4/16.
 */
public class AbstractRecommendPresenter<V extends AbstractRecommendView, M extends AbstractRecommendModel> extends CommonPresenter<V, M> {
	
	protected void convertIDtoTitle(Collection<Integer> movieID, Consumer<List<String>> resultConsumer) {
		
	}
}
