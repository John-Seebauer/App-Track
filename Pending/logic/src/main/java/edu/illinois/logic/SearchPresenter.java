package edu.illinois.logic;

import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.JDBCResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class SearchPresenter<V extends SearchView, M extends SearchModel> extends CommonPresenter<V, M>
		implements SearchView.ActionListener, SearchModel.ActionListener {
	private final static Logger logger = Logger.getLogger(SearchPresenter.class.getName());
	
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
		model.setActionListener(this);
	}
	
	@Override
	public String getProperty(String name) {
		return super.getProperty(name);
	}
	
	@Override
	public void initSearchrequst(String query) {
		model.runSELECTquery(String.format(model.getProperty("backend.GET_MOVIE"), query));
	}
	
	@Override
	public void rateMovie(Integer movieID, Double value, String name) {
		String updateQuery = model.getProperty("SAVE_RATING");
		String user = model.getUser();
		Integer rating = value.intValue();
		model.saveRating(String.format(updateQuery, user, movieID, rating, rating), Arrays.asList(rating, name));
	}
	
	@Override
	public void notifySELECTresponse(JDBCResult result) {
		if (result.getResult().isPresent()) {
			view.notifySELECTresponse(result.formatToContainer());
		}
	}
	
	@Override
	public void saveRatingSuccess(JDBCResult result) {
		String original =  result.getOriginalQuery().getQuery();
		String resultString = result.getResult().isPresent() ? String.valueOf(result.hadFailure()) : "unknown";
		
		logger.info(String.format("Received notification of UPDATE query's result: %s for\n\t%s\n\t", resultString, original));
		result.getOriginalQuery().getAdditionalArgs().ifPresent(array -> {
			Integer rating = (Integer) ((List<Object>) array).get(0);
			String title = (String) ((List<Object>) array).get(1);
			view.showNotification("Success!", String.format("Saved rating of %d for %s.", rating, title));
		});
	}
	
	@Override
	public void saveRatingFailure(JDBCResult result) {
		String query = " for query: " + result.getOriginalQuery().getQuery();
		
		view.queryFailedCleanup();
		
		if (!result.hadFailure()) {
			if (result.getException().isPresent()) {
				view.showAndLogError(logger, Level.WARNING, "JDBCResult" + query +
						" failed but was not marked as such.", result.getException().get());
			} else {
				logger.log(Level.WARNING, "JDBCResult" + query + " failed but was not marked as such.");
				view.showMessage("JDBCResult" + query + " failed but was not marked as such.");
			}
		} else {
			if (result.getException().isPresent()) {
				view.showAndLogError(logger, Level.WARNING, "JDBCResult" + query + " failed.", result.getException().get());
			} else {
				logger.log(Level.WARNING, "JDBCResult" + query + " failed.");
				view.showMessage("JDBCResult" + query + " failed.");
			}
		}
	}
	
	@Override
	public void notifyGenreAndPlot(Collection<String> genres, Collection<String> plots, DatabaseEntry selected) {
		String genre = genres.isEmpty() ? "Unknown" : genres.iterator().next();
		String plot = plots.isEmpty() ? "Unknown plot" : plots.iterator().next();
		view.displayRatingWindow(selected.getAttribute("title", String.class), genre, plot,
				selected.getAttribute("movie_id", Integer.class));
	}
	
	@Override
	public void initRatingWindow(DatabaseEntry selected) {
		Integer movie_id = selected.getAttribute("movie_id", Integer.class);
		model.getGenreAndPlotForMovie(String.format(model.getProperty("GENRE_FOR_MOVIE"), movie_id),
				String.format(model.getProperty("PLOT_FOR_MOVIE"), movie_id), selected);
	}
	
	
}
