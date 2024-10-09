
package vm.share.options;

public interface OptionObjectFactory<T> {

    public String getPlaceholder();

    public String[] getPossibleValues();

    public String getDescription();

    public String getParameterDescription(String key);

    public String getDefaultValue();

    public T getObject(String key);
}
