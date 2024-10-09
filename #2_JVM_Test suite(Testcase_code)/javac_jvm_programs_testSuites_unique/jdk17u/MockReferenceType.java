
package nsk.share.jdi;

import com.sun.jdi.*;
import java.util.List;
import java.util.Map;

public class MockReferenceType implements ReferenceType {

    private final String signature;

    private final String name;

    private final VirtualMachine virtualMachine;

    private MockReferenceType(VirtualMachine virtualMachine, String signature, String name) {
        this.signature = signature;
        this.name = name;
        this.virtualMachine = virtualMachine;
    }

    @Override
    public String signature() {
        return signature;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String genericSignature() {
        return null;
    }

    @Override
    public ClassLoaderReference classLoader() {
        return null;
    }

    @Override
    public String sourceName() throws AbsentInformationException {
        return null;
    }

    @Override
    public List<String> sourceNames(String stratum) throws AbsentInformationException {
        return null;
    }

    @Override
    public List<String> sourcePaths(String stratum) throws AbsentInformationException {
        return null;
    }

    @Override
    public String sourceDebugExtension() throws AbsentInformationException {
        return null;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean isPrepared() {
        return false;
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public boolean failedToInitialize() {
        return false;
    }

    @Override
    public List<Field> fields() {
        return null;
    }

    @Override
    public List<Field> visibleFields() {
        return null;
    }

    @Override
    public List<Field> allFields() {
        return null;
    }

    @Override
    public Field fieldByName(String fieldName) {
        return null;
    }

    @Override
    public List<Method> methods() {
        return null;
    }

    @Override
    public List<Method> visibleMethods() {
        return null;
    }

    @Override
    public List<Method> allMethods() {
        return null;
    }

    @Override
    public List<Method> methodsByName(String name) {
        return null;
    }

    @Override
    public List<Method> methodsByName(String name, String signature) {
        return null;
    }

    @Override
    public List<ReferenceType> nestedTypes() {
        return null;
    }

    @Override
    public Value getValue(Field field) {
        return null;
    }

    @Override
    public Map<Field, Value> getValues(List<? extends Field> fields) {
        return null;
    }

    @Override
    public ClassObjectReference classObject() {
        return null;
    }

    @Override
    public List<Location> allLineLocations() throws AbsentInformationException {
        return null;
    }

    @Override
    public List<Location> allLineLocations(String stratum, String sourceName) throws AbsentInformationException {
        return null;
    }

    @Override
    public List<Location> locationsOfLine(int lineNumber) throws AbsentInformationException {
        return null;
    }

    @Override
    public List<Location> locationsOfLine(String stratum, String sourceName, int lineNumber) throws AbsentInformationException {
        return null;
    }

    @Override
    public List<String> availableStrata() {
        return null;
    }

    @Override
    public String defaultStratum() {
        return null;
    }

    @Override
    public List<ObjectReference> instances(long maxInstances) {
        return null;
    }

    @Override
    public int majorVersion() {
        return 0;
    }

    @Override
    public int minorVersion() {
        return 0;
    }

    @Override
    public int constantPoolCount() {
        return 0;
    }

    @Override
    public byte[] constantPool() {
        return new byte[0];
    }

    @Override
    public VirtualMachine virtualMachine() {
        return virtualMachine;
    }

    @Override
    public int modifiers() {
        return 0;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public boolean isPackagePrivate() {
        return false;
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public int compareTo(ReferenceType o) {
        return 0;
    }

    public static Value createObjectReference(VirtualMachine virtualMachine, String typeSignature, String typeName) throws Exception {
        Class clazz = Class.forName("com.sun.tools.jdi.ObjectReferenceImpl");
        java.lang.reflect.Constructor c = clazz.getDeclaredConstructor(VirtualMachine.class, long.class);
        c.setAccessible(true);
        Value objRef = (Value) c.newInstance(virtualMachine, 0);
        Type mockType = new MockReferenceType(virtualMachine, typeSignature, typeName);
        java.lang.reflect.Field typeField = clazz.getDeclaredField("type");
        typeField.setAccessible(true);
        typeField.set(objRef, mockType);
        return objRef;
    }
}
