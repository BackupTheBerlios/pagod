package MAV.SRVPackage;


/**
* MAV/SRVPackage/ResultSeqHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 22 mai 2007 18 h 36 CEST
*/

public final class ResultSeqHolder implements org.omg.CORBA.portable.Streamable
{
  public MAV.ResultVote value[] = null;

  public ResultSeqHolder ()
  {
  }

  public ResultSeqHolder (MAV.ResultVote[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = MAV.SRVPackage.ResultSeqHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    MAV.SRVPackage.ResultSeqHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return MAV.SRVPackage.ResultSeqHelper.type ();
  }

}
