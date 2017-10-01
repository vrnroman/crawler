package capital.scalable.interview.crawler.scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class, that helps to parse html page and detect included js-scripts.
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public class PageScanner {

    // CSS selector for tag <script> with non empty "src" attribute
    private static final String SCRIPT_WITH_SRC_SELECTOR = "script:not([src=''])";

    /**
     * Find included js-scripts in webpage.
     * @param url Web page url
     * @return List of the scriptnames
     */
    public static List<String> findJsLibs(String url) {
        try {
            Document page = Jsoup.connect(url).get();
            return findJsLibs(page);
        } catch (IOException e) {
            //it's Ok, if page doesn't exist or unavailable in this moment
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Find included js-scripts in html document.
     * @param page HTML document
     * @return List of the scriptnames
     */
    public static List<String> findJsLibs(Document page) {
        Elements elements = page.select(SCRIPT_WITH_SRC_SELECTOR);
        return elements.stream()
                .map(element -> element.attr("src"))
                .map(scriptName -> scriptName.replaceAll("\\?.*", "")) //request parameters doesn't matter
                .map(scriptName -> scriptName.substring(scriptName.lastIndexOf('/') + 1)) //only names, without path
                .filter(scriptName -> scriptName.toLowerCase().endsWith(".js")) //only js (not .php for example)
                .collect(Collectors.toList());
    }
}
