package MAV.SRVPackage;


/**
* MAV/SRVPackage/BadAuthentification.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 27 mars 2007 18 h 57 CEST
*/

public final class BadAuthentification extends org.omg.CORBA.UserException
{
  public String reason = null;

  public BadAuthentification ()
  {
    super(BadAuthentificationHelper.id());
  } // ctor

  public BadAuthentification (String _reason)
  {
    super(BadAuthentificationHelper.id());
    reason = _reason;
  } // ctor


  public BadAuthentification (String $reason, String _reason)
  {
    super(BadAuthentificationHelper.id() + "  " + $reason);
    reason = _reason;
  } // ctor

} // class BadAuthentification
