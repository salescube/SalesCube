<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@taglib prefix="s" uri="http://sastruts.seasar.org" %>
<%@taglib prefix="f" uri="http://sastruts.seasar.org/functions" %>
<%@taglib prefix="sw" uri="/WEB-INF/tags/functions.tld" %>
<%
  if (!response.getContentType().startsWith("application/vnd.ms-excel")) {
    response.setHeader("Expires", "0");
    response.setHeader("Pragma","no-cache");
    response.setHeader("Cache-Control","no-cache");
  }
%>