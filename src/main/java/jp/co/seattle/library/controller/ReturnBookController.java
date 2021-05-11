package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalService;

/**
 * 書籍返却コントローラー
 */
@Controller
public class ReturnBookController {
    final static Logger logger = LoggerFactory.getLogger(ReturnBookController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private RentalService rentalService;

    /**
     * 返却処理をして詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return 遷移画面先
     */
    @Transactional
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    public String editBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome ReturnBookControler.java! The client locale is {}.", locale);

        //RentalServiceのreturnBookメソッドを呼んで返却処理を行う
        rentalService.returnBook(bookId);

        //返却した本の詳細情報を詳細画面に表示
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //貸出可表示をする
        model.addAttribute("rentOK", "貸出可");

        return "details";
    }

}
