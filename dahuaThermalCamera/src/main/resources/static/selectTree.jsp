<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/common/common.jsp"%>
<%
	// jsp对应的静态页面目录及文件名: 树标签页面
	// jsp的功能说明: 弹出树标签窗口,带节点查询功能
	// jsp对应的action:
	// @author 叶清平
	// @create date: 2015-01-27
	// @version: 1.0.0
%>
<!DOCTYPE html>
<html>
<head>
<title>树标签页面</title>
<script type="text/javascript" src="${contextPath}/page/common/selectTree.js"></script>
<style type="text/css">
.tree .tree-item, .tree .tree-folder {
	border: 0px solid #FFF;
}
</style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="initIds" value="${param.ids}">
	<input type="hidden" id="ids">
	<input type="hidden" id="names">
	<input type="hidden" id="checkbox" value="${param.checkbox}">
	<input type="hidden" id="lines" value="${param.lines}">
	<input type="hidden" id="cascadeCheck" value="${param.cascadeCheck}">
	<input type="hidden" id="onlyLeafCheck" value="${param.onlyLeafCheck}">
	<input type="hidden" id="dataUrl" value="${param.dataUrl}">
	<input type="hidden" id="expandNodes" value="${param.expandNodes}">
	<!-- 查询区域 -->
	<div data-options="region:'north',border:false" style="overflow: hidden; padding: 1px;">
		<table style="background: #fbfaff; width: 100%;">
			<tr>
				<td width="140px">
					<input id="condition" class="easyui-searchbox" data-options="prompt:'请输入查询条件',searcher:next" style="width: 140px;" />
				</td>
				<td width="70px">
					<a class="easyui-linkbutton easyui-tooltip" title="上一个" data-options="iconCls:'pagination-prev',plain:true" onclick="prev()"></a>
					<a class="easyui-linkbutton easyui-tooltip" title="下一个" data-options="iconCls:'pagination-next',plain:true" onclick="next()"></a>
				</td>
				<td>
					<span id="msg" style="color: red;"></span>
				</td>
			</tr>
		</table>
	</div>
	<!-- 树 -->
	<div data-options="region:'center',border:false">
		<ul id="treeTag"></ul>
	</div>
</body>
</html>
