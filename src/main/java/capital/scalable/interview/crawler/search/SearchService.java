package capital.scalable.interview.crawler.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface to any parseLinks service.
 * Provides functionality to create parseLinks query and parse links from result.
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public interface SearchService {

    /**
     * Create parseLinks query and returns links to relevant pages.
     * @param searchTerm search string
     * @param limit how many links would be in result page
     * @return list of urls to result pages
     * @throws IOException network troubles
     */
   default List<String> parseLinks(String searchTerm, int limit) throws IOException  {
       searchTerm = encodeSearchTerm(searchTerm);
       String url = buildSearchRequest(searchTerm, limit);
       Document document = Jsoup.connect(url).get();
       return parseLinks(document);
    }

    /**
     * Parses links to relevant pages from parseLinks result page.
     * @param page Search result page
     * @return list of urls to result pages
     */
    default List<String> parseLinks(Document page) {
       Elements results = selectElementsFromSearchResultPage(page);
       return results.stream()
               .map(this::mapDomElementToUrlString)
               .collect(Collectors.toList());
    }

    /**
     * Encodes search term for request. For example: spaces replace to +.
     * @param searchTerm search term
     * @return correct term for search engine
     * @throws UnsupportedEncodingException
     */
    default String encodeSearchTerm(String searchTerm) throws UnsupportedEncodingException {
       return URLEncoder.encode(searchTerm, StandardCharsets.UTF_8.toString());
    }

    /**
     * Creates url for search engine with all needed parameters and encoding
     * @param searchTerm search string
     * @param limit how many links would be in result page
     * @return final url for search engine
     */
    String buildSearchRequest(String searchTerm, int limit);

    /**
     * Rules for parsing search result page and finding links to result web pages.
     * @param document HTML document
     * @return List of HTML elements with result links.
     */
    Elements selectElementsFromSearchResultPage(Document document);

    /**
     * Converts HTML element to correct link.
     * @param element HTML element
     * @return link to result page
     */
    String mapDomElementToUrlString(Element element);
}
