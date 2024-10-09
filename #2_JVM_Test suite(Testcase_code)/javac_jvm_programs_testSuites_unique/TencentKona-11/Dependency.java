

class Dependency {
    private String methodName;
    private String methodDescriptor;
    private String target;

    public Dependency(String methodName, String methodDescriptor, String target) {
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Dependency)) {
            return false;
        }

        Dependency other = (Dependency)o;
        return target.equals(other.target) &&
               methodName.equals(other.methodName) &&
               methodDescriptor.equals(other.methodDescriptor);
    }

    @Override
    public int hashCode() {
        return methodName.hashCode() ^ methodDescriptor.hashCode() ^ target.hashCode();
    }

    public String getMethodName() {
        return methodName;
    }
}
