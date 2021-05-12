package jp.co.seattle.library.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * 書籍編集コントローラー
 */
@Controller
public class EditController {
    final static Logger logger = LoggerFactory.getLogger(EditController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 編集画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return 遷移画面先
     */
    @Transactional
    @RequestMapping(value = "/editBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    public String editBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome editControler.java! The client locale is {}.", locale);

        model.addAttribute("bookEditInfo", booksService.getBookInfo(bookId));

        return "edit";
    }

    /**
     * 書籍情報を編集する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param publishDate 出版日
     * @param description 説明
     * @param isbn ISBN
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/editsBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String editsBook(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("description") String description,
            @RequestParam("isbn") String isbn,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome editsBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setDescription(description);
        bookInfo.setIsbn(isbn);
        bookInfo.setBookId(bookId);

        //バリデーションチェック
        if (StringUtils.isEmpty(title) | StringUtils.isEmpty(author) | StringUtils.isEmpty(publisher)
                | StringUtils.isEmpty(publishDate)) {
            model.addAttribute("emptyError", "必須項目に値を入力してください");
            return "edit";
        }

        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(publishDate);

        } catch (ParseException p) {
            model.addAttribute("dateError", "出版日は半角数字のYYYYMMDD形式で入力してください");
            return "edit";
        }

        boolean isIsbn = isbn.matches("[0-9]{10}|[0-9]{13}|^$");
        if (!isIsbn) {
            model.addAttribute("isbnError", "ISBNの桁数または半角数字が正しくありません");
            return "edit";
        }

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "edit";
            }

        } else {
            bookInfo.setThumbnailName(booksService.getBookInfo(bookId).getThumbnailName());
            bookInfo.setThumbnailUrl(booksService.getBookInfo(bookId).getThumbnailUrl());
        }

        // 書籍情報を更新する
        booksService.updateBook(bookInfo);

        model.addAttribute("resultMessage", "更新完了");

        // TODO 編集更新した書籍の詳細情報を表示するように実装
        model.addAttribute("bookDetailsInfo", bookInfo);

        //貸出ステータス表示
        model.addAttribute("rentOK", "貸出可");

        //  詳細画面に遷移する
        return "details";
    }

}
