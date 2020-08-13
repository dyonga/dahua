package com.bhyh.result;
public abstract interface Paginable
{
  public static final int DEF_MAX_RESULTS = 20;
  
  public abstract int getTotalCount();
  
  public abstract int getTotalPage();
  
  public abstract int getPageNo();
  
  public abstract int getMaxResults();
  
  public abstract int getFirstResult();
  
  public abstract boolean isFirstPage();
  
  public abstract boolean isLastPage();
  
  public abstract int getNextPage();
  
  public abstract int getPrePage();
}
