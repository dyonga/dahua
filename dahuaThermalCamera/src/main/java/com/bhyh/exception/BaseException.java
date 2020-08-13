package com.bhyh.exception;

import java.util.Arrays;



public class BaseException
  extends RuntimeException
  implements IBaseException
{
	private static final long serialVersionUID = 1L;
protected String code;
  protected Object[] values;
  
  public BaseException(String code)
  {
    this.code = code;
  }
  
  public BaseException(String code, Object... values)
  {
    this.code = code;
    this.values = values;
  }
  
  public BaseException(String code, Throwable cause)
  {
    super(cause);
    this.code = code;
  }
  
  public BaseException(String code, Throwable cause, Object... values)
  {
    super(cause);
    this.values = values;
    this.code = code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getCode()
  {
    return this.code;
  }
  
  public Object[] getValues()
  {
    return this.values;
  }
  
  public void setValues(Object[] values)
  {
    this.values = values;
  }
  
  public String getMethodName()
  {
    return getStackTrace() == null ? "" : getStackTrace()[0].getMethodName();
  }
  
  public String getStackInfo()
  {
    return Arrays.toString(getStackTrace());
  }
  
//  public String getMessage()
//  {
//    return MessageHandler.getMessage(this.code, this.values);
//  }
}
