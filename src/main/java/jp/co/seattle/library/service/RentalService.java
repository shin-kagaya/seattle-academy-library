package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 書籍貸出サービス
 * 
 *  rentalテーブルに関する処理を実装する
 */
@Service
public class RentalService {
    final static Logger logger = LoggerFactory.getLogger(RentalService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍IDに紐づく書籍を貸し出す
     *
     * @param bookId 書籍ID
     */
    public void rentalBook(int bookId) {

        String sql = "INSERT INTO rental (book_id, rent_date) VALUES ("               
                + bookId + ",sysdate())";

        jdbcTemplate.update(sql);
    }

    /**
     * rentalテーブルに書籍IDがあるかどうか調べる
     *
     * @param bookId 書籍ID
     * @return IDあれば1、なければ0
     */
    public int checkStatus(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT EXISTS(SELECT * FROM rental WHERE book_id = " + bookId + ") AS bookcheck";

        int number = jdbcTemplate.queryForObject(sql, Integer.class);

        return number;
    }

    /**
     * 書籍IDに紐づく書籍を返却する
     * 
     * @param bookId 書籍ID
     */
    public void returnBook(int bookId) {
        String sql = "DELETE FROM rental WHERE book_id=" + bookId + ";";
        jdbcTemplate.update(sql);
    }

}
