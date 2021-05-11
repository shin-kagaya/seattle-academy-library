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
 * 書籍貸出コントローラー
 */
@Controller
public class RentBookController {
    final static Logger logger = LoggerFactory.getLogger(RentBookController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private RentalService rentalService;

    /**
     * 貸出処理をして詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return 遷移画面先
     */
    @Transactional
    @RequestMapping(value = "/rentBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    public String editBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome RentBookControler.java! The client locale is {}.", locale);

        //RentalServiceのrentalメソッドを呼んで貸出処理を行う
        rentalService.rentalBook(bookId);

        //貸し出した本の詳細情報を詳細画面に表示
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //貸出中表示をする、削除できないことを表示する
        model.addAttribute("rentNG", "貸出中");
        model.addAttribute("deleteNG", "貸出中の本は削除できません");

        return "details";
    }

}
