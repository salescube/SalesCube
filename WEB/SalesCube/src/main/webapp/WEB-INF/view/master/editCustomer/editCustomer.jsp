<%@page import="jp.co.arkinfosys.common.CategoryTrns"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　顧客マスタ管理（登録・編集）</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	$(function() {
		applyPriceAlignment();
	});

    var maxCount = 0;
    var zipToAddress = null;
    var addressToZip = null;

    function init() {
    	var obj = $("#selectedDelivery").get(0);
    	if($("#selectedDeliveryIndex").val() == "") {
            if (obj.options.length > 1) {       // 納入先が入っていたら先頭要素を選択させる
                obj.selectedIndex = 1;
            }
    	}
    	else {
        	var index = new Number($("#selectedDeliveryIndex").val());
        	obj.selectedIndex = index;
    	}
    	maxCount = obj.options.length - 1;

        // 納入先・敬称のデフォルト値保存
        $("#defaultPcPreCategory").val($("#deliveryRow_0PcPreCategory").val());

    	//桁適用
    	_after_load($(".numeral_commas"));

        changeDelivery(true);   // 納入先プルダウン選択イベントを発動

        // 納入先は共通処理ではENTERキーフォーカス移動設定が十分ではないため独自に設定
		var target = $("#delivery_table").find(":input[type!='hidden'][type!='file'][type!='reset'][type!='submit'][type!='button'][type!='image'][type!='textarea']");
		if (target.size() > 0) {
			target.unbind('keypress');
			target.bind('keypress', 'return', move_focus_to_next_tabindex );
		}

        createZipCodeAndAddressIdMap();

        $("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
    }

    /**
     * 画面内の郵便番号と住所1の組を取得し、相互に変換できるidのハッシュを作成する
     */
	function createZipCodeAndAddressIdMap() {
	    zipToAddress = new Object();
	    addressToZip = new Object();

	    // 画面内の郵便番号と住所1のリストを作成してid順でソートする
        var zipCodeElementList = $("input[id$='ZipCode']");
        var address1ElementList = $("input[id$='Address1']");
        var comp = function(a, b) {
       		if(a.id > b.id) {
           		return -1;
       		}
       		else {
           		return 1;
       		}
   		};
        zipCodeElementList.sort(comp);
        address1ElementList.sort(comp);

        for(var i = 0; i < zipCodeElementList.size(); i++) {
            zipToAddress[ zipCodeElementList.get(i).id ] = address1ElementList.get(i).id;
            addressToZip[ address1ElementList.get(i).id ] = zipCodeElementList.get(i).id;
        }
	}

    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerCustomer(); }
    function onF4() { deleteCustomer(); }
    function onF5() { openMasterDefaultSettingDialog('master', 'CUSTOMER_MST'); }
    function onF6() { importCSV(); }
    function onF8() { outputHistory();}

	// 履歴出力
	function outputHistory(){
		// この内容でエクセル出力しますか？
		if(!confirm('<bean:message key="confirm.history" />')){
			return;
		}

		// 出力を実行する
		// Ajaxリクエストによって変更履歴をロードする
		var data = new Object();
		data["customerCode"] = $("#customerCode").val();
		data["customerName"] = $("#customerName").val();

		asyncRequest(
			contextRoot + "/ajax/outputCustomerHistAjax/prepare",
			data,
			function(data) {
				$("#errors").empty();
				window.open(contextRoot + "/ajax/outputCustomerHistAjax/excel",	"<bean:message key='words.name.excel'/>");
			},
			function(xmlHttpRequest, textStatus, errorThrown) {
				if (xmlHttpRequest.status == 450) {
					// 検索条件エラー
					$("#errors").empty();
					$("#errors").append(xmlHttpRequest.responseText);
				} else if (xmlHttpRequest.status == 401) {
					// 未ログイン
					alert(xmlHttpRequest.responseText);
					window.location.doHref(contextRoot + "/login");
				} else {
					// その他のエラー
					alert(xmlHttpRequest.responseText);
				}
			}
		);
	}

    function setZipCode(id, map) {
        $("#" + id + "ZipCode").val(map["zipCode"]);
        $("#" + id + "Address1").val(map["zipAddress1"]);
        $("#" + id + "Address2").val(map["zipAddress2"]);
    }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
    		location.doHref(contextRoot + "/master/editCustomer/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.customer.back'/>")){
            location.doHref(contextRoot + "/master/searchCustomer/");
        }
    }

    function registerCustomer() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
    		_before_submit($(".numeral_commas"));
        	$("#editCustomerForm").attr("action", "${f:url('/master/editCustomer/insert')}");
        </c:if>
        <c:if test="${editMode}">

        var confirmResult;
       	confirmResult = confirm("<bean:message key='confirm.update'/>");
    	if(confirmResult){
    		_before_submit($(".numeral_commas"));
        	$("#editCustomerForm").attr("action", "${f:url('/master/editCustomer/update')}");
        </c:if>
        $("#editCustomerForm").submit();
        }
    }

    function deleteCustomer() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
    		_before_submit($(".numeral_commas"));
        	$("#editCustomerForm").attr("action", "${f:url('/master/editCustomer/delete')}");
        	$("#editCustomerForm").submit();
        }
    }

    function changeDelivery(notClear) {
        hideAllDelivery();
        var obj = $("#selectedDelivery").get(0);
        showDelivery(obj.selectedIndex, notClear);
        $("#selectedDeliveryIndex").val(obj.selectedIndex);
    }

    function clearNewDeliveryRow() {
        $("#deliveryRow_0Name").val("");
        $("#deliveryRow_0Kana").val("");
        $("#deliveryRow_0OfficeName").val("");
        $("#deliveryRow_0OfficeKana").val("");
        $("#deliveryRow_0DeptName").val("");
        $("#deliveryRow_0ZipCode").val("");
        $("#deliveryRow_0Address1").val("");
        $("#deliveryRow_0Address2").val("");
        $("#deliveryRow_0PcName").val("");
        $("#deliveryRow_0PcKana").val("");
        $("#deliveryRow_0PcPreCategory").val($("#defaultPcPreCategory").val());
        $("#deliveryRow_0Tel").val("");
        $("#deliveryRow_0Fax").val("");
        $("#deliveryRow_0Email").val("");
    }

    function showDelivery(index, notClear) {
        if (index == 0) {
            if(!notClear) {
            	clearNewDeliveryRow();
            }
            $("#addb").removeAttr("disabled");
            $("#delb").attr("disabled", "disabled");
        } else {
            $("#addb").attr("disabled", "disabled");
            $("#delb").removeAttr("disabled");
        }
        var tabindex = 604;
        for (var j = 1; j <= 6; j++) {
            var trObj = $("#deliveryRow_" + index + "_" + j);
            trObj.show();
            trObj.find("input, select").each(
            	function() {
                	$(this).attr("tabindex", tabindex++);
            	}
            );
        }
    }

    function removeDelivery(index) {
        for (var j = 1; j <= 6; j++) {
            var trObj = $("#deliveryRow_" + index + "_" + j);
            trObj.remove();
        }
    }

    function searchZipCode(arg) {
    	var id = arg;
        if(arg.data) {
            // 納入先の場合
            id = arg.data.id;
        }
        openSearchZipDialog(id, setZipCode);
        $("#" + id + "_zipCode").val($("#" + id + "ZipCode").val());
        $("#" + id + "_zipAddress1").val($("#" + id + "Address1").val());
    }

	function searchZipCodeDirect(id) {
		// 入力チェック
		var val = $("#" + id + "ZipCode").val();
		if (!val) {
			return;
		}

		// 該当箇所のエラーをクリア
		var type = null;
		var message = null;
		if(id.indexOf("delivery") == 0) {
			// 納入先住所
			type = "delivery";
			message = "<bean:message key="warns.zipcode.notidentical" arg0="納入先"/>";
		}
		else if(id.indexOf("billTo") == 0) {
			// 請求先住所
			type = "billTo";
			message = "<bean:message key="warns.zipcode.notidentical" arg0="請求先"/>";
		}
		else {
			// 顧客住所
			type = "customer";
			message = "<bean:message key="warns.zipcode.notidentical" arg0="顧客"/>";
		}
		$("#" + type + "_errors").empty();

		// 検索実行
		var data = {"zipCode" : val};
		asyncRequest(contextRoot + "/ajax/master/searchZipCodeAjax/search", data,
			function(data) {
				var results = eval(data);
				if (results.length!=1) {
					$("#" + type + "_errors").append(document.createTextNode(message));
				}
				else {
					$("#" + id + "ZipCode").val(results[0].zipCode);
					$("#" + id + "Address1").val(results[0].zipAddress1);
					$("#" + id + "Address2").val(results[0].zipAddress2);
				}
			}
		);
	}

    function hideAllDelivery() {
        for (var i=0; i <= maxCount;i++) {
            for (var j = 1; j <= 6; j++) {
                var trObj = $("#deliveryRow_" + i + "_" + j);
                trObj.hide();
                trObj.find("input, select").each(
               		function() {
                   		$(this).attr("tabindex", "-1");
               		}
                );
            }
        }
    }

    function copyFromCustomer(idPrefix) {
        // 顧客名
        $("#" + idPrefix + "Name").val($("#customerName").val());

        // 顧客名カナ
        $("#" + idPrefix + "Kana").val($("#customerKana").val());

        // 事業所名
        $("#" + idPrefix + "OfficeName").val($("#customerOfficeName").val());

        // 事業所名カナ
        $("#" + idPrefix + "OfficeKana").val($("#customerOfficeKana").val());

        // 部署名
        $("#" + idPrefix + "DeptName").val($("#customerDeptName").val());

        // 郵便番号
        $("#" + idPrefix + "ZipCode").val($("#customerZipCode").val());

        // 住所１
        $("#" + idPrefix + "Address1").val($("#customerAddress1").val());

        // 住所２
        $("#" + idPrefix + "Address2").val($("#customerAddress2").val());

        // 担当者
        $("#" + idPrefix + "PcName").val($("#customerPcName").val());

        // 担当者カナ
        $("#" + idPrefix + "PcKana").val($("#customerPcKana").val());

        // 敬称
        $("#" + idPrefix + "PcPreCategory").val($("#customerPcPreCategory").val());

        // TEL
        $("#" + idPrefix + "Tel").val($("#customerTel").val());

        // Fax
        $("#" + idPrefix + "Fax").val($("#customerFax").val());

        // E-MAIL
        $("#" + idPrefix + "Email").val($("#customerEmail").val());
    }

    function copyCustomerToDelivery() {
        var selectObj = $("#selectedDelivery").get(0);
        var index = selectObj.selectedIndex;

        copyFromCustomer("deliveryRow_" + index);

    }

    function copyCustomerToBilling() {
        copyFromCustomer("billTo_delivery");
    }

    function addDelivery() {
        // 納入先名が空の場合は追加しない
        var elem = $("#deliveryRow_0Name");
        if(elem.val() == "") {
            var message = "<bean:message key='errors.required' arg0="##"/>";
            message = message.replace("##", "<bean:message key='labels.deliveryName'/>");
            alert(message);
            return;
        }

        // 同じ名称の納入先は許可しない
        var selectObj = $("#selectedDelivery").get(0);
        for(var i = 0; i < selectObj.options.length; i++) {
            if(elem.val() == selectObj.options[i].text) {
                alert("<bean:message key='errors.duplicate.deliveryName'/>");
                return;
            }
        }

        // 最大件数を＋１
        maxCount++;

        // １行目
        var trObj = $("#deliveryRow_0_1").clone(true);
        var tdObj = trObj.children(":first");
        var elem;

        // 情報を設定
        // 行ID
        trObj.attr("id", "deliveryRow_" + maxCount + "_1");

        // 納入先名
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryName");
        elem.attr("id", "deliveryRow_" + maxCount + "Name");

        // 次のセルへ
        tdObj = tdObj.next();

        // 納入先名カナ
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryKana");
        elem.attr("id", "deliveryRow_" + maxCount + "Kana");

        // 行追加
        var targetRow;
        if (maxCount == 1) {
            targetRow = $("#deliveryRow_0_6");
        } else {
            targetRow = $("#deliveryRow_" + (maxCount-1) + "_6");
        }
        targetRow.after(trObj);

        var prevTrObj = trObj;

        // 次の行へ
        trObj = $("#deliveryRow_0_2").clone(true);
        tdObj = trObj.children(":first");
        // 行ID
        trObj.attr("id", "deliveryRow_" + maxCount + "_2");

        // 事業所名
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryOfficeName");
        elem.attr("id", "deliveryRow_" + maxCount + "OfficeName");

        // 次のセルへ
        tdObj = tdObj.next();

        // 事業所名カナ
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryOfficeKana");
        elem.attr("id", "deliveryRow_" + maxCount + "OfficeKana");

        // 次のセルへ
        tdObj = tdObj.next();

        // 部署名
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryDeptName");
        elem.attr("id", "deliveryRow_" + maxCount + "DeptName");

        // 行追加
        prevTrObj.after(trObj);
        prevTrObj = trObj;

        // 次の行へ
        trObj = $("#deliveryRow_0_3").clone();
        tdObj = trObj.children(":first");
        // 行ID
        trObj.attr("id", "deliveryRow_" + maxCount + "_3");

        // 郵便番号
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryZipCode");
        elem.attr("id", "deliveryRow_" + maxCount + "ZipCode");
        elem.bind('keypress', 'return', move_focus_to_next_tabindex );

        // 虫眼鏡アイコン
        elem = elem.next();
        elem.attr("id", "deliveryRow_" + maxCount + "_3_icon");
        elem.bind("click", {"id":"deliveryRow_" + maxCount}, searchZipCode);

        // 次のセルへ
        tdObj = tdObj.next();

        // 住所１
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryAddress1");
        elem.attr("id", "deliveryRow_" + maxCount + "Address1");
        elem.bind('keypress', 'return', move_focus_to_next_tabindex );

        // 次のセルへ
        tdObj = tdObj.next();

        // 住所２
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryAddress2");
        elem.attr("id", "deliveryRow_" + maxCount + "Address2");
        elem.bind('keypress', 'return', move_focus_to_next_tabindex );

        // 行追加
        prevTrObj.after(trObj);
        prevTrObj = trObj;

        // 次の行へ
        trObj = $("#deliveryRow_0_4").clone(true);
        tdObj = trObj.children(":first");
        // 行ID
        trObj.attr("id", "deliveryRow_" + maxCount + "_4");

        // 担当者
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryPcName");
        elem.attr("id", "deliveryRow_" + maxCount + "PcName");

        // 次のセルへ
        tdObj = tdObj.next();

        // 担当者カナ
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryPcKana");
        elem.attr("id", "deliveryRow_" + maxCount + "PcKana");

        // 次のセルへ
        tdObj = tdObj.next();

        // 敬称
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // プルダウン
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryPcPreCategory");
        elem.attr("id", "deliveryRow_" + maxCount + "PcPreCategory");
        elem = elem.next(); // hiddenは削除
        elem.remove();

        // 行追加
        prevTrObj.after(trObj);
        prevTrObj = trObj;

        // 次の行へ
        trObj = $("#deliveryRow_0_5").clone(true);
        tdObj = trObj.children(":first");
        // 行ID
        trObj.attr("id", "deliveryRow_" + maxCount + "_5");

        // TEL
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryTel");
        elem.attr("id", "deliveryRow_" + maxCount + "Tel");

        // 次のセルへ
        tdObj = tdObj.next();

        // FAX
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryFax");
        elem.attr("id", "deliveryRow_" + maxCount + "Fax");

        // 行追加
        prevTrObj.after(trObj);
        prevTrObj = trObj;

        // 次の行へ
        trObj = $("#deliveryRow_0_6").clone(true);
        tdObj = trObj.children(":first");
        // 行ID
        trObj.attr("id", "deliveryRow_" + maxCount + "_6");

        // E-Mail
        tdObj = tdObj.next();       // thタグを読み飛ばす
        // テキストボックス
        elem = tdObj.children(":first");
        elem.attr("name", "deliveryList[" + (maxCount-1) + "].deliveryEmail");
        elem.attr("id", "deliveryRow_" + maxCount + "Email");

        // 行追加
        prevTrObj.after(trObj);


        // １行増やす
        selectObj.options.length = selectObj.options.length + 1;
        var index = selectObj.options.length-1;
        selectObj.options[index].value = "";        // 納入先コードは設定不要
        selectObj.options[index].text = $("#deliveryRow_" + maxCount + "Name").val();

        // 選択状態にする
        selectObj.selectedIndex = index;
        // 表示状態を切り替える
        changeDelivery();

        createZipCodeAndAddressIdMap();
    }

    function resetDeliveryID(index) {
        // 削除によりＩＤを１減らす
        var srcID = index+1;
        var destID = index;
        var elem

        // １行目
        var trObj = $("#deliveryRow_" + srcID + "_1")
        // 行ID
        trObj.attr("id", "deliveryRow_" + destID + "_1");

        // 納入先名
        var suffix = "Name";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 納入先名カナ
        suffix = "Kana";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // ２行目
        var trObj = $("#deliveryRow_" + srcID + "_2")
        // 行ID
        trObj.attr("id", "deliveryRow_" + destID + "_2");

        // 事業所名
        suffix = "OfficeName";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 事業所名カナ
        suffix = "OfficeKana";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 部署名
        suffix = "DeptName";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // ３行目
        var trObj = $("#deliveryRow_" + srcID + "_3")
        // 行ID
        trObj.attr("id", "deliveryRow_" + destID + "_3");

        // 郵便番号
        suffix = "ZipCode";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 虫眼鏡アイコン
        elem = elem.next();
        elem.attr("id", "deliveryRow_" + destID + "_3_icon");
        elem.bind("click", {"id":"deliveryRow_" + (destID-1)}, searchZipCode);

        // 住所１
        suffix = "Address1";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 住所２
        suffix = "Address2";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // ４行目
        var trObj = $("#deliveryRow_" + srcID + "_4")
        // 行ID
        trObj.attr("id", "deliveryRow_" + destID + "_4");

        // 担当者
        suffix = "PcName";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 担当者カナ
        suffix = "PcKana";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // 敬称
        suffix = "PcPreCategory";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // ５行目
        var trObj = $("#deliveryRow_" + srcID + "_5")
        // 行ID
        trObj.attr("id", "deliveryRow_" + destID + "_5");

        // TEL
        suffix = "Tel";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // FAX
        suffix = "Fax";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);

        // ５行目
        var trObj = $("#deliveryRow_" + srcID + "_6")
        // 行ID
        trObj.attr("id", "deliveryRow_" + destID + "_6");

        // E-Mail
        suffix = "Email";
        elem = $("#deliveryRow_" + srcID + suffix);
        elem.attr("id", "deliveryRow_" + destID + suffix);
        elem.attr("name", "deliveryList[" + (destID-1) + "].delivery" + suffix);
    }

    function deleteDelivery() {
        // プルダウンから削除
        var selectObj = $("#selectedDelivery").get(0);
        var orgSize = selectObj.options.length;
        var index = selectObj.selectedIndex;
        selectObj.remove(index);

        // 納入先情報を削除
        removeDelivery(index);

        // IDを付け替える
        for (var i = index; i < orgSize; i++) {
            resetDeliveryID(i);
        }

        // 件数を１減らす
        maxCount--;

        // 選択
        if (selectObj.options.length > 1) {
            selectObj.selectedIndex = 1;
        } else {
            selectObj.selectedIndex = 0;
        }

        // 表示状態を切り替える
        changeDelivery();

        createZipCodeAndAddressIdMap();
    }

	function applyPriceAlignment() {
		if($("#priceFractCategory").val()) {
			// 円単価
			$(".BDCyen").setBDCStyle( $("#priceFractCategory").val() ,0 ).attBDC();
		}
	}

	/**
	 * 顧客名カナの変更に応じて振り込み名義に変換する
	 */
	function convertPaymentName(customerKana) {
		if(customerKana.length == 0) {
			$("#paymentName").val("");
			return;
		}

		var data = new Object();
		data[ "customerKana" ] = customerKana;

		asyncRequest(
				contextRoot + "/ajax/convertPaymentNameAjax/convert",
				data,
				function(paymentName){
					$("#paymentName").val(paymentName);
				}
			);
	}

	/**
	 * 郵便番号と住所の一致をチェックし、不一致であればメッセージを表示する
	 */
	function checkZipCodeAndAddress(element) {
		var tempId = zipToAddress[ element.id ];

		var zipCodeId = null;
		var address1Id = null;
		if( tempId == null ) {
			zipCodeId = addressToZip[ element.id ];
			address1Id = element.id;
		}
		else {
			zipCodeId = element.id;
			address1Id = zipToAddress[ element.id ];
		}

		var zipCode = $("#" + zipCodeId);
		var address1 = $("#" + address1Id);

		var type = null;
		var message = null;
		if(zipCodeId.indexOf("delivery") == 0) {
			var regex = new RegExp("^deliveryRow_([0-9]+)ZipCode$");
			var match = zipCodeId.match(regex);
			// 納入先住所
			if(match == null || match[1] == "0") {
				message = "<bean:message key="warns.zipcode.address.mismatch" arg0="納入先"/>";
			}
			else {
				message = "<bean:message key="warns.line.zipcode.address.mismatch" arg0="NUM" arg1="納入先"/>";
				message = message.replace("NUM", match[1]);
			}
		}
		else if(zipCodeId.indexOf("billTo") == 0) {
			// 請求先住所
			message = "<bean:message key="warns.zipcode.address.mismatch" arg0="請求先"/>";
		}
		else {
			// 顧客住所
			message = "<bean:message key="warns.zipcode.address.mismatch" arg0="顧客"/>";

		}
		$("#address_errors").empty();

		if( zipCode.val().length == 0 ) {
			if( address1.val().length != 0 ) {
				// 郵便番号が空で住所１が入力されている場合、住所１から郵便番号を検索する
				openSearchZipDialogWhenResultMultiple(
					zipCodeId + "Dialog",
					function(id, map) {
						zipCode.val(map["zipCode"]);
						address1.val(map["zipAddress1"]);
					},
					null,
					address1.val()
				);
			}
			return;
		}

		var data = new Object();
		data[ "zipCode" ] = zipCode.val();
		data[ "zipAddress1" ] = address1.val();

		asyncRequest(
				contextRoot + "/ajax/checkZipCodeAndAddressAjax/check",
				data,
				function(result){
					if(result != "true") {
						$("#address_errors").append(document.createTextNode(message));
					}
				}
			);
	}


	/*
	 * 既存顧客を呼び出す顧客検索ダイアログを表示する
	 */
	function openCustomerSearchDialog(){
		if(!${isUpdate}) {
			return;
		}
		if(${editMode}) {
			return;
		}

		// 顧客検索ダイアログを開く
		openSearchCustomerDialog( "editCustomer", loadCustomer );
	}

	/*
	 * 顧客検索ダイアログで表示された商品を読み込む
	 */
	function loadCustomer( dialogId, map ) {
		$("#LoadForm").find("#customerCode").val( map["customerCode"] );
		$("#LoadForm").submit();
	}

	-->
	</script>
	<script type="text/javascript" src="./scripts/common.js"></script>
	<script type="text/javascript" src="./scripts/master_customer.js"></script>
	<script type="text/javascript" src="./scripts/jquery.bgiframe.min.js"></script>
</head>
<body onhelp="return false;" onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1302"/>
</jsp:include>


<!-- メイン機能 -->
<s:form styleId="editCustomerForm" onsubmit="return false;">
<div id="main_function">
	<html:hidden property="isUpdate" />
	<html:hidden property="editMode" />

	<span class="title">顧客</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="disabled">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerCustomer()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerCustomer()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="disabled">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteCustomer()">F4<br>削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2003" disabled="disabled">F4<br>削除</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2004" disabled="disabled">F5<br>初期値</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${!editMode}">
		<button tabindex="2004" onclick="openMasterDefaultSettingDialog('master', 'CUSTOMER_MST')">F5<br>初期値</button>
    </c:if>
	<c:if test="${editMode}">
		<button tabindex="2004" disabled="disabled">F5<br>初期値</button>
    </c:if>
</c:if>
<button disabled="disabled">F6<br>&nbsp;</button>
<button disabled="disabled">F7<br>&nbsp;</button>
<c:if test="${isUpdate}">
	<c:if test="${!editMode}">
		<button tabindex="2006" disabled="disabled">F8<br>履歴出力</button>
    </c:if>
	<c:if test="${editMode}">
		<button tabindex="2006" onclick="outputHistory()">F8<br>履歴出力</button>
    </c:if>
</c:if>
		<button disabled="disabled">F9<br>&nbsp;</button>
		<button disabled="disabled">F10<br>&nbsp;</button>
		<button disabled="disabled">F11<br>&nbsp;</button>
		<button disabled="disabled">F12<br>&nbsp;</button>
	</div>
	<br><br><br>

	<div class="function_forms">
	<div id="address_errors" class="action_errors" style="padding-left: 20px"></div>
	<div class="action_errors" style="padding-left: 20px"><html:errors/></div>
	<div style="padding-left: 20px;color: blue;">
	<html:messages id="msg" message="true">
		<bean:write name="msg" ignore="true"/><br>
	</html:messages>
	</div>

    <div class="form_section_wrap">
	    <div class="form_section">
	    	<div class="section_title">
				<span>顧客情報</span>
	            <button class="btn_toggle" />
			</div><!-- /.section_title -->

			<div id="order_section" class="section_body">
			<table id="user_info" class="forms" summary="顧客情報">
				<tr>
					<th><div class="col_title_right_req">顧客コード<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:text styleId="customerCode" property="customerCode" tabindex="100" readonly="${editMode || !isUpdate}"
	                    	styleClass="${editMode || !isUpdate ? 'c_disable' : '' }" style="width: 180px;ime-mode:disabled;"
	                    	onfocus="this.curVal=this.value;"  />

	                    <c:if test="${!editMode}">
	    				<html:image src="${f:url('/images//customize/btn_search.png')}"
	    					style="vertical-align: middle; cursor: pointer;" tabindex="101" onclick="openCustomerSearchDialog();" />
	    				</c:if>
	                </td>
					<th><div class="col_title_right_req">顧客名<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="60" styleId="customerName" property="customerName" style="width: 250px" tabindex="102"
							onfocus="this.curVal=this.value;"/></td>
					<th><div class="col_title_right_req">顧客名カナ<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="60" styleId="customerKana" property="customerKana" style="width: 250px" tabindex="103"
							onfocus="this.curVal=this.value;"
							onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); convertPaymentName(this.value); }"/></td>
				</tr>
				<tr>
					<th><div class="col_title_right_req">事業所名<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="60" styleId="customerOfficeName" property="customerOfficeName" style="width:250px" tabindex="104"/></td>
					<th><div class="col_title_right_req">事業所名カナ<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="60" styleId="customerOfficeKana" property="customerOfficeKana" style="width:250px" tabindex="105"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/></td>
					<th><div class="col_title_right_req">顧客略称<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="14" styleId="customerAbbr" property="customerAbbr" style="width:250px" tabindex="106"/></td>
				</tr>
			</table>

			<table class="forms" style="width: 910px" summary="自社情報2">
				<tr>
					<th><div class="col_title_right_req">郵便番号<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="8" styleId="customerZipCode" property="customerZipCode" style="width:100px;ime-mode:disabled;" tabindex="201"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){searchZipCodeDirect('customer');}"/>
	                <html:image tabindex="202" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="searchZipCode('customer');" />
	                </td>
					<th><div class="col_title_right_req">住所１<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="50" styleId="customerAddress1" property="customerAddress1" style="width:250px" tabindex="202"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ checkZipCodeAndAddress(this); }"/></td>
					<th><div class="col_title_right">住所２</div></th>
					<td><html:text maxlength="50" styleId="customerAddress2" property="customerAddress2" style="width:250px" tabindex="203"/></td>
				</tr>
				<tr>
					<th><div class="col_title_right">担当者</div></th>
					<td><html:text maxlength="60" styleId="customerPcName" property="customerPcName" style="width:250px" tabindex="204"
							onfocus="this.curVal=this.value;" /></td>
					<th><div class="col_title_right">担当者カナ</div></th>
					<td><html:text maxlength="60" styleId="customerPcKana" property="customerPcKana" style="width:250px" tabindex="205"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/></td>
					<th><div class="col_title_right">敬称</div></th>
					<td>
	                    <html:select styleId="customerPcPreCategory" property="customerPcPreCategory" tabindex="206">
	                        <html:options collection="preTypeCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
				</tr>
				<tr>
	                <th><div class="col_title_right">部署名</div></th>
	                <td><html:text maxlength="60" styleId="customerDeptName" property="customerDeptName" style="width:250px" tabindex="207"/></td>
	                <th><div class="col_title_right">役職</div></th>
	                <td colspan="3"><html:text maxlength="60" styleId="customerPcPost" property="customerPcPost" style="width:250px" tabindex="208"/></td>
				</tr>
				<tr>
					<th><div class="col_title_right">TEL</div></th>
					<td><html:text maxlength="15" styleId="customerTel" property="customerTel" style="width:250px;ime-mode:disabled;" tabindex="209"/></td>
					<th><div class="col_title_right">FAX</div></th>
					<td colspan="3"><html:text maxlength="15" styleId="customerFax" property="customerFax" style="width:250px;ime-mode:disabled;" tabindex="210"/></td>
				</tr>
				<tr>
					<th><div class="col_title_right">E-MAIL</div></th>
					<td colspan="5"><html:text maxlength="255" styleId="customerEmail" property="customerEmail" style="width:250px;ime-mode:disabled;" tabindex="211"/></td>
				</tr>
			</table>

			<table class="forms" style="width: 910px" summary="自社情報3">
				<tr>
					<th><div class="col_title_right">顧客ランク</div></th>
					<td colspan="3">
	                    <html:select styleId="customerRankCategory" property="customerRankCategory" tabindex="300" style="width: 250px;">
	                        <html:options collection="customerRankList" property="value" labelProperty="label"/>
						</html:select>
						 <html:checkbox styleId="customerUpdFlag" property="customerUpdFlag" value="1" tabindex="301">顧客ランク適用</html:checkbox>
					</td>
					<th><div class="col_title_right">受注停止</div></th>
					<td>
	                    <html:select styleId="customerRoCategory" property="customerRoCategory" tabindex="302" style="width:150px;">
	                        <html:options collection="customerRoCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right">与信限度額</div></th>
					<td>
						<html:text maxlength="9" styleId="maxCreditLimit" property="maxCreditLimit" styleClass="numeral_commas yen_value BDCyen"
							onchange="applyPriceAlignment();"
							style="width:120px; ime-mode: disabled;" tabindex="303"/>
					</td>
				</tr>
				<tr>
					<th><div class="col_title_right">業種</div></th>
					<td>
	                    <html:select styleId="customerBusinessCategory" property="customerBusinessCategory" tabindex="304" style="width:180px;">
	                        <html:options collection="customerBusinessCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right">職種</div></th>
					<td>
	                    <html:select styleId="customerJobCategory" property="customerJobCategory" tabindex="305" style="width:180px;">
	                        <html:options collection="customerJobCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th><div class="col_title_right_req">税端数処理<bean:message key='labels.must'/></div></th>
					<td>
                    	<html:select styleId="taxFractCategory" property="taxFractCategory" tabindex="306" styleClass="${editMode || !isUpdate ? 'c_disable' : '' }"  disabled="${editMode || !isUpdate ? 'true' : '' }"  >
                        	<html:options collection="taxFractCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right_req">税転嫁<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:select styleId="taxShiftCategory" property="taxShiftCategory" tabindex="308" style="width:150px;">
	                        <html:options collection="taxShiftCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right">最終締処理日</div></th>
					<td><html:text styleId="lastCutoffDate" property="lastCutoffDate" style="width:120px;text-align:center;" tabindex="309" styleClass="c_disable" readonly="true"/></td>
				</tr>
			</table>
			<html:hidden property="taxFractCategory" styleId="taxFractCategory" />
			<html:hidden property="priceFractCategory" styleId="priceFractCategory" />

			<table class="forms" style="width: 910px" summary="自社情報3">
				<tr>
					<th><div class="col_title_right_req">取引区分<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:select styleId="salesCmCategory" property="salesCmCategory" tabindex="400" >
	                        <html:options collection="salesCmCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right_req">支払条件<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:select styleId="cutoffGroupCategory" property="cutoffGroupCategory" tabindex="401">
	                        <html:options collection="cutoffGroupList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right_req">回収方法<bean:message key='labels.must'/></div></th>
					<td colspan="3">
	                    <html:select styleId="paybackTypeCategory" property="paybackTypeCategory" tabindex="402">
	                        <html:options collection="paybackTypeCategoryList" property="value" labelProperty="label"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th><div class="col_title_right_req">請求書発行単位<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:select styleId="billPrintUnit" property="billPrintUnit" tabindex="403" >
	                        <html:options collection="billPrintUnitList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right_req">請求書日付有無<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:select styleId="billDatePrint" property="billDatePrint" tabindex="404">
	                        <html:options collection="billDatePrintList" property="value" labelProperty="label"/>
						</html:select>
					</td>
					<th><div class="col_title_right">仮納品書出力不可</div></th>
					<td><html:checkbox styleId="tempDeliverySlipFlag" property="tempDeliverySlipFlag" value="0" tabindex="405"/></td>
				</tr>
			</table>

			<table class="forms" style="width: 910px" summary="自社情報3">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 90%">
				</colgroup>
				<tr>
					<th><div class="col_title_right">振込名義</div></th>
					<td><html:text maxlength="60" styleId="paymentName" property="paymentName" style="width:600px" tabindex="500"/></td>
				</tr>
				<tr>
					<th><div class="col_title_right">備考</div></th>
					<td><html:text maxlength="120" styleId="remarks" property="remarks" style="width:600px" tabindex="501"/></td>
				</tr>
				<tr>
					<th><div class="col_title_right">コメント</div></th>
					<td><html:text maxlength="1000" styleId="commentData" property="commentData" style="width:600px" tabindex="502"/></td>
				</tr>
			</table>
			</div><!-- /.section_body -->
		</div><!-- /.form_section -->
	</div><!-- /.form_section_wrap -->

    <div class="form_section_wrap">
	    <div class="form_section">
	    	<div class="section_title">
				<span>
				<span>納入先情報</span>
				<button style="position:relative; left: 800px; width: 150px;" onclick="copyCustomerToDelivery()" tabindex="600" class="btn_medium">顧客の情報をコピー</button>
				</span>
	            <button class="btn_toggle" />
			</div><!-- /.section_title -->

			<div id="order_section" class="section_body">
			<table id="delivery_table" class="forms" summary="納入先情報">
	            <tbody id="delivery_info">
				<tr>
					<th><div class="col_title_right_req">納入先<bean:message key='labels.must'/></div></th>
					<td colspan="5">
						<html:select styleId="selectedDelivery" property="selectedDelivery" style="width: 500px" onchange="changeDelivery()" tabindex="601">
	                        <html:option value=""></html:option>
	                        <html:options collection="deliveryList" property="deliveryCode" labelProperty="deliveryName"/>
						</html:select>
						<html:hidden styleId="selectedDeliveryIndex" property="selectedDeliveryIndex"/>
						<button id="addb" style="width: 80px; margin-left: 50px" onclick="addDelivery()" tabindex="602" class="btn_medium">追加</button>
						<button id="delb" style="width: 80px" onclick="deleteDelivery()" tabindex="603" class="btn_medium">削除</button>
					</td>
				</tr>
				<tr id="deliveryRow_0_1">
					<th><div class="col_title_right_req">納入先名<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:text maxlength="60" styleId="deliveryRow_0Name" property="newDeliveryName" tabindex="604" style="width:200px;"/>
					</td>
					<th><div class="col_title_right_req">納入先名カナ<bean:message key='labels.must'/></div></th>
					<td colspan="3">
						<html:text maxlength="60" styleId="deliveryRow_0Kana" property="newDeliveryKana" tabindex="605" style="width:200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
					</td>
				</tr>
				<tr id="deliveryRow_0_2">
					<th><div class="col_title_right">事業所名</div></th>
					<td>
						<html:text maxlength="60" styleId="deliveryRow_0OfficeName" property="newDeliveryOfficeName" tabindex="606" style="width:200px;"/>
	                </td>
					<th><div class="col_title_right">事業所名カナ</div></th>
					<td>
						<html:text maxlength="60" styleId="deliveryRow_0OfficeKana" property="newDeliveryOfficeKana" tabindex="607" style="width:200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
	                </td>
					<th><div class="col_title_right">部署名</div></th>
					<td>
						<html:text maxlength="60" styleId="deliveryRow_0DeptName" property="newDeliveryDeptName" tabindex="608" style="width:200px;"/>
	                </td>
				</tr>
				<tr id="deliveryRow_0_3">
					<th><div class="col_title_right_req">郵便番号<bean:message key='labels.must'/></div></th>
					<td>
						<html:text maxlength="8" styleId="deliveryRow_0ZipCode" property="newDeliveryZipCode" tabindex="609" style="width:100px;ime-mode:disabled;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ searchZipCodeDirect('deliveryRow_0'); }"/>
	    				<html:image tabindex="610" styleId="deliveryRow_0ZipCodeIcon" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;"/>
	                </td>
					<th><div class="col_title_right_req">住所１<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:text maxlength="50" styleId="deliveryRow_0Address1" property="newDeliveryAddress1" tabindex="611" style="width:200px;"
	                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ checkZipCodeAndAddress(this); }"/>
	                </td>
					<th><div class="col_title_right">住所２</div></th>
					<td>
	                    <html:text maxlength="50" styleId="deliveryRow_0Address2" property="newDeliveryAddress2" tabindex="612" style="width:200px;"/>
	                </td>
				</tr>
				<tr id="deliveryRow_0_4">
					<th><div class="col_title_right">担当者</div></th>
					<td>
						<html:text maxlength="60" styleId="deliveryRow_0PcName" property="newDeliveryPcName" tabindex="613" style="width:150px;"/>
	                </td>
					<th><div class="col_title_right">担当者カナ</div></th>
					<td>
						<html:text maxlength="60" styleId="deliveryRow_0PcKana" property="newDeliveryPcKana" tabindex="614" style="width:150px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
	                </td>
					<th><div class="col_title_right">敬称</div></th>
					<td>
	                    <html:select styleId="deliveryRow_0PcPreCategory" property="newDeliveryPcPreCategory" tabindex="615">
	                        <html:options collection="preTypeCategoryList" property="value" labelProperty="label"/>
						</html:select>
	                    <input type="hidden" id="defaultPcPreCategory" name="defaultPcPreCategory" value="">
	                </td>
				</tr>
				<tr id="deliveryRow_0_5">
					<th><div class="col_title_right">TEL</div></th>
					<td>
						<html:text maxlength="15" styleId="deliveryRow_0Tel" property="newDeliveryTel" tabindex="616" style="width:150px;ime-mode:disabled;"/>
	                </td>
					<th><div class="col_title_right">FAX</div></th>
					<td colspan="3">
	                    <html:text maxlength="15" styleId="deliveryRow_0Fax" property="newDeliveryFax" tabindex="617" style="width:150px;ime-mode:disabled;"/>
	                </td>
				</tr>
				<tr id="deliveryRow_0_6">
					<th><div class="col_title_right">E-MAIL</div></th>
					<td colspan="5">
						<html:text maxlength="255" styleId="deliveryRow_0Email" property="newDeliveryEmail" tabindex="618" style="width:400px;ime-mode:disabled;"/>
	                </td>
				</tr>
	            <c:forEach var="deliveryList" varStatus="s" items="${deliveryList}">
				<tr id="deliveryRow_${s.index+1}_1" style="display: none;">
					<th><div class="col_title_right_req">納入先名<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:text maxlength="60" styleId="deliveryRow_${s.index+1}Name" name="deliveryList" property="deliveryName" tabindex="-1" indexed="true" style="width:200px;"/>
	                    <html:hidden styleId="deliveryRow_${s.index+1}Code" name="deliveryList" property="deliveryCode" indexed="true"/>
					</td>
					<th><div class="col_title_right_req">納入先名カナ<bean:message key='labels.must'/></div></th>
					<td colspan="3">
	                    <html:text maxlength="60" styleId="deliveryRow_${s.index+1}Kana" name="deliveryList" property="deliveryKana" tabindex="-1" indexed="true" style="width:200px;"
	                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
					</td>
				</tr>
				<tr id="deliveryRow_${s.index+1}_2" style="display: none;">
					<th><div class="col_title_right">事業所名</div></th>
					<td><html:text maxlength="60" styleId="deliveryRow_${s.index+1}OfficeName" name="deliveryList" property="deliveryOfficeName" tabindex="-1" indexed="true" style="width:200px;"/></td>
					<th><div class="col_title_right">事業所名カナ</div></th>
					<td><html:text maxlength="60" styleId="deliveryRow_${s.index+1}OfficeKana" name="deliveryList" property="deliveryOfficeKana" tabindex="-1" indexed="true" style="width:200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/></td>
					<th><div class="col_title_right">部署名</div></th>
					<td><html:text maxlength="60" styleId="deliveryRow_${s.index+1}DeptName" name="deliveryList" property="deliveryDeptName" tabindex="-1" indexed="true" style="width:200px;"/></td>
				</tr>
				<tr id="deliveryRow_${s.index+1}_3" style="display: none;">
					<th><div class="col_title_right_req">郵便番号<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="8" styleId="deliveryRow_${s.index+1}ZipCode" name="deliveryList" property="deliveryZipCode" tabindex="-1" indexed="true" style="width:100px;ime-mode:disabled;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ searchZipCodeDirect('deliveryRow_${s.index+1}'); }"/>
					<html:image tabindex="-1" styleId="deliveryRow_${s.index+1}_3_icon" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;"/>
	                </td>
					<th><div class="col_title_right_req">住所１<bean:message key='labels.must'/></div></th>
					<td><html:text maxlength="50" styleId="deliveryRow_${s.index+1}Address1" name="deliveryList" property="deliveryAddress1" tabindex="-1" indexed="true" style="width:200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ checkZipCodeAndAddress(this); }"/></td>
					<th><div class="col_title_right">住所２</div></th>
					<td><html:text maxlength="50" styleId="deliveryRow_${s.index+1}Address2" name="deliveryList" property="deliveryAddress2" tabindex="-1" indexed="true" style="width:200px;"/></td>
				</tr>
				<tr id="deliveryRow_${s.index+1}_4" style="display: none;">
					<th><div class="col_title_right">担当者</div></th>
					<td><html:text maxlength="60" styleId="deliveryRow_${s.index+1}PcName" name="deliveryList" property="deliveryPcName" tabindex="-1" indexed="true" style="width:200px;"/></td>
					<th><div class="col_title_right">担当者カナ</div></th>
					<td><html:text maxlength="60" styleId="deliveryRow_${s.index+1}PcKana" name="deliveryList" property="deliveryPcKana" tabindex="-1" indexed="true" style="width:200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/></td>
					<th><div class="col_title_right">敬称</div></th>
					<td>
	                    <html:select styleId="deliveryRow_${s.index+1}PcPreCategory" name="deliveryList" property="deliveryPcPreCategory" tabindex="-1" indexed="true">
	                        <html:options collection="preTypeCategoryList" property="value" labelProperty="label"/>
						</html:select>
	                </td>
				</tr>
				<tr id="deliveryRow_${s.index+1}_5" style="display: none;">
					<th><div class="col_title_right">TEL</div></th>
					<td><html:text maxlength="15" styleId="deliveryRow_${s.index+1}Tel" name="deliveryList" property="deliveryTel" tabindex="-1" indexed="true" style="width:200px;ime-mode:disabled;"/></td>
					<th><div class="col_title_right">FAX</div></th>
					<td colspan="3"><html:text maxlength="15" styleId="deliveryRow_${s.index+1}Fax" name="deliveryList" property="deliveryFax" tabindex="-1" indexed="true" style="width:200px;ime-mode:disabled;"/></td>
				</tr>
				<tr id="deliveryRow_${s.index+1}_6" style="display: none;">
					<th><div class="col_title_right">E-MAIL</div></th>
					<td colspan="5"><html:text maxlength="255" styleId="deliveryRow_${s.index+1}Email" name="deliveryList" property="deliveryEmail" tabindex="-1" indexed="true" style="width:400px;ime-mode:disabled;"/></td>
				</tr>
	            <script type="text/javascript">
				<!--
	            	// イベントの貼り付け
	                $("#deliveryRow_${s.index+1}_3_icon").bind("click", {"id": "deliveryRow_${s.index+1}"}, searchZipCode);
	            -->
	            </script>
	            </c:forEach>
	            <script type="text/javascript">
	            <!--
	                // イベントの貼り付け
	                $("#deliveryRow_0ZipCodeIcon").bind("click", {"id": "deliveryRow_0"}, searchZipCode);
	            -->
	            </script>
	            </tbody>
			</table>
			</div><!-- /.section_body -->
		</div><!-- /.form_section -->
	</div><!-- /.form_section_wrap -->

    <div class="form_section_wrap">
	    <div class="form_section">
	    	<div class="section_title">
				<span>請求先情報</span>
				<button style="position:relative; left: 800px; width: 150px;" onclick="copyCustomerToBilling()" tabindex="700" class="btn_medium">顧客の情報をコピー</button>
	            <button class="btn_toggle" />
			</div><!-- /.section_title -->

			<div id="order_section" class="section_body">

			<table id="bill_info" class="forms" summary="請求先情報">
				<tr>
					<th><div class="col_title_right_req">請求先名<bean:message key='labels.must'/></div></th>
					<td>
						<html:hidden styleId="billTo_deliveryCode" property="billTo.deliveryCode" />
						<html:text maxlength="60" styleId="billTo_deliveryName" property="billTo.deliveryName" tabindex="701" style="width: 200px;"/>
					</td>
					<th><div class="col_title_right_req">請求先名カナ<bean:message key='labels.must'/></div></th>
					<td colspan="3">
						<html:text maxlength="60" styleId="billTo_deliveryKana" property="billTo.deliveryKana" tabindex="702" style="width: 200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
					</td>
				</tr>
				<tr>
					<th><div class="col_title_right">事業所名</div></th>
					<td>
						<html:text maxlength="60" styleId="billTo_deliveryOfficeName" property="billTo.deliveryOfficeName" tabindex="703" style="width: 200px;"/>
	                </td>
					<th><div class="col_title_right">事業所名カナ</div></th>
					<td>
						<html:text maxlength="60" styleId="billTo_deliveryOfficeKana" property="billTo.deliveryOfficeKana" tabindex="704" style="width: 200px;"
							onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
	                </td>
					<th><div class="col_title_right">部署名</div></th>
					<td>
						<html:text maxlength="60" styleId="billTo_deliveryDeptName" property="billTo.deliveryDeptName" tabindex="705" style="width: 200px;"/>
	                </td>
				</tr>
				<tr>
					<th><div class="col_title_right_req">郵便番号<bean:message key='labels.must'/></div></th>
					<td>
	                    <html:text maxlength="8" styleId="billTo_deliveryZipCode" property="billTo.deliveryZipCode" tabindex="706" style="width: 100px;ime-mode:disabled;"
	                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ searchZipCodeDirect('billTo_delivery'); }" />
	    				<html:image tabindex="707" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="searchZipCode('billTo_delivery');" />
	                </td>
					<th><div class="col_title_right_req">住所１<bean:message key='labels.must'/></div></th>
	                <td>
	                    <html:text maxlength="50" styleId="billTo_deliveryAddress1" property="billTo.deliveryAddress1" tabindex="708" style="width: 200px"
	                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ checkZipCodeAndAddress(this); }"/>
	                </td>
					<th><div class="col_title_right">住所２</div></th>
					<td>
	                    <html:text maxlength="50" styleId="billTo_deliveryAddress2" property="billTo.deliveryAddress2" tabindex="709" style="width: 200px"/>
	                </td>
				</tr>
				<tr>
					<th><div class="col_title_right">担当者</div></th>
					<td>
	                    <html:text maxlength="60" styleId="billTo_deliveryPcName" property="billTo.deliveryPcName" tabindex="710" style="width: 200px"/>
	                </td>
					<th><div class="col_title_right">担当者カナ</div></th>
					<td>
	                    <html:text maxlength="60" styleId="billTo_deliveryPcKana" property="billTo.deliveryPcKana" tabindex="711" style="width: 200px"
	                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=convertHKanaToKKana(this.value); }"/>
	                </td>
					<th><div class="col_title_right">敬称</div></th>
					<td>
	                    <html:select styleId="billTo_deliveryPcPreCategory" property="billTo.deliveryPcPreCategory" tabindex="712">
	                        <html:options collection="preTypeCategoryList" property="value" labelProperty="label"/>
						</html:select>
	                </td>
				</tr>
				<tr>
					<th><div class="col_title_right">TEL</div></th>
					<td>
	                    <html:text maxlength="15" styleId="billTo_deliveryTel" property="billTo.deliveryTel" tabindex="713" style="width: 200px;ime-mode:disabled;"/>
	                </td>
					<th><div class="col_title_right">FAX</div></th>
					<td colspan="3">
	                    <html:text maxlength="15" styleId="billTo_deliveryFax" property="billTo.deliveryFax" tabindex="714" style="width: 200px;ime-mode:disabled;"/>
	                </td>
				</tr>
				<tr>
					<th><div class="col_title_right">E-MAIL</div></th>
					<td colspan="5">
	                    <html:text maxlength="255" styleId="billTo_deliveryEmail" property="billTo.deliveryEmail" tabindex="715" style="width: 400px;ime-mode:disabled;"/>
	                </td>
				</tr>
			</table>
			</div><!-- /.section_body -->
		</div><!-- /.form_section -->
	</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}　更新日:${updDatetmShow}　</span>
			<button tabindex="800" onclick="initForm()" class="btn_medium">初期化</button>
<c:if test="${!isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="801" disabled="disabled" class="btn_medium">新規追加</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
            <button tabindex="802" disabled="disabled" class="btn_medium">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button tabindex="802" onclick="registerCustomer()" class="btn_medium">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button tabindex="802" onclick="registerCustomer()" class="btn_medium">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="803" disabled="disabled" class="btn_medium">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="803" onclick="deleteCustomer()" class="btn_medium">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="803" disabled="disabled" class="btn_medium">削除</button>
    </c:if>
</c:if>
		</div>

	</div>
</div>

	<html:hidden property="creDatetm"/>
	<html:hidden property="creDatetmShow"/>
	<html:hidden property="updDatetm"/>
	<html:hidden property="updDatetmShow"/>
	<html:hidden property="lastSalesCutoffDate"/>

<%-- 敬称 --%>
  	<c:forEach var="preType" items="${preTypeCategoryList}" varStatus="status">
	<input type="hidden" name="preTypeCategoryList[${status.index}].label" value="${f:h(preType.label)}">
	<input type="hidden" name="preTypeCategoryList[${status.index}].value" value="${preType.value}">
</c:forEach>
<%-- 顧客ランク --%>
<c:forEach var="rank" items="${customerRankList}" varStatus="status">
	<input type="hidden" name="customerRankList[${status.index}].label" value="${f:h(rank.label)}">
	<input type="hidden" name="customerRankList[${status.index}].value" value="${rank.value}">
</c:forEach>
<%-- 受注停止 --%>
<c:forEach var="roCategory" items="${customerRoCategoryList}" varStatus="status">
	<input type="hidden" name="customerRoCategoryList[${status.index}].label" value="${f:h(roCategory.label)}">
	<input type="hidden" name="customerRoCategoryList[${status.index}].value" value="${roCategory.value}">
</c:forEach>
<%-- 業種 --%>
<c:forEach var="businessCategory" items="${customerBusinessCategoryList}" varStatus="status">
	<input type="hidden" name="customerBusinessCategoryList[${status.index}].label" value="${f:h(businessCategory.label)}">
	<input type="hidden" name="customerBusinessCategoryList[${status.index}].value" value="${businessCategory.value}">
</c:forEach>
<%-- 職種 --%>
<c:forEach var="jobCategory" items="${customerJobCategoryList}" varStatus="status">
	<input type="hidden" name="customerJobCategoryList[${status.index}].label" value="${f:h(jobCategory.label)}">
	<input type="hidden" name="customerJobCategoryList[${status.index}].value" value="${jobCategory.value}">
</c:forEach>
<%-- 取引区分 --%>
<c:forEach var="cmCategory" items="${salesCmCategoryList}" varStatus="status">
	<input type="hidden" name="salesCmCategoryList[${status.index}].label" value="${f:h(cmCategory.label)}">
	<input type="hidden" name="salesCmCategoryList[${status.index}].value" value="${cmCategory.value}">
</c:forEach>
<%-- 税転嫁 --%>
<c:forEach var="taxShiftCategory" items="${taxShiftCategoryList}" varStatus="status">
	<input type="hidden" name="taxShiftCategoryList[${status.index}].label" value="${f:h(taxShiftCategory.label)}">
	<input type="hidden" name="taxShiftCategoryList[${status.index}].value" value="${taxShiftCategory.value}">
</c:forEach>
<%-- 税転嫁 --%>
<c:forEach var="cutoffGroup" items="${cutoffGroupList}" varStatus="status">
	<input type="hidden" name="cutoffGroupList[${status.index}].label" value="${f:h(cutoffGroup.label)}">
	<input type="hidden" name="cutoffGroupList[${status.index}].value" value="${cutoffGroup.value}">
</c:forEach>
<%-- 回収方法 --%>
<c:forEach var="paybackTypeCategory" items="${paybackTypeCategoryList}" varStatus="status">
	<input type="hidden" name="paybackTypeCategoryList[${status.index}].label" value="${f:h(paybackTypeCategory.label)}">
	<input type="hidden" name="paybackTypeCategoryList[${status.index}].value" value="${paybackTypeCategory.value}">
</c:forEach>
<%-- 税端数処理 --%>
<c:forEach var="taxFractCategory" items="${taxFractCategoryList}" varStatus="status">
	<input type="hidden" name="taxFractCategoryList[${status.index}].label" value="${f:h(taxFractCategory.label)}">
	<input type="hidden" name="taxFractCategoryList[${status.index}].value" value="${taxFractCategory.value}">
</c:forEach>
<%-- 単価端数処理 --%>
<c:forEach var="priceFractCategory" items="${priceFractCategoryList}" varStatus="status">
	<input type="hidden" name="priceFractCategoryList[${status.index}].label" value="${f:h(priceFractCategory.label)}">
	<input type="hidden" name="priceFractCategoryList[${status.index}].value" value="${priceFractCategory.value}">
</c:forEach>
<%-- 請求書発行単位 --%>
<c:forEach var="billPrintUnit" items="${billPrintUnitList}" varStatus="status">
	<input type="hidden" name="billPrintUnitList[${status.index}].label" value="${f:h(billPrintUnit.label)}">
	<input type="hidden" name="billPrintUnitList[${status.index}].value" value="${billPrintUnit.value}">
</c:forEach>
<%-- 請求書日付有無 --%>
<c:forEach var="billDatePrint" items="${billDatePrintList}" varStatus="status">
	<input type="hidden" name="billDatePrintList[${status.index}].label" value="${f:h(billDatePrint.label)}">
	<input type="hidden" name="billDatePrintList[${status.index}].value" value="${billDatePrint.value}">
</c:forEach>

</s:form>

<form id="LoadForm" action="${f:url('load')}" method="POST">
	<input type="hidden" id="customerCode" name="customerCode">
</form>
</body>

</html>
