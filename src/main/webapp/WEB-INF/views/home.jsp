<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>ホーム｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="resources/js/button.js"></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <h1>Home</h1>
        <a href="<%=request.getContextPath()%>/addBook" class="btn_add_book">書籍の追加</a> 
        <a href="<%=request.getContextPath()%>/bulkRegist" class="btn_bulk_book">一括登録</a>
        <a href="<%=request.getContextPath()%>/rentOkBook" class="btn_rent_ok_book">貸出可能書籍一覧</a>
        <form class="search" id="search" action="<%=request.getContextPath()%>/searchBooks" method="post">
            <ul class="podition">
                <li><input type="radio" name="searchColumn" value="title" checked> 書籍名 <input type="radio" name="searchColumn" value="author"> 著者名</li>
                <li><input type="radio" name="searchStyle" value="part" checked> 部分一致 <input type="radio" name="searchStyle" value="perfect"> 完全一致</li>
            </ul>
            <input id="sbox" name="searchWord" type="text" placeholder="検索ワードを入力" />
            <button type="submit" id="sbtn" value="検索" disabled>検索</button>
        </form>
        <div class="content_body">            
            <div class="error_msg">           
                ${resultMessage}
                ${searchResultMessage}
                ${rentBookMessage}
            </div>
            <div class="booklist">
                <c:forEach var="bookInfo" items="${bookList}">
                    <div class="books">
                        <form method="post" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                            <a href="javascript:void(0)" onclick="this.parentNode.submit();"> 
                                <c:if test="${bookInfo.thumbnail == 'null'}">
                                    <img class="book_noimg" src="resources/img/noImg.png">
                                </c:if>
                                <c:if test="${bookInfo.thumbnail != 'null'}">
                                    <img class="book_img" src="${bookInfo.thumbnail}">
                                </c:if>
                            </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                        </form>
                        <ul>
                            <li class="book_title">${bookInfo.title}</li>
                            <li class="book_author">${bookInfo.author}</li>
                            <li class="book_publisher">${bookInfo.publisher}</li>
                            <li class="book_publish_date">${bookInfo.publishDate}</li>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
</body>
</html>
