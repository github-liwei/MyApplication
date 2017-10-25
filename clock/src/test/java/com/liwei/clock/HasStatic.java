package com.liwei.clock;

import android.app.Activity;
import com.liwei.clock.activity.UpdateUserActivity;

public class HasStatic extends UpdateUserActivity {
    private static int x = 100;

    public void uiui() {
        HasStatic hs1 = new HasStatic();
        hs1.x++;
        HasStatic hs2 = new HasStatic();
        hs2.x++;
        hs1 = new HasStatic();
        hs1.x++;
        HasStatic.x--;
    }

}
