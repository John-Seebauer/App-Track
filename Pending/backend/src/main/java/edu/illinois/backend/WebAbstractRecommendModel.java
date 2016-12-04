package edu.illinois.backend;

import edu.illinois.logic.AbstractRecommendModel;
import edu.illinois.util.DatabaseTable;
import edu.illinois.util.JDBCResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by john on 12/4/16.
 */
public abstract class WebAbstractRecommendModel extends WebCommonModel implements AbstractRecommendModel {
	@Override
	public void convertIDtoTitle(Collection<Integer> movieID, Consumer<List<String>> resultConsumer) {
		String query = getProperty("TRANSLATE_IDS_TO_NAMES");
		StringBuilder idList = new StringBuilder();
		for (Integer movie_id : movieID) {
			idList.append(movie_id).append(',');
		}
		String idListWithExtraComma = idList.toString();
		String formattedQuery = String.format(query, idListWithExtraComma.substring(0, idListWithExtraComma.length() - 1));
		storageService.runSELECTquery(formattedQuery, this::convertIDtoTitleResponse, null, Arrays.asList(resultConsumer, movieID));
	}
	
	public void convertIDtoTitleResponse(JDBCResult result) {
		if (result.getOriginalQuery().getAdditionalArgs().isPresent()) {
			List<Object> argsArray = (List<Object>) result.getOriginalQuery().getAdditionalArgs().get();
			Consumer<List<String>> resultConsumer = (Consumer<List<String>>) argsArray.get(0);
			Collection<Integer> movieID = (Collection<Integer>) argsArray.get(1);
			if (result.getResult().isPresent()) {
				DatabaseTable databaseTable = result.getResult().get();
				
			}
			
		}
	}
}
