package com.bhyh.exception;
public class CheckedException
  extends BaseException
{
 
  private static final long serialVersionUID = 1L;

  public CheckedException(String code)
  {
    super(code);
  }
	  
  public CheckedException(String code, Object[] values)
  {
    super(code, values);
  }
  
  public CheckedException(String code, Throwable cause)
  {
    super(code, cause);
  }
  
  public CheckedException(String code, Throwable cause, Object[] values)
  {
    super(code, cause, values);
  }
  
  public static void throwException(String code)
  {
    throw new CheckedException(code);
  }
  
  public static void throwException(String code, Object... values)
  {
    throw new CheckedException(code, values);
  }
  
  public static void throwException(String code, Throwable cause)
  {
    throw new CheckedException(code, cause);
  }
  
  public static void throwException(String code, Throwable cause, Object... values)
  {
    throw new CheckedException(code, cause, values);
  }
  
  public static void throwException(boolean expression, String code)
  {
    if (expression) {
      throw new CheckedException(code);
    }
  }
  
  public static void throwException(boolean expression, String code, Object... values)
  {
    if (expression) {
      throw new CheckedException(code, values);
    }
  }
  
  public static void throwException(boolean expression, String code, Throwable cause)
  {
    if (expression) {
      throw new CheckedException(code, cause);
    }
  }
  
  public static void throwException(boolean expression, String code, Throwable cause, Object... values)
  {
    if (expression) {
      throw new CheckedException(code, cause, values);
    }
  }
}
