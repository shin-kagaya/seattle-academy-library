package jp.co.seattle.library.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HomeController {
    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * Homeボタンからホーム画面に戻るページ
     * @param model
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String transitionHome(Model model) {
        model.addAttribute("bookList", booksService.getBookList());
        return "home";
    }

    /**
    * 書籍を検索する
    * @param searchWord  検索ワード
    * @param searchColumn  検索カラム
    * @param searchStyle  検索方法
    */
    @Transactional
    @RequestMapping(value = "/searchBooks", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String searchBook(Locale locale,
            @RequestParam("searchWord") String searchWord,
            @RequestParam("searchColumn") String searchColumn,
            @RequestParam("searchStyle") String searchStyle,
            Model model) {
        logger.info("Welcome searchBooks.java! The client locale is {}.", locale);

        //検索で得られる書籍リストを格納するリストを宣言
        List<BookInfo> searchResult = new ArrayList<BookInfo>();
        
        //検索方法が完全一致の時、部分一致の時の条件分岐
        if (searchStyle.equals("perfect")) {
            searchResult = booksService.perfectSearchGetBookList(searchWord, searchColumn);
        } else {
            searchResult = booksService.partSearchGetBookList(searchWord, searchColumn);
        }
        //リストに入っている書籍の数で条件分岐
        if(searchResult.size() == 0){
            model.addAttribute("searchResultMessage", "該当する書籍が見つかりませんでした");
        }else {
            model.addAttribute("searchResultMessage", "該当する書籍が"+searchResult.size()+"冊見つかりました");
        }
        
        model.addAttribute("bookList", searchResult);
        return "home";
    }

    /**
     * 貸出可能書籍一覧を取得する
     * @param model
     * @return 遷移画面先
     */
    @RequestMapping(value = "/rentOkBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {

        //貸出可能書籍を取得するメソッドを呼び出してリストに代入
        List<BookInfo> getRentOkBooks = booksService.getRentOkBookList();
        //リストが空のとき、それ以外で条件分岐
        if (CollectionUtils.isEmpty(getRentOkBooks)) {
            model.addAttribute("rentBookMessage", "貸出可能な書籍がありません");
        }else {
            model.addAttribute("rentBookMessage", "貸出可能な書籍はこちらです");
        }
        model.addAttribute("bookList", getRentOkBooks);
        return "home";
    }
}
