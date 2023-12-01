package cn.study.model;

/**
 * 枚举策略
 */
public class getToDo {



    public static void main(String[] args) {
        String tuesday = getToDo("Monday");
        System.out.println(tuesday);
    }

    //策略枚举判断
    public static String getToDo(String day){

        CheckDay checkDay=new CheckDay();
        return checkDay.day(DayEnum.valueOf(day));
    }

}
