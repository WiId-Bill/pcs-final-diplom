import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> map = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {


        File[] pdfFiles = pdfsDir.listFiles();
        assert pdfFiles != null;
        for (File pdfFile : pdfFiles) {
            var doc = new PdfDocument(new PdfReader(pdfFile));
            int pagesCount = doc.getNumberOfPages();
            for (int i = 0; i < pagesCount; i++) {
                int currentPageNum = i + 1;
                var page = doc.getPage(currentPageNum);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");


                Map<String, Integer> frequency = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    frequency.put(word, frequency.getOrDefault(word, 0) + 1);
                }

                PageEntry pageEntry;
                List<PageEntry> currentList;
                for (Map.Entry<String, Integer> pair : frequency.entrySet()) {
                    String key = pair.getKey();
                    Integer value = pair.getValue();
                    pageEntry = new PageEntry(pdfFile.getName(), currentPageNum, value);
                    currentList = new ArrayList<>();
                    if (map.containsKey(key)) {
                        currentList = map.get(key);
                    }
                    currentList.add(pageEntry);
                    map.put(key, currentList);
                    Collections.sort(currentList);
                }

            }
        }


    }


    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> result = map.get(word.toLowerCase());
        if (result != null) {
            return result;
        }

        return new ArrayList<>();

    }


}
