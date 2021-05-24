
//貸出ステータスの<div>タグのclassをセレクタにしてreadyを使い、貸出ステータス表示を読みこむ
$(".lendingStatus_label").ready(function() {
	var rentStatus = $(".lendingStatus_label").text();
	var text = '貸出可'

	//読み込んだ貸出ステータス表示に'貸出可'が含まれているかチェック
	if (rentStatus.indexOf(text) > -1) {
		$("#return").prop("disabled", true);
	} else {
		$("#rent").prop("disabled", true);
		$("#edit").prop("disabled", true);
		$("#delete").prop("disabled", true);
	}
});

$(function() {
	//テキストボックス内にテキストが入力されたら読み込む
	$("#sbox").change(function() {
		$("#sbtn").prop("disabled", false);
	});
});


$(function() {
	//テキストボックス内にテキストが入力されたら読み込む
	$("#sbox").change(function() {
		var val = $(this).val().length;
		//入力されたテキストが1文字以上ある時検索ボタンを活性にする
		if (val == 0) {
			$("#sbtn").prop("disabled", true);
		}else
		$("#sbtn").prop("disabled", false);
	});
});
