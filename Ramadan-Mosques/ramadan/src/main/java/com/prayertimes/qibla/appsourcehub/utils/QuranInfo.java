package com.prayertimes.qibla.appsourcehub.utils;


public class QuranInfo
{

    public static int JUZ_PAGE_START[] = {
        1, 22, 42, 62, 82, 102, 121, 142, 162, 182, 
        201, 222, 242, 262, 282, 302, 322, 342, 362, 382, 
        402, 422, 442, 462, 482, 502, 522, 542, 562, 582
    };
    public static int PAGE_AYAH_START[] = {
        1, 1, 6, 17, 25, 30, 38, 49, 58, 62, 
        70, 77, 84, 89, 94, 102, 106, 113, 120, 127, 
        135, 142, 146, 154, 164, 170, 177, 182, 187, 191, 
        197, 203, 211, 216, 220, 225, 231, 234, 238, 246, 
        249, 253, 257, 260, 265, 270, 275, 282, 283, 1, 
        10, 16, 23, 30, 38, 46, 53, 62, 71, 78, 
        84, 92, 101, 109, 116, 122, 133, 141, 149, 154, 
        158, 166, 174, 181, 187, 195, 1, 7, 12, 15, 
        20, 24, 27, 34, 38, 45, 52, 60, 66, 75, 
        80, 87, 92, 95, 102, 106, 114, 122, 128, 135, 
        141, 148, 155, 163, 171, 176, 3, 6, 10, 14, 
        18, 24, 32, 37, 42, 46, 51, 58, 65, 71, 
        77, 83, 90, 96, 104, 109, 114, 1, 9, 19, 
        28, 36, 45, 53, 60, 69, 74, 82, 91, 95, 
        102, 111, 119, 125, 132, 138, 143, 147, 152, 158, 
        1, 12, 23, 31, 38, 44, 52, 58, 68, 74, 
        82, 88, 96, 105, 121, 131, 138, 144, 150, 156, 
        160, 164, 171, 179, 188, 196, 1, 9, 17, 26, 
        34, 41, 46, 53, 62, 70, 1, 7, 14, 21, 
        27, 32, 37, 41, 48, 55, 62, 69, 73, 80, 
        87, 94, 100, 107, 112, 118, 123, 1, 7, 15, 
        21, 26, 34, 43, 54, 62, 71, 79, 89, 98, 
        107, 6, 13, 20, 29, 38, 46, 54, 63, 72, 
        82, 89, 98, 109, 118, 5, 15, 23, 31, 38, 
        44, 53, 64, 70, 79, 87, 96, 104, 1, 6, 
        14, 19, 29, 35, 43, 6, 11, 19, 25, 34, 
        43, 1, 16, 32, 52, 71, 91, 7, 15, 27, 
        35, 43, 55, 65, 73, 80, 88, 94, 103, 111, 
        119, 1, 8, 18, 28, 39, 50, 59, 67, 76, 
        87, 97, 105, 5, 16, 21, 28, 35, 46, 54, 
        62, 75, 84, 98, 1, 12, 26, 39, 52, 65, 
        77, 96, 13, 38, 52, 65, 77, 88, 99, 114, 
        126, 1, 11, 25, 36, 45, 58, 73, 82, 91, 
        102, 1, 6, 16, 24, 31, 39, 47, 56, 65, 
        73, 1, 18, 28, 43, 60, 75, 90, 105, 1, 
        11, 21, 28, 32, 37, 44, 54, 59, 62, 3, 
        12, 21, 33, 44, 56, 68, 1, 20, 40, 61, 
        84, 112, 137, 160, 184, 207, 1, 14, 23, 36, 
        45, 56, 64, 77, 89, 6, 14, 22, 29, 36, 
        44, 51, 60, 71, 78, 85, 7, 15, 24, 31, 
        39, 46, 53, 64, 6, 16, 25, 33, 42, 51, 
        1, 12, 20, 29, 1, 12, 21, 1, 7, 16, 
        23, 31, 36, 44, 51, 55, 63, 1, 8, 15, 
        23, 32, 40, 49, 4, 12, 19, 31, 39, 45, 
        13, 28, 41, 55, 71, 1, 25, 52, 77, 103, 
        127, 154, 1, 17, 27, 43, 62, 84, 6, 11, 
        22, 32, 41, 48, 57, 68, 75, 8, 17, 26, 
        34, 41, 50, 59, 67, 78, 1, 12, 21, 30, 
        39, 47, 1, 11, 16, 23, 32, 45, 52, 11, 
        23, 34, 48, 61, 74, 1, 19, 40, 1, 14, 
        23, 33, 6, 15, 21, 29, 1, 12, 20, 30, 
        1, 10, 16, 24, 29, 5, 12, 1, 16, 36, 
        7, 31, 52, 15, 32, 1, 27, 45, 7, 28, 
        50, 17, 41, 68, 17, 51, 77, 4, 12, 19, 
        25, 1, 7, 12, 22, 4, 10, 17, 1, 6, 
        12, 6, 1, 9, 5, 1, 10, 1, 6, 1, 
        8, 1, 13, 27, 16, 43, 9, 35, 11, 40, 
        11, 1, 14, 1, 20, 18, 48, 20, 6, 26, 
        20, 1, 31, 16, 1, 1, 1, 7, 35, 1, 
        1, 16, 1, 24, 1, 15, 1, 1, 8, 10, 
        1, 1, 1, 1
    };
    public static int PAGE_SURA_START[] = {
        1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
        2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
        3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 
        4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
        7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 
        8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 
        9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
        9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 
        10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 
        11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 
        12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 
        13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 
        14, 15, 15, 15, 15, 15, 15, 16, 16, 16, 
        16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 17, 17, 17, 17, 17, 17, 17, 17, 17, 
        17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 
        18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 
        19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 
        20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 
        21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 
        22, 23, 23, 23, 23, 23, 23, 23, 23, 24, 
        24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 
        25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 
        26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 
        27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 
        28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 
        29, 29, 29, 29, 30, 30, 30, 30, 30, 30, 
        31, 31, 31, 31, 32, 32, 32, 33, 33, 33, 
        33, 33, 33, 33, 33, 33, 33, 34, 34, 34, 
        34, 34, 34, 34, 35, 35, 35, 35, 35, 35, 
        36, 36, 36, 36, 36, 37, 37, 37, 37, 37, 
        37, 37, 38, 38, 38, 38, 38, 38, 39, 39, 
        39, 39, 39, 39, 39, 39, 39, 40, 40, 40, 
        40, 40, 40, 40, 40, 40, 41, 41, 41, 41, 
        41, 41, 42, 42, 42, 42, 42, 42, 42, 43, 
        43, 43, 43, 43, 43, 44, 44, 44, 45, 45, 
        45, 45, 46, 46, 46, 46, 47, 47, 47, 47, 
        48, 48, 48, 48, 48, 49, 49, 50, 50, 50, 
        51, 51, 51, 52, 52, 53, 53, 53, 54, 54, 
        54, 55, 55, 55, 56, 56, 56, 57, 57, 57, 
        57, 58, 58, 58, 58, 59, 59, 59, 60, 60, 
        60, 61, 62, 62, 63, 64, 64, 65, 65, 66, 
        66, 67, 67, 67, 68, 68, 69, 69, 70, 70, 
        71, 72, 72, 73, 73, 74, 74, 75, 76, 76, 
        77, 78, 78, 79, 80, 81, 82, 83, 83, 85, 
        86, 87, 89, 89, 91, 92, 95, 97, 98, 100, 
        103, 106, 109, 112
    };
    public static String SURA_DESC[] = {
        "The Opener", "The Cow", "Family of Imran", "The Women", "The Table Spread", "The Cattle", "The Heights", "The Spoils of War", "The Repentance", "Jonah", 
        "Hud", "Joseph", "The Thunder", "Abrahim", "The Rocky Tract", "The Bee", "The Night Journey", "The Cave", "Mary", "Ta-Ha", 
        "The Prophets", "The Pilgrimage", "The Believers", "The Light", "The Criterian", "The Poets", "The Ant", "The Stories", "The Spider", "The Romans", 
        "Luqman", "The Prostration", "The Combined Forces", "Sheba", "Originator", "Ya Sin", "Those who set the Ranks", "The Letter Saad", "The Troops", "The Forgiver", 
        "Explained in Detail", "The Consultation", "The Ornaments of Gold", "The Smoke", "The Crouching", "The Wind-Curved Sandhills", "Muhammad", "The Victory", "The Rooms", "The Letter Qaf", 
        "The Winnowing Winds", "The Mount", "The Star", "The Moon", "The Beneficent", "The Inevitable", "The Iron", "The Pleading Woman", "The Exile", "She that is to be examined", 
        "The Ranks", "The Congregation, Friday", "The Hypocrites", "The Mutual Disillusion", "The Divorce", "The Prohibtiion", "The Sovereignty", "The Pen", "The Reality", "The Ascending Stairways", 
        "Noah", "The Jinn", "The Enshrouded One", "The Cloaked One", "The Resurrection", "The Man", "The Emissaries", "The Tidings", "Those who drag forth", "He Frowned", 
        "The Overthrowing", "The Cleaving", "The Defrauding", "The Sundering", "The Mansions of the Stars", "The Nightcommer", "The Most High", "The Overwhelming", "The Dawn", "The City", 
        "The Sun", "The Night", "The Morning Hours", "The Relief", "The Fig", "The Clot", "The Power", "The Clear Proof", "The Earthquake", "The Courser", 
        "The Calamity", "The Rivalry in world increase", "The Declining Day", "The Traducer", "The Elephant", "Quraysh", "The Small Kindnesses", "The Abundance", "The Disbelievers", "The Divine Support", 
        "The Palm Fiber, Flame", "The Sincerity", "The Daybreak", "The Mankind"
    };
    public static boolean SURA_IS_MAKKI[];
    public static String SURA_NAMES[] = {
        "Al-Fatiha", "Al-Baqara", "Aal-E-Imran", "An-Nisa", "Al-Maeda", "Al-Anaam", "Al-Araf", "Al-Anfal", "At-Tawba", "Yunus", 
        "Hud", "Yusuf", "Ar-Rad", "Ibrahim", "Al-Hijr", "An-Nahl", "Al-Isra", "Al-Kahf", "Maryam", "Ta-Ha", 
        "Al-Anbiya", "Al-Hajj", "Al-Mumenoon", "An-Noor", "Al-Furqan", "Ash-Shu'ara", "An-Naml", "Al-Qasas", "Al-Ankaboot", "Ar-Room", 
        "Luqman", "As-Sajda", "Al-Ahzab", "Saba", "Fatir", "Ya-Seen", "As-Saaffat", "Sad", "Az-Zumar", "Ghafir", 
        "Fussilat", "Ash-Shura", "Az-Zukhruf", "Ad-Dukhan", "Al-Jathiya", "Al-Ahqaf", "Muhammad", "Al-Fath", "Al-Hujraat", "Qaf", 
        "Adh-Dhariyat", "At-Tur", "An-Najm", "Al-Qamar", "Ar-Rahman", "Al-Waqia", "Al-Hadid", "Al-Mujadila", "Al-Hashr", "Al-Mumtahina", 
        "As-Saff", "Al-Jumua", "Al-Munafiqoon", "At-Taghabun", "At-Talaq", "At-Tahrim", "Al-Mulk", "Al-Qalam", "Al-Haaqqa", "Al-Maarij", 
        "Nooh", "Al-Jinn", "Al-Muzzammil", "Al-Muddaththir", "Al-Qiyama", "Al-Insan", "Al-Mursalat", "An-Naba", "An-Naziat", "Abasa", 
        "At-Takwir", "Al-Infitar", "Al-Mutaffifin", "Al-Inshiqaq", "Al-Burooj", "At-Tariq", "Al-Ala", "Al-Ghashiya", "Al-Fajr", "Al-Balad", 
        "Ash-Shams", "Al-Lail", "Ad-Dhuha", "Al-Inshirah", "At-Tin", "Al-Alaq", "Al-Qadr", "Al-Bayyina", "Az-Zalzala", "Al-Adiyat", 
        "Al-Qaria", "At-Takathur", "Al-Asr", "Al-Humaza", "Al-Fil", "Quraish", "Al-Maun", "Al-Kauther", "Al-Kafiroon", "An-Nasr", 
        "Al-Masadd", "Al-Ikhlas", "Al-Falaq", "An-Nas"
    };
    public static String SURA_NAMES_AR[] = {
        "\u0627\u0644\u0641\u0627\u062A\u062D\u0629", "\u0627\u0644\u0628\u0642\u0631\u0629", "\u0622\u0644 \u0639\u0645\u0631\u0627\u0646", "\u0627\u0644\u0646\u0633\u0627\u0621", "\u0627\u0644\u0645\u0627\u0626\u062F\u0629", "\u0627\uFEF7\u0646\u0639\u0627\u0645", "\u0627\uFEF7\u0639\u0631\u0627\u0641", "\u0627\uFEF7\u0646\u0641\u0627\u0644", "\u0627\u0644\u062A\u0648\u0628\u0629", "\u064A\u0648\u0646\u0633", 
        "\u0647\u0648\u062F", "\u064A\u0648\u0633\u0641", "\u0627\u0644\u0631\u0639\u062F", "\u0625\u0628\u0631\u0627\u0647\u064A\u0645", "\u0627\u0644\u062D\u062C\u0631", "\u0627\u0644\u0646\u062D\u0644", "\u0627\uFEF9\u0633\u0631\u0627\u0621", "\u0627\u0644\u0643\u0647\u0641", "\u0645\u0631\u064A\u0645", "\u0637\u0647", 
        "\u0627\uFEF7\u0646\u0628\u064A\u0627\u0621", "\u0627\u0644\u062D\u062C", "\u0627\u0644\u0645\u0624\u0645\u0646\u0648\u0646", "\u0627\u0644\u0646\u0648\u0631", "\u0627\u0644\u0641\u0631\u0642\u0627\u0646", "\u0627\u0644\u0634\u0639\u0631\u0627\u0621", "\u0627\u0644\u0646\u0645\u0644", "\u0627\u0644\u0642\u0635\u0635", "\u0627\u0644\u0639\u0646\u0643\u0628\u0648\u062A", "\u0627\u0644\u0631\u0648\u0645", 
        "\u0644\u0642\u0645\u0627\u0646", "\u0627\u0644\u0633\u062C\u062F\u0629", "\u0627\uFEF7\u062D\u0632\u0627\u0628", "\u0633\u0628\u0623", "\u0641\u0627\u0637\u0631", "\u064A\u0633", "\u0627\u0644\u0635\u0627\u0641\u0627\u062A", "\u0635", "\u0627\u0644\u0632\u0645\u0631", "\u063A\u0627\u0641\u0631", 
        "\u0641\u0635\u0644\u062A", "\u0627\u0644\u0634\u0648\u0631\u0649", "\u0627\u0644\u0632\u062E\u0631\u0641", "\u0627\u0644\u062F\u062E\u0627\u0646", "\u0627\u0644\u062C\u0627\u062B\u064A\u0629", "\u0627\uFEF7\u062D\u0642\u0627\u0641", "\u0645\u062D\u0645\u062F", "\u0627\u0644\u0641\u062A\u062D", "\u0627\u0644\u062D\u062C\u0631\u0627\u062A", "\u0642", 
        "\u0627\u0644\u0630\u0627\u0631\u064A\u0627\u062A", "\u0627\u0644\u0637\u0648\u0631", "\u0627\u0644\u0646\u062C\u0645", "\u0627\u0644\u0642\u0645\u0631", "\u0627\u0644\u0631\u062D\u0645\u0646", "\u0627\u0644\u0648\u0627\u0642\u0639\u0629", "\u0627\u0644\u062D\u062F\u064A\u062F", "\u0627\u0644\u0645\u062C\u0627\u062F\u0644\u0629", "\u0627\u0644\u062D\u0634\u0631", "\u0627\u0644\u0645\u0645\u062A\u062D\u0646\u0629", 
        "\u0627\u0644\u0635\u0641", "\u0627\u0644\u062C\u0645\u0639\u0629", "\u0627\u0644\u0645\u0646\u0627\u0641\u0642\u0648\u0646", "\u0627\u0644\u062A\u063A\u0627\u0628\u0646", "\u0627\u0644\u0637\u0644\u0627\u0642", "\u0627\u0644\u062A\u062D\u0631\u064A\u0645", "\u0627\u0644\u0645\u0644\u0643", "\u0627\u0644\u0642\u0644\u0645", "\u0627\u0644\u062D\u0627\u0642\u0629", "\u0627\u0644\u0645\u0639\u0627\u0631\u062C", 
        "\u0646\u0648\u062D", "\u0627\u0644\u062C\u0646", "\u0627\u0644\u0645\u0632\u0645\u0644", "\u0627\u0644\u0645\u062F\u062B\u0631", "\u0627\u0644\u0642\u064A\u0627\u0645\u0629", "\u0627\uFEF9\u0646\u0633\u0627\u0646", "\u0627\u0644\u0645\u0631\u0633\u0644\u0627\u062A", "\u0627\u0644\u0646\u0628\u0623", "\u0627\u0644\u0646\u0627\u0632\u0639\u0627\u062A", "\u0639\u0628\u0633", 
        "\u0627\u0644\u062A\u0643\u0648\u064A\u0631", "\u0627\u0644\u0627\u0646\u0641\u0637\u0627\u0631", "\u0627\u0644\u0645\u0637\u0641\u0641\u064A\u0646", "\u0627\u0644\u0627\u0646\u0634\u0642\u0627\u0642", "\u0627\u0644\u0628\u0631\u0648\u062C", "\u0627\u0644\u0637\u0627\u0631\u0642", "\u0627\uFEF7\u0639\u0644\u0649", "\u0627\u0644\u063A\u0627\u0634\u064A\u0629", "\u0627\u0644\u0641\u062C\u0631", "\u0627\u0644\u0628\u0644\u062F", 
        "\u0627\u0644\u0634\u0645\u0633", "\u0627\u0644\u0644\u064A\u0644", "\u0627\u0644\u0636\u062D\u0649", "\u0627\u0644\u0634\u0631\u062D", "\u0627\u0644\u062A\u064A\u0646", "\u0627\u0644\u0639\u0644\u0642", "\u0627\u0644\u0642\u062F\u0631", "\u0627\u0644\u0628\u064A\u0646\u0629", "\u0627\u0644\u0632\u0644\u0632\u0644\u0629", "\u0627\u0644\u0639\u0627\u062F\u064A\u0627\u062A", 
        "\u0627\u0644\u0642\u0627\u0631\u0639\u0629", "\u0627\u0644\u062A\u0643\u0627\u062B\u0631", "\u0627\u0644\u0639\u0635\u0631", "\u0627\u0644\u0647\u0645\u0632\u0629", "\u0627\u0644\u0641\u064A\u0644", "\u0642\u0631\u064A\u0634", "\u0627\u0644\u0645\u0627\u0639\u0648\u0646", "\u0627\u0644\u0643\u0648\u062B\u0631", "\u0627\u0644\u0643\u0627\u0641\u0631\u0648\u0646", "\u0627\u0644\u0646\u0635\u0631", 
        "\u0627\u0644\u0645\u0633\u062F", "\u0627\uFEF9\u062E\u0644\u0627\u0635", "\u0627\u0644\u0641\u0644\u0642", "\u0627\u0644\u0646\u0627\u0633"
    };
    public static int SURA_NUM_AYAHS[] = {
        7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 
        123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 
        112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 
        34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 
        54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 
        60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 
        14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 
        28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 
        29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 
        15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 
        11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 
        5, 4, 5, 6
    };
    public static int SURA_PAGE_START[] = {
        1, 2, 50, 77, 106, 128, 151, 177, 187, 208, 
        221, 235, 249, 255, 262, 267, 282, 293, 305, 312, 
        322, 332, 342, 350, 359, 367, 377, 385, 396, 404, 
        411, 415, 418, 428, 434, 440, 446, 453, 458, 467, 
        477, 483, 489, 496, 499, 502, 507, 511, 515, 518, 
        520, 523, 526, 528, 531, 534, 537, 542, 545, 549, 
        551, 553, 554, 556, 558, 560, 562, 564, 566, 568, 
        570, 572, 574, 575, 577, 578, 580, 582, 583, 585, 
        586, 587, 587, 589, 590, 591, 591, 592, 593, 594, 
        595, 595, 596, 596, 597, 597, 598, 598, 599, 599, 
        600, 600, 601, 601, 601, 602, 602, 602, 603, 603, 
        603, 604, 604, 604
    };

    public QuranInfo()
    {
    }

    static 
    {
        boolean aflag[] = new boolean[115];
        aflag[0] = true;
        aflag[5] = true;
        aflag[6] = true;
        aflag[9] = true;
        aflag[10] = true;
        aflag[11] = true;
        aflag[13] = true;
        aflag[14] = true;
        aflag[15] = true;
        aflag[16] = true;
        aflag[17] = true;
        aflag[18] = true;
        aflag[19] = true;
        aflag[20] = true;
        aflag[22] = true;
        aflag[24] = true;
        aflag[25] = true;
        aflag[26] = true;
        aflag[27] = true;
        aflag[28] = true;
        aflag[29] = true;
        aflag[30] = true;
        aflag[31] = true;
        aflag[33] = true;
        aflag[34] = true;
        aflag[35] = true;
        aflag[36] = true;
        aflag[37] = true;
        aflag[38] = true;
        aflag[39] = true;
        aflag[40] = true;
        aflag[41] = true;
        aflag[42] = true;
        aflag[43] = true;
        aflag[44] = true;
        aflag[45] = true;
        aflag[49] = true;
        aflag[50] = true;
        aflag[51] = true;
        aflag[52] = true;
        aflag[53] = true;
        aflag[55] = true;
        aflag[66] = true;
        aflag[67] = true;
        aflag[68] = true;
        aflag[69] = true;
        aflag[70] = true;
        aflag[71] = true;
        aflag[72] = true;
        aflag[73] = true;
        aflag[74] = true;
        aflag[76] = true;
        aflag[77] = true;
        aflag[78] = true;
        aflag[79] = true;
        aflag[80] = true;
        aflag[81] = true;
        aflag[82] = true;
        aflag[83] = true;
        aflag[84] = true;
        aflag[85] = true;
        aflag[86] = true;
        aflag[87] = true;
        aflag[88] = true;
        aflag[89] = true;
        aflag[90] = true;
        aflag[91] = true;
        aflag[92] = true;
        aflag[93] = true;
        aflag[94] = true;
        aflag[95] = true;
        aflag[96] = true;
        aflag[97] = true;
        aflag[100] = true;
        aflag[101] = true;
        aflag[102] = true;
        aflag[103] = true;
        aflag[104] = true;
        aflag[105] = true;
        aflag[106] = true;
        aflag[107] = true;
        aflag[108] = true;
        aflag[109] = true;
        aflag[111] = true;
        aflag[112] = true;
        aflag[113] = true;
        aflag[114] = true;
        SURA_IS_MAKKI = aflag;
    }
}
