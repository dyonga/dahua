/**
 * 树标签页面js
 * 
 * @author 叶清平
 * @create date 2015-01-27
 * @version: 1.0.2
 * @modify	1.0.1	2015-02-07	叶清平	bug366	新增onBeforeSelect方法,判断是否只允许选中叶子节点
 * @modify	1.0.2	2015-03-12	叶清平			修改onLoadSuccess:删除ids清空
 */
/**
 * 符合查询条件的节点数组
 */
var searchNodes = [];
/**
 * 上一次选中的节点下标
 */
var searchIndex = -1;
/**
 * 上一次查询条件
 */
var searchValue;
/**
 * 页面初始化
 */
$(function() {
	// 重写window.onload方法，在树加载成功后再关闭遮罩
	window.onload = null;
	// 生成树
	$('#treeTag').tree({
		checkbox : $('#checkbox').val() == 'true' ? true : false,// 复选框
		lines : $('#lines').val() == 'true' ? true : false,// 虚线
		method : 'post',
		animate : true,
		cascadeCheck : $('#cascadeCheck').val() == 'true' ? true : false,// 层叠选中
		onlyLeafCheck : $('#onlyLeafCheck').val() == 'true' ? true : false,// 是否只在末级节点之前显示复选框
		url : $('#dataUrl').val(),
		onBeforeExpand : function(node, param) {
			$('#treeTag').tree('options').url = "";
		},
		onLoadSuccess : function(node, data) {
			git.closeProgress();// 加载完关闭
			// 判断是否制定展开节点
			var expandNodes = $('#expandNodes').val();
			if (!isEmptyStr(expandNodes)) {
				$('#treeTag').tree('collapseAll');
				$.each(expandNodes.split(','), function() {
					$('#treeTag').tree('expandTo', $('#treeTag').tree('find', this).target);
				});
			}
			// 设置默认值
			var ids = $('#initIds').val().split(',');
			for (var i = 0; i < ids.length; i++) {
				var node = $('#treeTag').tree('find', ids[i]);
				if (!isNullObj(node)) {
					if ($('#checkbox').val() == 'true' ? true : false) {
						$('#treeTag').tree('check', node.target);
					} else {
						$('#treeTag').tree('select', node.target);
					}
				}
			}
		},
		onLoadError : function(arguments) {
			git.closeProgress();// 加载完关闭
		},
		onBeforeSelect : function(node) {
			// 判断是否只允许选中叶子节点
			if ($('#onlyLeafCheck').val() == 'true') {
				// 判断是否叶子节点,不是则返回false不允许选中
				if (!$('#treeTag').tree('isLeaf', node.target)) {
					return false;
				}
			}
		},
		onCheck : function() {
			// 设置返回值
			setReturnValue();
		},
		onSelect : function(node) {
			// 设置返回值
			setReturnValue();
			// 新增判断是否带复选框，如果有，切换复选框选中状态
			if ($('#checkbox').val() == 'true') {
				if (node.checked) {
					$('#treeTag').tree('uncheck', node.target);
				} else {
					$('#treeTag').tree('check', node.target);
				}
			}
		}
	});
});

/**
 * 获取符合查询条件的节点数据
 */
function getSearchNodes(searchValue) {
	// 获取根节点
	var roots = $('#treeTag').tree('getRoots');
	var nodes;
	// 循环根节点
	$.each(roots, function() {
		// 判断该节点的名称是否包含查询条件,包含则添加到符合查询条件的数组中
		if (this.text.indexOf(searchValue) >= 0) {
			searchNodes.push(this);
		}
		// 遍历子节点
		nodes = $('#treeTag').tree('getChildren', this.target);
		if (nodes != null && nodes.length > 0) {
			$.each(nodes, function() {
				// 判断该节点的名称是否包含查询条件,包含则添加到符合查询条件的数组中
				if (this.text.indexOf(searchValue) >= 0) {
					searchNodes.push(this);
				}
			});
		}
	});
}
/**
 * 向上查询
 */
function prev() {
	// 判断查询条件是否改变
	if (searchValue !== $('#condition').searchbox('getValue')) {
		// 查询条件改变,则清空查询属性
		searchNodes = [];
		searchIndex = -1;
		searchValue = $('#condition').searchbox('getValue');
		// 判断查询条件是否为空
		if (!isEmptyStr(searchValue)) {
			// 获取符合查询条件的节点数据
			getSearchNodes(searchValue);
		}
	}
	// 判断符合查询条件的节点数组是否有数据
	if (searchNodes.length > 0) {
		$('#msg').html('');
		// 遍历符合查询条件的节点数组
		$.each(searchNodes, function(index) {
			// 判断上传查询的节点下标是否是最后一个,是则从头开始查
			if (searchIndex == -1 || searchIndex == 0) {
				searchIndex = searchNodes.length;
			}
			// 如果当前节点下标大于上一次选中的节点下标,则选中并滚动到该节点
			if (index == searchIndex - 1) {
				$('#treeTag').tree('expandTo', this.target).tree('select', this.target).tree('scrollTo', this.target);
				// 更新上一次选中的节点下标为当前下标
				searchIndex = index;
				return false;
			}
		});
	} else {
		$('#msg').html('查询结果为空!');
	}
}
/**
 * 向下查找
 */
function next() {
	// 判断查询条件是否改变
	if (searchValue !== $('#condition').searchbox('getValue')) {
		// 查询条件改变,则清空查询属性
		searchNodes = [];
		searchIndex = -1;
		searchValue = $('#condition').searchbox('getValue');
		// 判断查询条件是否为空
		if (!isEmptyStr(searchValue)) {
			// 获取符合查询条件的节点数据
			getSearchNodes(searchValue);
		}
	}
	// 判断符合查询条件的节点数组是否有数据
	if (searchNodes.length > 0) {
		$('#msg').html('');
		// 遍历符合查询条件的节点数组
		$.each(searchNodes, function(index) {
			// 判断上传查询的节点下标是否是最后一个,是则从头开始查
			if (searchIndex + 1 == searchNodes.length) {
				searchIndex = -1;
			}
			// 如果当前节点下标大于上一次选中的节点下标,则选中并滚动到该节点
			if (index == searchIndex + 1) {
				$('#treeTag').tree('expandTo', this.target).tree('select', this.target).tree('scrollTo', this.target);
				// 更新上一次选中的节点下标为当前下标
				searchIndex = index;
				return false;
			}
		});
	} else {
		$('#msg').html('查询结果为空!');
	}
}

/**
 * 设置返回值
 */
function setReturnValue() {
	// 判断是否为复选框
	if ($('#checkbox').val() == 'true' ? true : false) {
		var nodes = $('#treeTag').tree('getChecked');
		if (nodes) {
			var sname = '';
			var sid = '';
			for (var i = 0; i < nodes.length; i++) {
				if (sname != '')
					sname += ',';
				sname += nodes[i].text;
				if (sid != '')
					sid += ',';
				sid += nodes[i].id;
			}
			$("#names").val(sname);
			$("#ids").val(sid);
		}
	} else {
		var node = $('#treeTag').tree('getSelected');
		if (node) {
			$("#names").val(node.text);
			$("#ids").val(node.id);
		}
	}
}