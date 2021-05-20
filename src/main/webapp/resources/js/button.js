
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