package capital.scalable.interview.crawler;

import capital.scalable.interview.crawler.scanner.PageScanner;
import capital.scalable.interview.crawler.search.GoogleSearchService;
import capital.scalable.interview.crawler.search.SearchService;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Entry point.
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public class WebCrawlerApp {


    // Number of pages tyo analyze
    public static final int SEARCH_RESULTS_LIMIT = 30;

    // Number of scripts to print
    public static final int TOP_SCRIPTS_LIMIT = 5;

    public static void main(String... args) {
        System.out.println("Hi! Please, write search term and press enter:");
        Scanner in = new Scanner(System.in);
        String searchTerm = in.nextLine();
        System.out.println("Good choice. Wait a moment, please");
        try {
            long start = System.nanoTime();
            Crawler crawler = new Crawler();
            crawler.analyzeSearchResultsByQuery(searchTerm, SEARCH_RESULTS_LIMIT);
            List<Pair<String, Long>> sortedResults = crawler.getTopMostlyUsedJsScripts(TOP_SCRIPTS_LIMIT);
            System.out.println(String.format("Process time = %s ms. The mostly used js scripts are:",
                    (System.nanoTime() - start) / 1_000_000));
            sortedResults.stream().forEach(System.out::println);
        } catch (Exception ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }

    }

}
