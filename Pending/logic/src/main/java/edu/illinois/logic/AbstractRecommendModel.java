package edu.illinois.logic;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by John Seebauer (seebaue2) on 12/4/16.
 */
public interface AbstractRecommendModel extends CommonModel {
	void convertIDtoTitle(Collection<Integer> movieID, Consumer<List<String>> resultConsumer);
}
