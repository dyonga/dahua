package com.bhyh.result;

import java.io.Serializable;
import java.util.List;


public class Pagination
  extends SimplePage
  implements Serializable, Paginable
{
  private static final long serialVersionUID = 1L;
  private List<?> list;
  
  public Pagination() {}
  
  public Pagination(int firstResult, int maxResults, int totalCount)
  {
    super(firstResult, maxResults, totalCount);
  }
  
  public Pagination(int firstResult, int maxResults, int totalCount, List<?> paramList)
  {
    super(firstResult, maxResults, totalCount);
    this.list = paramList;
  }
  
  public List<?> getList()
  {
    return this.list;
  }
  
  public void setList(List<?> paramList)
  {
    this.list = paramList;
  }
  
  public String toString()
  {
    return 
      super.toString() + "," + "List size:" + getList().size();
  }
}
