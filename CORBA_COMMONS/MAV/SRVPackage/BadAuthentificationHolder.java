package MAV.SRVPackage;

/**
* MAV/SRVPackage/BadAuthentificationHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 27 mars 2007 18 h 57 CEST
*/

public final class BadAuthentificationHolder implements org.omg.CORBA.portable.Streamable
{
  public MAV.SRVPackage.BadAuthentification value = null;

  public BadAuthentificationHolder ()
  {
  }

  public BadAuthentificationHolder (MAV.SRVPackage.BadAuthentification initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = MAV.SRVPackage.BadAuthentificationHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    MAV.SRVPackage.BadAuthentificationHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return MAV.SRVPackage.BadAuthentificationHelper.type ();
  }

}
