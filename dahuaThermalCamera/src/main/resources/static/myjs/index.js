/**
 * 
 */
git.orgSelectionBox = function(suggestFields, checkbox) {
	var body = document.getElementsByTagName('body');
	var orgEle = document.getElementById('orgTree_div');
	checkbox = checkbox ? checkbox : false;
	if (!orgEle) {
		var s = document.createElement('div');
		s.setAttribute('id', 'orgTree_div');
		body[0].appendChild(s);
		var divStr = ' <div id="orgSelectionBoxDialog" style="display:none;width:400px;height:326px;" title="机构选择框"> ';
		divStr += '              <div class="easyui-panel" data-options="fit:true" style="padding:0px 5px;">';
		if (checkbox) {
			divStr += '<div style="margin:10px 0">';
			divStr += "<input type=\"checkbox\" checked onchange=\"$('#orgTree_').tree({cascadeCheck:$(this).is(':checked')})\">是否级联 ";
			divStr += "<input type=\"checkbox\" onchange=\"$('#orgTree_').tree({onlyLeafCheck:$(this).is(':checked')})\">过滤部门";
			divStr += ' </div>';
		}
		divStr += '                      <ul id="orgTree_" style="height: 95px;" class="easyui-tree" ></ul>';
		divStr += '              </div>';
		divStr += '     </div>';
		document.getElementById('orgTree_div').innerHTML = divStr;// document.write(divStr);
	}
	$('#orgSelectionBoxDialog').show();
	var tree = $('#orgTree_').tree({
		url : sy.contextPath + '/system/org/findChildren.do?orgNo=',
		idField : 'orgNo',
		textField : 'orgNm',
		parentField : 'parentOrgNo',
		checkbox : checkbox,
		onLoadSuccess : function() {
			var fields = suggestFields.split('|');
			var orgValue = $("input[name='" + fields[0] + "']").val();
			if (orgValue) {
				if (checkbox) {
					var array = orgValue.split(',');
					for (var i = 0; i < array.length; i++) {
						var node = $('#orgTree_').tree('find', array[i]);
						$('#orgTree_').tree('check', node.target);
					}
				} else {
					var node = $('#orgTree_').tree('find', orgValue);
					$('#orgTree_').tree('select', node.target);
				}
			}
		}
	});
	$('#orgSelectionBoxDialog').dialog({
		buttons : [ {
			text : '确定',
			handler : function() {
				var fields = suggestFields.split('|');
				if (!checkbox) {
					var node = $('#orgTree_').tree('getSelected');
					if (node) {
						$("input[name='" + fields[0] + "']").val(node.id);
						$("input[name='" + fields[1] + "']").val(node.text);
					}
				} else {
					var nodes = $('#orgTree_').tree('getChecked');
					var ids = '', names = '';
					for (var i = 0; i < nodes.length; i++) {
						if (ids != '')
							ids += ',';
						if (names != '')
							names += ',';
						ids += nodes[i].id;
						names += nodes[i].text;
					}
					$("input[name='" + fields[0] + "']").val(ids);
					$("input[name='" + fields[1] + "']").val(names);

				}
				$('#orgSelectionBoxDialog').dialog('close');
			}
		}, {
			text : '取消',
			handler : function() {
				$('#orgSelectionBoxDialog').dialog('close');
			}
		} ]
	});
}