package com.bhyh.exception;

import java.io.Serializable;

public abstract interface IBaseException
  extends Serializable
{
  public abstract void setCode(String paramString);
  
  public abstract String getCode();
  
  public abstract Object[] getValues();
  
  public abstract void setValues(Object[] paramArrayOfObject);
  
  public abstract String getMessage();
  
  public abstract String getMethodName();
  
  public abstract String getStackInfo();
}
