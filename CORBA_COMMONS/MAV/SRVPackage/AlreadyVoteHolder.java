package MAV.SRVPackage;

/**
* MAV/SRVPackage/AlreadyVoteHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 27 mars 2007 18 h 57 CEST
*/

public final class AlreadyVoteHolder implements org.omg.CORBA.portable.Streamable
{
  public MAV.SRVPackage.AlreadyVote value = null;

  public AlreadyVoteHolder ()
  {
  }

  public AlreadyVoteHolder (MAV.SRVPackage.AlreadyVote initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = MAV.SRVPackage.AlreadyVoteHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    MAV.SRVPackage.AlreadyVoteHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return MAV.SRVPackage.AlreadyVoteHelper.type ();
  }

}