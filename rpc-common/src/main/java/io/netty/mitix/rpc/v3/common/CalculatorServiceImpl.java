package io.netty.mitix.rpc.v3.common;

/**
 * 计算方法
 */
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public double add(double op1, double op2) {
        return op1 + op2;
    }

    @Override
    public double substract(double op1, double op2) {
        return op1 - op2;
    }

    @Override
    public double multiply(double op1, double op2) {
        return op1 * op2;
    }

    public static void main(String[] args) {
        CalculatorService service = new CalculatorServiceImpl();
        System.out.println(service.add(1.0, 2.0));
        System.out.println(service.substract(1.0, 2.0));
        System.out.println(service.multiply(1.0, 2.0));
    }

}
