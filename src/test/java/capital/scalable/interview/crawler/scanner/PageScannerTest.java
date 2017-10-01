package capital.scalable.interview.crawler.scanner;

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
 * Test for PageScanner.
 * Created by Roman Velichkin <vrnroman@gmail.com>.
 */
public class PageScannerTest {

    /**
     * There are 11 simply included js scripts on mercedes web page.
     * TODO: 12 scripts there in reality. gtm.js included too, but another way: in js-block via "insertBefore" function
     *
     * @throws IOException
     */
    @Test
    public void FindJsOnMercedesPageTest() throws IOException {
        Document page = createDocumentFromFile("mercedes.html");
        List<String> jsLibs = PageScanner.findJsLibs(page);
        Assert.assertEquals(11, jsLibs.size());
        Assert.assertTrue(jsLibs.contains("jquery-1.11.1.min.js"));
    }

    private static Document createDocumentFromFile(String filename) throws IOException {
        URL url = PageScannerTest.class.getClassLoader().getResource(filename);
        File file = new File(url.getPath());
        return Jsoup.parse(file, StandardCharsets.UTF_8.toString());
    }
}
