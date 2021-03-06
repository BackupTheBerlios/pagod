package MAV.SRVPackage;


/**
* MAV/SRVPackage/InternalErrorException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

public final class InternalErrorException extends org.omg.CORBA.UserException
{
  public String reason = null;

  public InternalErrorException ()
  {
    super(InternalErrorExceptionHelper.id());
  } // ctor

  public InternalErrorException (String _reason)
  {
    super(InternalErrorExceptionHelper.id());
    reason = _reason;
  } // ctor


  public InternalErrorException (String $reason, String _reason)
  {
    super(InternalErrorExceptionHelper.id() + "  " + $reason);
    reason = _reason;
  } // ctor

} // class InternalErrorException
