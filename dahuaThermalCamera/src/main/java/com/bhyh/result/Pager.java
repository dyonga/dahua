package com.bhyh.result;
public class Pager {

	/** 当前第几页 */
	protected int page = 1;
	/** 每页显示记录数 */
	protected int rows = 15;
	/** 排序字段 */
	protected String sort;
	/** 排序方式:desc/asc */
	protected String order;

	public Pager() {
	}

	/**
	 * 分页构造器
	 * @param page 当前第几页
	 * @param rows 每页显示记录数
	 */
	public Pager(int page, int rows) {
		this.page = page;
		this.rows = rows;
	}

	/**
	 * 分页构造器
	 * @param page 当前第几页
	 * @param rows 每页显示记录数
	 * @param sort 排序字段
	 * @param order 排序方式:desc/asc
	 */
	public Pager(int page, int rows, String sort, String order) {
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
	}

	/**
	 * 每页开始显示的索引号
	 * @return 索引号
	 */
	public int getFirstResult() {
		return (this.page - 1) * rows;
	}

	/**
	 * <pre>
	 * 每页显示的记录数
	 * </pre>
	 * @return 每页显示的记录数
	 */
	public int getMaxResults() {
		return rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
