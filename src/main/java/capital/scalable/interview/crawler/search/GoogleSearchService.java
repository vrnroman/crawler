package capital.scalable.interview.crawler.search;

import capital.scalable.interview.crawler.search.SearchService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Knows how to use google parseLinks and how to find links in result page.
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public class GoogleSearchService implements SearchService {

    @Override
    public String buildSearchRequest(String searchTerm, int limit) {
        return new StringBuilder()
                .append("https://www.google.com/search?q=")
                .append(searchTerm)
                .append("&num=")
                .append(limit).toString();
    }

    @Override
    public Elements selectElementsFromSearchResultPage(Document document) {
        return document.select("div[class='g']  div[class='s'] cite[class='_Rm']");
    }

    @Override
    public String mapDomElementToUrlString(Element element) {
        String url=  element.html();
        //TODO: sometimes google provides links without protocol, don't know why
        if (!url.startsWith("http:") || !url.startsWith("https:")) {
            url = "http://" + url;
        }
        return url;
    }
}
