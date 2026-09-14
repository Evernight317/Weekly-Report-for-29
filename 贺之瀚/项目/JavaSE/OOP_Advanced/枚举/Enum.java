// package OOP进阶.枚举;
/**
 * 其实就和C++的enum作用一样
 * 如果男1女0（硬编码）
 * 或者常量MAN=1 WOMAN=0（软编码）
 * 本质均为字面量判断，即判断一个int，无法控制传入参数范围，传入其他任意数字也可以运行
 * 
 * 而采用枚举作为参数，则可以限定传入的参数范围
 * 如果超出参数范围，编译期就会报错；同时，枚举的方式也能提高可读性
 * Enum
 */
public class Enum {
    public static void main(String[] args) {
        check (Constant.MAN);
    }
    public static void check (Constant sex) {
        switch (sex) {
            case MAN:
                System.out.println("有哪些好看的百合同人小说");
                break;
            case WOMAN:
                System.out.println("耽美");
                break;
            default: break;
        }
    } 
}
