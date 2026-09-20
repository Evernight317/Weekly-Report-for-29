package 异常.RE异常;
/**
 * 继承自RuntimeException类
 * AgeIllegalRuntimeException
 */
public class AgeIllegalRuntimeException extends RuntimeException{

    public AgeIllegalRuntimeException() {
    }

    public AgeIllegalRuntimeException(String message) {
        super(message);
    }
    
}
