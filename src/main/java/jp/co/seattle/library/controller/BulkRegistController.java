package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkRegistController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String bulkRegist(Model model) {
        return "bulkRegist";
    }

    /**
     * 書籍情報を一括登録する
     * @param file CSVファイル
     */
    @Transactional
    @RequestMapping(value = "/bulkBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String insertBook(Locale locale,
            @RequestParam("csvFile") MultipartFile file,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);
        try (InputStream stream = file.getInputStream();
            Reader reader = new InputStreamReader(stream);
                BufferedReader buf = new BufferedReader(reader);) {
            List<BookDetailsInfo> lines = new ArrayList<BookDetailsInfo>();
            List<String> errorMessages = new ArrayList<String>();
            int count = 1;

            //一行分のCSVファイルを入れる変数
            String line = null;

            //ループを書きます
            while ((line = buf.readLine()) != null) {
                String[] bookData = line.split(",", -1);
                BookDetailsInfo bookDetailsInfo = new BookDetailsInfo();
                boolean existsError = false;

                bookDetailsInfo.setDescription(bookData[5]);

                if (StringUtils.isEmpty(bookData[0]) | StringUtils.isEmpty(bookData[1])
                        | StringUtils.isEmpty(bookData[2])
                        | StringUtils.isEmpty(bookData[3])) {
                    existsError = true;
                }

                bookDetailsInfo.setTitle(bookData[0]);
                bookDetailsInfo.setAuthor(bookData[1]);
                bookDetailsInfo.setPublisher(bookData[2]);

                try {
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    df.setLenient(false);
                    df.parse(bookData[3]);

                    bookDetailsInfo.setPublishDate(bookData[3]);

                } catch (ParseException p) {
                    existsError = true;
                }
                boolean isIsbn = bookData[4].matches("[0-9]{10}|[0-9]{13}|^$");
                if (!isIsbn) {
                    existsError = true;
                }

                bookDetailsInfo.setIsbn(bookData[4]);

                if (existsError) {
                    errorMessages.add(count + "行目の書籍情報登録でバリデーションエラー");
                }
                lines.add(bookDetailsInfo);
                count++;
            }
            if (!CollectionUtils.isEmpty(errorMessages)) {
                model.addAttribute("errorMessages", errorMessages);
                return "bulkRegist";
            }

            for (BookDetailsInfo oneBookData : lines) {
                booksService.registBook(oneBookData);
            }
            model.addAttribute("resultMessage", "登録完了");
            return "bulkRegist";
        } catch (IOException e) {
            return "bulkRegist";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "bulkRegist";
        }

    }

}