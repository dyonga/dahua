package com.bhyh.result;

public class SimplePage
  implements Paginable
{
  private int totalCount;
  private int maxResults = 20;
  private int firstResult;
  
  public SimplePage() {}
  
  public SimplePage(int firstResult, int maxResults, int totalCount)
  {
    this.firstResult = (firstResult <= 0 ? 0 : firstResult);
    this.maxResults = (maxResults <= 0 ? 20 : maxResults);
    this.totalCount = (totalCount <= 0 ? 0 : totalCount);
  }
  
  public void adjustPage()
  {
    if (this.totalCount < 0) {
      this.totalCount = 0;
    }
    if (this.maxResults <= 0) {
      this.maxResults = 20;
    }
    if (this.firstResult < 0) {
      this.firstResult = 0;
    }
  }
  
  public int getPageNo()
  {
    return this.firstResult / this.maxResults + 1;
  }
  
  public int getMaxResults()
  {
    return this.maxResults;
  }
  
  public int getFirstResult()
  {
    return this.firstResult;
  }
  
  public int getTotalCount()
  {
    return this.totalCount;
  }
  
  public int getTotalPage()
  {
    int i = this.totalCount / this.maxResults;
    if ((this.totalCount % this.maxResults != 0) || (i == 0)) {
      i++;
    }
    return i;
  }
  
  public boolean isFirstPage()
  {
    return getPageNo() <= 1;
  }
  
  public boolean isLastPage()
  {
    return getPageNo() >= getTotalPage();
  }
  
  public int getNextPage()
  {
    if (isLastPage()) {
      return getPageNo();
    }
    return getPageNo() + 1;
  }
  
  public int getPrePage()
  {
    if (isFirstPage()) {
      return getPageNo();
    }
    return getPageNo() - 1;
  }
  
  public void setTotalCount(int paramInt)
  {
    this.totalCount = paramInt;
  }
  
  public void setMaxResults(int maxResults)
  {
    this.maxResults = maxResults;
  }
  
  public void setFirstResult(int firstResult)
  {
    this.firstResult = firstResult;
  }
  
  public String toString()
  {
    return 
    

      "totalCount:" + getTotalCount() + ",totalPage:" + getTotalPage() + ",pageNo:" + getPageNo() + ",firstResult" + getFirstResult() + ",maxResults:" + getMaxResults() + ",nextPage:" + getNextPage() + ",prePage:" + getPrePage();
  }
}
