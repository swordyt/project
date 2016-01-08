package com.travelsky.autotest.autosky.exception;

public class StepException extends RuntimeException
//public class StepException extends Exception
{
  private static final long serialVersionUID = 3341764449059630537L;

  public StepException()
  {
  }

  public StepException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public StepException(String message)
  {
    super(message);
  }

  public StepException(Throwable cause)
  {
    super(cause);
  }
}