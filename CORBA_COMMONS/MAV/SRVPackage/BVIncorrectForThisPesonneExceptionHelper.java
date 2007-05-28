package MAV.SRVPackage;


/**
* MAV/SRVPackage/BVIncorrectForThisPesonneExceptionHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 22 mai 2007 18 h 18 CEST
*/

abstract public class BVIncorrectForThisPesonneExceptionHelper
{
  private static String  _id = "IDL:MAV/SRV/BVIncorrectForThisPesonneException:1.0";

  public static void insert (org.omg.CORBA.Any a, MAV.SRVPackage.BVIncorrectForThisPesonneException that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static MAV.SRVPackage.BVIncorrectForThisPesonneException extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "reason",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (MAV.SRVPackage.BVIncorrectForThisPesonneExceptionHelper.id (), "BVIncorrectForThisPesonneException", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static MAV.SRVPackage.BVIncorrectForThisPesonneException read (org.omg.CORBA.portable.InputStream istream)
  {
    MAV.SRVPackage.BVIncorrectForThisPesonneException value = new MAV.SRVPackage.BVIncorrectForThisPesonneException ();
    // read and discard the repository ID
    istream.read_string ();
    value.reason = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, MAV.SRVPackage.BVIncorrectForThisPesonneException value)
  {
    // write the repository ID
    ostream.write_string (id ());
    ostream.write_string (value.reason);
  }

}
