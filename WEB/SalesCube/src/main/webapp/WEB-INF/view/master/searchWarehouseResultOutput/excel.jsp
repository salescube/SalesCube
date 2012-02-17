<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<% response.setHeader("Content-Disposition", "attachment; filename=\"WAREHOUSE_LIST.xls\"" );%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:x="urn:schemas-microsoft-com:office:excel"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=UTF-8" />
<style>
<!--
td
	{mso-style-parent:style0;
	padding-top:1px;
	padding-right:1px;
	padding-left:1px;
	mso-ignore:padding;
	color:windowtext;
	font-size:11.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:"MS PGothic", monospace;
	mso-font-charset:128;
	mso-number-format:General;
	text-align:general;
	vertical-align:middle;
	border:none;
	mso-background-source:auto;
	mso-pattern:auto;
	mso-protection:locked visible;
	white-space:nowrap;
	mso-rotate:0;}
.xl64
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	font-weight:700;
	mso-number-format:"\@";
	border:.5pt solid windowtext;
	background:#FFD95F;
	mso-pattern:auto none;}
.xl65
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\#\,\#\#0_ ";
	border:.5pt solid windowtext;}
.xl66
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"${mineDto.unitPriceDecAlignFormat4xls}";
	border:.5pt solid windowtext;}
.xl67
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"${mineDto.numDecAlignFormat4xls}";
	border:.5pt solid windowtext;}
.xl68
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"Short Date";
	border:.5pt solid windowtext;}
.xl69
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"yyyy\/m\/d\\ h\:mm\:ss\.000";
	border:.5pt solid windowtext;}
.xl70
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\@";
	border:.5pt solid windowtext;}
.xl71
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"${mineDto.statsDecAlignFormat4xls}";
	border:.5pt solid windowtext;}
-->
</style>
</head>
<body>
	<span id="searchResultList">
		<%@ include file="/WEB-INF/view/master/searchWarehouseResultOutput/resultList.jsp" %>
	</span>
</body>
</html>