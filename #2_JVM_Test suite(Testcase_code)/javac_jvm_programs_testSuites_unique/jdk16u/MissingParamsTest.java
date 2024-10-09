


public class MissingParamsTest {
    
    MissingParamsTest(int param) { }

    
    <T> MissingParamsTest() { }

    
    void missingParam(int param) { }

    
    <T> void missingTyparam() { }
}
