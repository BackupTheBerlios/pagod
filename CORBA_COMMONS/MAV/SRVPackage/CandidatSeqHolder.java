package MAV.SRVPackage;


/**
* MAV/SRVPackage/CandidatSeqHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

public final class CandidatSeqHolder implements org.omg.CORBA.portable.Streamable
{
  public MAV.Candidat value[] = null;

  public CandidatSeqHolder ()
  {
  }

  public CandidatSeqHolder (MAV.Candidat[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = MAV.SRVPackage.CandidatSeqHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    MAV.SRVPackage.CandidatSeqHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return MAV.SRVPackage.CandidatSeqHelper.type ();
  }

}
