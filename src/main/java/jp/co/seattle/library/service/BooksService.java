package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id, title, author, publisher, publish_date, thumbnail_url from books order by title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 完全一致検索
     * 該当する書籍リストを取得する
     *
     * @param searchWord 検索ワード
     * @param searchColumn 検索カラム
     *
     * @return 書籍リスト
     */
    public List<BookInfo> perfectSearchGetBookList(String searchWord, String searchColumn) {

        List<BookInfo> perfectSearchGetedBookList = jdbcTemplate.query(
                "SELECT id, title, author, publisher, publish_date, thumbnail_url FROM books WHERE " + searchColumn
                        + " = '" + searchWord + "' ORDER BY title ASC",
                new BookInfoRowMapper());

        return perfectSearchGetedBookList;
    }

    /**
     * 部分一致検索
     * 該当する書籍リストを取得する
     * 
     * @param searchWord 検索ワード
     * @param searchColumn 検索カラム
     *
     * @return 書籍リスト
     */
    public List<BookInfo> partSearchGetBookList(String searchWord, String searchColumn) {

        List<BookInfo> partSearchGetedBookList = jdbcTemplate.query(
                "SELECT id, title, author, publisher, publish_date, thumbnail_url FROM books WHERE " + searchColumn
                        + " LIKE '%" + searchWord + "%' ORDER BY title ASC",
                new BookInfoRowMapper());

        return partSearchGetedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * booksに登録された最新の書籍IDを取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public int getBookId() {

        // 最新の書籍IDを取得する
        String sql = "SELECT MAX(id) FROM books";

        int maxId = jdbcTemplate.queryForObject(sql, Integer.class);

        return maxId;
    }

    /**
     * 書籍IDに紐づく書籍を削除する
     * 
     * @param bookId 書籍ID
     */
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE ID=" + bookId + ";";
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title, author, publisher, publish_date, description, isbn, thumbnail_name, thumbnail_url, reg_date, upd_date) VALUES ('"
                + bookInfo.getTitle() + "','"
                + bookInfo.getAuthor() + "','"
                + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getDescription() + "','"
                + bookInfo.getIsbn() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "',"
                + "sysdate(),"
                + "sysdate())";

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍情報を更新する
     *
     * @param bookInfo 書籍情報
     */
    public void updateBook(BookDetailsInfo bookInfo) {

        //シングル→SQLでカラムのデータ型が文字列の時
        //ダブル→Javaで文字列として認識させたい
        //＋→文字列の中で変数を変数として認識させたい時
        String sql = "UPDATE books SET "
                + "TITLE='" + bookInfo.getTitle() + "',"
                + "AUTHOR= '" + bookInfo.getAuthor() + "',"
                + "PUBLISHER= '" + bookInfo.getPublisher() + "',"
                + "PUBLISH_DATE= '" + bookInfo.getPublishDate() + "',"
                + "THUMBNAIL_URL= '" + bookInfo.getThumbnailUrl() + "',"
                + "THUMBNAIL_NAME= '" + bookInfo.getThumbnailName() + "',"
                + "UPD_DATE= sysdate(),"
                + "DESCRIPTION= '" + bookInfo.getDescription() + "',"
                + "ISBN= '" + bookInfo.getIsbn() + "'"
                + "WHERE ID= " + bookInfo.getBookId();

        jdbcTemplate.update(sql);

    }
}
