package capital.scalable.interview.crawler.search;

import capital.scalable.interview.crawler.scanner.PageScannerTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public class GoogleSearchServiceTest {

    private GoogleSearchService searchService = new GoogleSearchService();

    @Test
    public void noLinksTest() throws IOException {
        Document page = createDocumentFromFile("empty.html");
        List<String> urlsInResultList = searchService.parseLinks(page);
        Assert.assertTrue(urlsInResultList.isEmpty());
    }

    @Test
    public void eightLinksTest() throws IOException {
        Document page = createDocumentFromFile("java-request.html");
        List<String> urlsInResultList = searchService.parseLinks(page);
        Assert.assertEquals(8, urlsInResultList.size());
    }


    private static Document createDocumentFromFile(String filename) throws IOException {
        URL url = PageScannerTest.class.getClassLoader().getResource(filename);
        File file = new File(url.getPath());
        return Jsoup.parse(file, StandardCharsets.UTF_8.toString());
    }
}
