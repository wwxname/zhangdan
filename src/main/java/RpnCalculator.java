import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * 定义结构体支持多层回滚
 */
class CalDouble {
    //怎么算出来的第一个值
    CalDouble first = null;
    //怎么算出来的第二个值
    CalDouble second = null;
    //操作符，暂时没用，但是self有用，表示该值是自己进去 没有经过运算
    String operator = null;
    //自己的值
    Double selfDouble = null;
}

/**
 * @author 王文祥
 * @date 2018 11 14 21:37
 * 很多地方没有优化，含有魔法值，整体逻辑可见
 */
public class RpnCalculator {

    static Integer flag = 1;

    static Stack<CalDouble> res = new Stack<CalDouble>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String shuru = null;
        if (in.hasNext()) {
            shuru = in.nextLine();
            try {
                process(shuru);

            } catch (Exception e) {

            }
        }
        print();
        while (flag == 1) {
            if (in.hasNext()) {
                shuru = in.nextLine();
                try {
                    process(shuru);

                } catch (Exception e) {

                }
            }
            print();
        }


    }

    //处理一次输出
    static void process(String s) throws Exception {
        String[] ss = s.split("\\s+");
        for (int i = 0; i < ss.length; i++) {
            try {
                processOne(ss[i], 2 * i + 1);
            } catch (Exception e) {
                System.err.println("fatal: " + e.getMessage());
                throw e;
            }
        }

    }

    /**
     * 处理单个操作符和数子
     *
     * @param s        单个
     * @param position 位置，位置确定比较简陋 2 * i + 1
     */
    static void processOne(String s, Integer position) {

        if (s.equals("clear")) {
            res.clear();
            return;
        }
        if (s.equals("undo")) {
            CalDouble pop = null;
            if (!res.empty()) {
                pop = res.pop();
            } else {
                return;
            }
            if (!pop.operator.equals("self")) {
                res.push(pop.first);
                res.push(pop.second);
            }
            return;
        }
        CalDouble d = new CalDouble();
        if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
            CalDouble d1 = null;
            CalDouble d2 = null;
            if (!res.empty()) {
                d1 = res.pop();
            } else {
                throw new RuntimeException("operator" + " + " + "(position: " + position + "):" + "insufficient parameters stack :" +
                        "no param");
            }
            if (!res.empty()) {
                d2 = res.pop();
            } else {
                res.push(d1);
                throw new RuntimeException("operator" + " + " + "(position: " + position + "):" + "insufficient parameters stack :" +
                        d1.selfDouble);
            }
            d.first = d2;
            d.second = d1;
            if (s.equals("+")) {

                d.selfDouble = d1.selfDouble + d2.selfDouble;
                d.operator = "+";
            }
            if (s.equals("-")) {

                d.selfDouble = d2.selfDouble - d1.selfDouble;
                d.operator = "-";
            }
            if (s.equals("*")) {

                d.selfDouble = d2.selfDouble * d1.selfDouble;
                d.operator = "*";
            }
            if (s.equals("/")) {
                d.first = d2;
                d.second = d1;
                d.selfDouble = d2.selfDouble / d1.selfDouble;
                d.operator = "/";
            }
        }
        if (s.equals("sqrt")) {
            CalDouble d1 = null;
            if (!res.empty()) {
                d1 = res.pop();
            } else {
                throw new RuntimeException("operator" + " + " + "(position: " + position + "):" + "insufficient parameters stack :" +
                        "no param");
            }
            d.first = d1;
            d.selfDouble = Math.sqrt(d1.selfDouble);
            d.operator = "sqrt";
        }
        if (d.operator == null) {
            try {
                d.selfDouble = Double.valueOf(s);
            } catch (NumberFormatException e) {
                throw new RuntimeException("operator" + " 数字 " + "(position: " + position + "):" + "insufficient parameters stack :" +
                        "no param");
            }
            d.operator = "self";
        }
        res.push(d);
    }

    /**
     * 打印
     */
    static void print() {
        String s = "";
        List<CalDouble> list = res.subList(0, res.size());
        for (CalDouble d : list) {
            String dd = String.format("%.10f", d.selfDouble);
            s = s + dd + " ";
        }
        System.err.println("stack: " + s);
    }
}
