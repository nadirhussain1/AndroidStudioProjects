package com.prayertimes.qibla.appsourcehub.activity;


public class GregorianCalendar
{

    public GregorianCalendar()
    {
    }

    int[] chrToIsl(int i, int j, int k, int l)
    {
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        if(i > 1582 || i == 1582 && j > 10 || i == 1582 && j == 10 && k > 14)
        {
            i1 = -32075 + (k + ((intPart((1461 * (i + 4800 + intPart((j - 14) / 12))) / 4) + intPart((367 * (j - 2 - 12 * intPart((j - 14) / 12))) / 12)) - intPart((3 * intPart((i + 4900 + intPart((j - 14) / 12)) / 100)) / 4)));
        } else
        {
            i1 = 0x1a64f1 + (k + ((i * 367 - intPart((7 * (i + 5001 + intPart((j - 9) / 7))) / 4)) + intPart((j * 275) / 9)));
        }
        j1 = 10632 + (i1 - 0x1dbb18);
        k1 = intPart((j1 - 1) / 10631);
        l1 = l + (354 + (j1 - k1 * 10631));
        i2 = intPart((10985 - l1) / 5316) * intPart((l1 * 50) / 17719) + intPart(l1 / 5670) * intPart((l1 * 43) / 15238);
        j2 = 29 + (l1 - intPart((30 - i2) / 15) * intPart((i2 * 17719) / 50) - intPart(i2 / 16) * intPart((i2 * 15238) / 43));
        k2 = intPart((j2 * 24) / 709);
        return (new int[] {
            j2 - intPart((k2 * 709) / 24), k2, -30 + (i2 + k1 * 30)
        });
    }

    String[] chrToString(int i, int j, int k, int l)
    {
        String as[] = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", 
            "November", "December"
        };
        int[] _tmp = new int[3];
        int ai[] = islToChr(i, j, k, l);
        String as1[] = new String[3];
        as1[0] = Integer.toString(ai[0]);
        as1[1] = as[-1 + ai[1]];
        as1[2] = Integer.toString(ai[2]);
        return as1;
    }

    String getDayName(int i)
    {
        return (new String[] {
            "\u0627\u0644\u0633\u0628\u062A", "\u0627\u0644\u0623\u062D\u062F", "\u0627\u0644\u0623\u062B\u0646\u064A\u0646", "\u0627\u0644\u062B\u0644\u0627\u062B\u0627\u0621", "\u0627\u0644\u0623\u0631\u0628\u0639\u0627\u0621", "\u0627\u0644\u062E\u0645\u064A\u0633", "\u0627\u0644\u062C\u0645\u0639\u0629"
        })[i];
    }

    String getMonthName(int i)
    {
        return (new String[] {
            "\u0643\u0627\u0646\u0648\u0646 \u0627\u0644\u062B\u0627\u0646\u064A", "\u0634\u0628\u0627\u0637", "\u0622\u0630\u0627\u0631", "\u0646\u064A\u0633\u0627\u0646", "\u0623\u064A\u0627\u0631", "\u062D\u0632\u064A\u0631\u0627\u0646", "\u062A\u0645\u0648\u0632", "\u0622\u0628", "\u0623\u064A\u0644\u0648\u0644", "\u062A\u0634\u0631\u064A\u0646 \u0627\u0644\u0623\u0648\u0644", 
            "\u062A\u0634\u0631\u064A\u0646 \u0627\u0644\u062B\u0627\u0646\u064A", "\u0643\u0627\u0646\u0648\u0646 \u0627\u0644\u0623\u0648\u0644"
        })[i];
    }

    int intPart(int i)
    {
        if((double)(float)i < -9.9999999999999995E-008D)
        {
            return (int)Math.ceil((double)i - 9.9999999999999995E-008D);
        } else
        {
            return (int)Math.floor(9.9999999999999995E-008D + (double)i);
        }
    }

    String[] isToString(int i, int j, int k, int l)
    {
        String as[] = {
            "Muharram", "Safar", "Rabi-al Awwal", "Rabi-al Thani", "Jumada al-Ula", "Jumada al-Thani", "Rajab", "Sha'ban", "Ramadhan", "Shawwal", 
            "Dhul Qa'dah", "Dhul Hijjah"
        };
        String as1[] = {
            "\331\u2026\330\255\330\261\331\u2018\331\u2026", "\330\265\331\uFFFD?\330\261", "\330\261\330\250\331\u0160\330\271 \330\247\331\u201E\330\243\331\u02C6\331\u201E", "\330\261\330\250\331\u0160\330\271 \330\247\331\u201E\330\253\330\247\331\u2020\331" +
"\u0160"
, "\330\254\331\u2026\330\247\330\257\331\u2030 \330\247\331\u201E\330\243\331\u02C6" +
"\331\u201E\331\u2030"
, "\330\254\331\u2026\330\247\330\257\331\u2030 \330\247\331\u201E\330\253\330\247\331" +
"\u2020\331\u0160"
, "\330\261\330\254\330\250", "\330\264\330\271\330\250\330\247\331\u2020", "\330\261\331\u2026\330\266\330\247\331\u2020", "\330\264\331\u02C6\330\247\331\u201E", 
            "\330\260\331\u02C6 \330\247\331\u201E\331\u201A\330\271\330\257\330\251", "\330\260\331\u02C6 \330\247\331\u201E\330\255\330\254\330\251"
        };
        int[] _tmp = new int[3];
        int ai[] = chrToIsl(i, j, k, l);
        String as2[] = new String[4];
        as2[0] = Integer.toString(ai[0]);
        as2[1] = as[-1 + ai[1]];
        as2[2] = as1[-1 + ai[1]];
        as2[3] = Integer.toString(ai[2]);
        return as2;
    }

    int[] islToChr(int i, int j, int k, int l)
    {
        int i1 = (-385 + (0x1dbb18 + (k + ((intPart((3 + i * 11) / 30) + i * 354 + j * 30) - intPart((j - 1) / 2))))) - l;
        int l2;
        int j3;
        int k3;
        if(i1 > 0x231518)
        {
            int l3 = i1 + 0x10bd9;
            int i4 = intPart((l3 * 4) / 0x23ab1);
            int j4 = l3 - intPart((3 + 0x23ab1 * i4) / 4);
            int k4 = intPart((4000 * (j4 + 1)) / 0x164b09);
            int l4 = 31 + (j4 - intPart((k4 * 1461) / 4));
            int i5 = intPart((l4 * 80) / 2447);
            l2 = l4 - intPart((i5 * 2447) / 80);
            int j5 = intPart(i5 / 11);
            j3 = (i5 + 2) - j5 * 12;
            k3 = j5 + (k4 + 100 * (i4 - 49));
        } else
        {
            int j1 = i1 + 1402;
            int k1 = intPart((j1 - 1) / 1461);
            int l1 = j1 - k1 * 1461;
            int i2 = intPart((l1 - 1) / 365) - intPart(l1 / 1461);
            int j2 = 30 + (l1 - i2 * 365);
            int k2 = intPart((j2 * 80) / 2447);
            l2 = j2 - intPart((k2 * 2447) / 80);
            int i3 = intPart(k2 / 11);
            j3 = (k2 + 2) - i3 * 12;
            k3 = -4716 + (i3 + (i2 + k1 * 4));
        }
        return (new int[] {
            l2, j3, k3
        });
    }
}
