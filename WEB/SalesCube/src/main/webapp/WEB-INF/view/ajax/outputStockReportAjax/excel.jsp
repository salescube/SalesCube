<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<% response.setHeader("Content-Disposition", "attachment; filename=\"STOCK_REPORT.xls\"" );%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:x="urn:schemas-microsoft-com:office:excel"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=UTF-8" />
<style>
<!--
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
.xl70
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\@";}
.xl71
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\0022¥\0022\#\,\#\#0\;\0022¥\0022\\-\#\,\#\#0";}
.xl72
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\@";
	border:.5pt solid windowtext;}
.xl73
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\#\,\#\#0";
	border:.5pt solid windowtext;}
.xl74
	{mso-style-parent:style0;
	font-size: 10pt;
	font-weight: normal;
	white-space: nowrap;
	mso-number-format:"\0022¥\0022\#\,\#\#0\;\0022¥\0022\\-\#\,\#\#0";
	border:.5pt solid windowtext;}
-->
</style>
</head>
<body>
	<span id="searchResultList">
		<%@ include file="/WEB-INF/view/ajax/outputStockReportAjax/outputResultList.jsp" %>
	</span>
</body>
</html>