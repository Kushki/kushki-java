package com.kushkipagos;

/**
 * Created by acabrera on 17/01/17.
 */
public class TestDelete {

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            int a = getRandomMonthsColombia();
            if(a == 37) {
                System.out.println("================================================= 36");
            }
            if(a < 2 || a > 36) {
                System.out.println("MAL *************************************************");
            }
            System.out.println("Random==> " + getRandomMonthsColombia());
        }
    }

    public static Integer getRandomMonthsColombia() {
        return 2 + (int)(Math.random() * 35);
    }
}
