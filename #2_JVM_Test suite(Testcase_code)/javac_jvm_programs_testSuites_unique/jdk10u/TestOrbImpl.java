

import java.applet.Applet;
import java.util.Properties;

import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.Environment;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UnionMember;
import org.omg.CORBA.WrongTransaction;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.portable.OutputStream;


public class TestOrbImpl extends ORB{

    @Override
    protected void set_parameters(String[] args, Properties props) {

    }

    @Override
    protected void set_parameters(Applet app, Properties props) {

    }

    @Override
    public String[] list_initial_services() {
        return null;
    }

    @Override
    public Object resolve_initial_references(String object_name)
            throws InvalidName {
        return null;
    }

    @Override
    public String object_to_string(Object obj) {
        return null;
    }

    @Override
    public Object string_to_object(String str) {
        return null;
    }

    @Override
    public NVList create_list(int count) {
        return null;
    }

    @Override
    public NamedValue create_named_value(String s, Any any, int flags) {
        return null;
    }

    @Override
    public ExceptionList create_exception_list() {
        return null;
    }

    @Override
    public ContextList create_context_list() {
        return null;
    }

    @Override
    public Context get_default_context() {
        return null;
    }

    @Override
    public Environment create_environment() {
        return null;
    }

    @Override
    public OutputStream create_output_stream() {
        return null;
    }

    @Override
    public void send_multiple_requests_oneway(Request[] req) {

    }

    @Override
    public void send_multiple_requests_deferred(Request[] req) {

    }

    @Override
    public boolean poll_next_response() {
        return false;
    }

    @Override
    public Request get_next_response() throws WrongTransaction {
        return null;
    }

    @Override
    public TypeCode get_primitive_tc(TCKind tcKind) {
        return null;
    }

    @Override
    public TypeCode create_struct_tc(String id, String name,
            StructMember[] members) {
        return null;
    }

    @Override
    public TypeCode create_union_tc(String id, String name,
            TypeCode discriminator_type, UnionMember[] members) {
        return null;
    }

    @Override
    public TypeCode create_enum_tc(String id, String name, String[] members) {
        return null;
    }

    @Override
    public TypeCode create_alias_tc(String id, String name,
            TypeCode original_type) {
        return null;
    }

    @Override
    public TypeCode create_exception_tc(String id, String name,
            StructMember[] members) {
        return null;
    }

    @Override
    public TypeCode create_interface_tc(String id, String name) {
        return null;
    }

    @Override
    public TypeCode create_string_tc(int bound) {
        return null;
    }

    @Override
    public TypeCode create_wstring_tc(int bound) {
        return null;
    }

    @Override
    public TypeCode create_sequence_tc(int bound, TypeCode element_type) {
        return null;
    }

    @Override
    public TypeCode create_recursive_sequence_tc(int bound, int offset) {
        return null;
    }

    @Override
    public TypeCode create_array_tc(int length, TypeCode element_type) {
        return null;
    }

    @Override
    public Any create_any() {
        return null;
    }

}
