package MAV;


/**
* MAV/CandidatHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

abstract public class CandidatHelper
{
  private static String  _id = "IDL:MAV/Candidat:1.0";

  public static void insert (org.omg.CORBA.Any a, MAV.Candidat that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static MAV.Candidat extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (MAV.CandidatHelper.id (), "Candidat");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static MAV.Candidat read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_CandidatStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, MAV.Candidat value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static MAV.Candidat narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof MAV.Candidat)
      return (MAV.Candidat)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      MAV._CandidatStub stub = new MAV._CandidatStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static MAV.Candidat unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof MAV.Candidat)
      return (MAV.Candidat)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      MAV._CandidatStub stub = new MAV._CandidatStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
