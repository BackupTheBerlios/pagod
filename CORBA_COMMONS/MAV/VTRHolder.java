package MAV;

/**
* MAV/VTRHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

public final class VTRHolder implements org.omg.CORBA.portable.Streamable
{
  public MAV.VTR value = null;

  public VTRHolder ()
  {
  }

  public VTRHolder (MAV.VTR initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = MAV.VTRHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    MAV.VTRHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return MAV.VTRHelper.type ();
  }

}
