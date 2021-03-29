<%@ page import="com.eluon.vepc.mano.vo.PaginationVO" %>
                <!--페이징 시작-->
				<table cellpadding="0" cellspacing="0" width="748">
                    <tr>
                        <td width="15%" height="53" align="left"><img src="<c:out value="${contextPath}" />/images/txt_page.gif" width="24" height="6" alt="page" align="absmiddle"/> <c:out value="${paginationVO.currentPage}" /> / <c:out value="${paginationVO.maxPage}" /></td>
                        <td width="70%" align="center">
                         <%= ((PaginationVO)request.getAttribute("paginationVO")).getPageDivideForm()%>
                        </td>
                        <td width="15%"></td>
                    </tr>
                </table>
                <!--페이징 끝-->