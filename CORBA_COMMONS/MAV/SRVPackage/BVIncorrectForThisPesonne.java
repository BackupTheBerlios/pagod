package MAV.SRVPackage;


/**
* MAV/SRVPackage/BVIncorrectForThisPesonne.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 22 mai 2007 18 h 17 CEST
*/

public final class BVIncorrectForThisPesonne extends org.omg.CORBA.UserException
{
  public String reason = null;

  public BVIncorrectForThisPesonne ()
  {
    super(BVIncorrectForThisPesonneHelper.id());
  } // ctor

  public BVIncorrectForThisPesonne (String _reason)
  {
    super(BVIncorrectForThisPesonneHelper.id());
    reason = _reason;
  } // ctor


  public BVIncorrectForThisPesonne (String $reason, String _reason)
  {
    super(BVIncorrectForThisPesonneHelper.id() + "  " + $reason);
    reason = _reason;
  } // ctor

} // class BVIncorrectForThisPesonne
