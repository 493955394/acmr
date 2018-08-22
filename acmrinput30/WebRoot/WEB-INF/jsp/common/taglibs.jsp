<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="projectTitle" value="<%=com.acmr.helper.constants.Const.COMP_OFFICE %>" />
<c:set var="pub" value="<%=acmr.util.WebConfig.factor.getInstance().getPropertie(\"login.properties\", \"puburl\")%>" />
<c:set var="passport" value="<%=acmr.util.WebConfig.factor.getInstance().getPropertie(\"login.properties\", \"ssologinurl\")%>" />
