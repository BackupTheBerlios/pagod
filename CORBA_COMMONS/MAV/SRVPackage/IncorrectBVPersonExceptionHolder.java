package MAV.SRVPackage;

/**
* MAV/SRVPackage/IncorrectBVPersonExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

public final class IncorrectBVPersonExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public MAV.SRVPackage.IncorrectBVPersonException value = null;

  public IncorrectBVPersonExceptionHolder ()
  {
  }

  public IncorrectBVPersonExceptionHolder (MAV.SRVPackage.IncorrectBVPersonException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = MAV.SRVPackage.IncorrectBVPersonExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    MAV.SRVPackage.IncorrectBVPersonExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return MAV.SRVPackage.IncorrectBVPersonExceptionHelper.type ();
  }

}
