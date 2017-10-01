package capital.scalable.interview.crawler;

import capital.scalable.interview.crawler.scanner.PageScanner;
import capital.scalable.interview.crawler.search.GoogleSearchService;
import capital.scalable.interview.crawler.search.SearchService;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Crawler. Could execute search requests and analyze results.
 * Stateful by design.
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public class Crawler {

    //Comparator for Pairs of [scriptsName, frequency] with REVERSE order
    private static final Comparator<Pair<String, Long>> JS_LIBS_COMPARATOR =
            (o1, o2) -> Stream.<Supplier<Integer>>of(
                            () -> Long.compare(o2.getRight(), o1.getRight()), //DESC
                            () -> String.CASE_INSENSITIVE_ORDER.compare(o2.getLeft(), o1.getLeft())) //ASC
                    .map(Supplier::get) // execute lambda
                    .filter(element -> !element.equals(0)) // if compare == 0 should execute next lambda
                    .findFirst()
                    .orElseGet(() -> 0);

    //TODO: injecting would be nice
    private SearchService searchService;

    //Holds links to results of last query
    private List<String> lastSearchResultLinks;

    //Frequency map of last query
    private Map<String, Long> jsLibsFrequencyMap;

    /**
     * Creates Crawler with default search engine.
     */
    public Crawler() {
        this(new GoogleSearchService());
    }

    /**
     * Creates Crawler with spicified search engine.
     * @param searchService
     */
    public Crawler(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Executes search and analyze results (creates frequency map of included js scripts to top search result pages).
     * @param searchTerm search term
     * @param limit number of pages in search result list
     * @throws IOException net problems
     */
    public void analyzeSearchResultsByQuery(String searchTerm, int limit) throws IOException {
        lastSearchResultLinks = searchService.parseLinks(searchTerm, limit);
        jsLibsFrequencyMap = lastSearchResultLinks
                .parallelStream() // net operations below should be executed in parallel threads
                .flatMap(page -> PageScanner.findJsLibs(page).stream()) //convert page to scripts from this page
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); //select script, count group by script
    }

    /**
     * Sorts js frequency map (reverse mode) and returns top results.
     * @param limit number of results
     * @return list of pairs of js script name and it frequency in reverse order
     */
    public List<Pair<String, Long>> getTopMostlyUsedJsScripts(int limit) {
        if (limit < 0) {
            return Collections.EMPTY_LIST;
        }
        return jsLibsFrequencyMap.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue())) //convert to convenient structure
                .sorted(JS_LIBS_COMPARATOR) //sorts in reverse order
                .limit(limit)
                .collect(Collectors.toList());
    }
}
