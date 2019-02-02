package com.brainpixel.cletracker.utils;

import android.app.Activity;

import com.brainpixel.cletracker.model.Category;
import com.brainpixel.cletracker.model.Cycle;
import com.brainpixel.cletracker.model.Requirements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nadirhussain on 20/05/2017.
 */

public class CycleReqsManagerUtil {
    private CycleReqsManagerUtil() {

    }

    public static Cycle getCurrentCycleForTexas(Calendar admissionCalendar, Calendar birthCalendar) {
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        int admissionYear = admissionCalendar.get(Calendar.YEAR);
        int birthMonth = birthCalendar.get(Calendar.MONTH);

        float admissionYearsDiff = getDiffInYears(Calendar.getInstance(Locale.US), admissionCalendar);
        int cycleStartYear;
        if (birthMonth <= admissionMonth) {
            cycleStartYear = admissionYear + 1;
            admissionCalendar.set(cycleStartYear, birthMonth, 1);
        } else {
            cycleStartYear = admissionYear;
            admissionCalendar.set(cycleStartYear, birthMonth, 1);
        }

        float birthMonthYearsDiff = getDiffInYears(Calendar.getInstance(Locale.US), admissionCalendar);
        if (admissionYearsDiff <= 3 && birthMonthYearsDiff <= 2) {
            return getNewlyAdmittedCycleForTexas(cycleStartYear, birthMonth);
        } else {
            return getExperiencedCycleForTexas(cycleStartYear, birthMonth);
        }
    }

    private static Cycle getNewlyAdmittedCycleForTexas(int cycleStartYear, int birthMonth) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(cycleStartYear + 2, birthMonth, 1);
        int lastDateOfMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String minDate = "" + (birthMonth + 1) + "/" + "01" + "/" + (cycleStartYear - 1);
        String startDate = "" + (birthMonth + 1) + "/" + "01" + "/" + cycleStartYear;
        String endDate = "" + (birthMonth + 1) + "/" + "" + lastDateOfMonth + "/" + (cycleStartYear + 2);
        String title = "Newly Admitted Attorney";

        return new Cycle(title, startDate, endDate, minDate, createTexasCycleRequirements());
    }

    private static Cycle getExperiencedCycleForTexas(int cycleStartYear, int birthMonth) {
        cycleStartYear += 2; // add 2 years of newly admitted

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(cycleStartYear + 1, birthMonth, 1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        while (endCalendar.getTime().before(new Date())) {
            cycleStartYear++;
            endCalendar.set(cycleStartYear + 1, birthMonth, 1);
            endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        int lastDateOfMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String startDate = "" + (birthMonth + 1) + "/" + "01" + "/" + cycleStartYear;
        String endDate = "" + (birthMonth + 1) + "/" + "" + lastDateOfMonth + "/" + (cycleStartYear + 1);
        String minDate = "" + (birthMonth + 1) + "/" + "01" + "/" + (cycleStartYear - 3); // 1 year prior to the newly admitted cycle
        String title = "Experienced Attorney";

        return new Cycle(title, startDate, endDate, minDate, createTexasCycleRequirements());
    }

    private static Requirements createTexasCycleRequirements() {
        ArrayList<Category> requiredCategories = new ArrayList<>();
        Category ethicsCategory = new Category("ethics or professional responsibility", 3);
        Category othersCategory = new Category("General", 12);

        requiredCategories.add(ethicsCategory);
        requiredCategories.add(othersCategory);

        return new Requirements(15, requiredCategories);
    }

    public static Cycle getCurrentCycleForNewYork(Calendar admissionCalendar, Calendar birthCalendar) {
        float admissionYearsDiff = getDiffInYears(Calendar.getInstance(Locale.US), admissionCalendar);
        if (admissionYearsDiff <= 1) {
            return getNewYorkNewlyAdmittedFirstCycle(admissionCalendar);
        } else if (admissionYearsDiff <= 2) {
            return getNewYorkNewlyAdmitted2ndCycle(admissionCalendar);
        } else {
            return getNewYorkExperiencedCycle(admissionYearsDiff, admissionCalendar, birthCalendar);
        }
    }

    private static Cycle getNewYorkNewlyAdmittedFirstCycle(Calendar admissionCalendar) {
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        int admissionYear = admissionCalendar.get(Calendar.YEAR);
        int admissionDay = admissionCalendar.get(Calendar.DAY_OF_MONTH);


        String minDate = "" + (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear - 1);
        String startDate = "" + (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear);
        String endDate = (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear + 1);

        String title = "Newly Admitted Attorney: 1st Cycle";
        return new Cycle(title, startDate, endDate, minDate, getNewYorNewlyAdmittedReqs());
    }

    private static Cycle getNewYorkNewlyAdmitted2ndCycle(Calendar admissionCalendar) {
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        int admissionYear = admissionCalendar.get(Calendar.YEAR);
        int admissionDay = admissionCalendar.get(Calendar.DAY_OF_MONTH);

        String minDate = "" + (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear - 1);
        String startDate = "" + (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear + 1);
        String endDate = "" + (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear + 2);
        String title = "Newly Admitted Attorney: 2nd Cycle";
        return new Cycle(title, startDate, endDate, minDate, getNewYorNewlyAdmittedReqs());
    }

    private static Cycle getNewYorkExperiencedCycle(float admissionYearsDiff, Calendar admissionCalendar, Calendar birthCalendar) {
        int admissionDay = admissionCalendar.get(Calendar.DAY_OF_MONTH);
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        int admissionYear = admissionCalendar.get(Calendar.YEAR);

        int birthDay = birthCalendar.get(Calendar.DAY_OF_MONTH);
        int birthMonth = birthCalendar.get(Calendar.MONTH);


        int addOffsetInAdmission = ((int) admissionYearsDiff / 2) * 2;
        int cycleStartYear = admissionYear + addOffsetInAdmission;

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.set(cycleStartYear + 2, birthMonth, birthDay);
        while(endDateCalendar.getTime().before(new Date())){
            cycleStartYear+=2;
            endDateCalendar.set(cycleStartYear + 2, birthMonth, birthDay);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(cycleStartYear, birthMonth, birthDay);

        String minDate = "" + (admissionMonth + 1) + "/" + admissionDay + "/" + (admissionYear - 1);
        String startDate = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);


        String endDate = (endDateCalendar.get(Calendar.MONTH) + 1) + "/" + endDateCalendar.get(Calendar.DAY_OF_MONTH) + "/" + endDateCalendar.get(Calendar.YEAR);
        String title = "Experienced Attorney";

        return new Cycle(title, startDate, endDate, minDate, getNewYorExperiencedReqs());
    }

    private static Requirements getNewYorNewlyAdmittedReqs() {
        ArrayList<Category> requiredCategories = new ArrayList<>();

        Category ethicsCategory = new Category("Ethics/Professionalism", 3);
        Category skillsCategory = new Category("Skills", 6);
        Category practiceCategory = new Category("Law Practice", 7);

        requiredCategories.add(ethicsCategory);
        requiredCategories.add(skillsCategory);
        requiredCategories.add(practiceCategory);

        return new Requirements(16, requiredCategories);
    }

    private static Requirements getNewYorExperiencedReqs() {
        ArrayList<Category> requiredCategories = new ArrayList<>();
        Category ethicsCategory = new Category("Ethics/Professionalism", 4);
        Category othersCategory = new Category("General", 20);
        requiredCategories.add(ethicsCategory);
        requiredCategories.add(othersCategory);

        return new Requirements(24, requiredCategories);
    }

    public static Cycle getCourseCycleForIllinois(Calendar admissionCalendar, String lastName) {
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        Calendar currentCalendar = Calendar.getInstance(Locale.US);
        float admissionYearsDiff = getDiffInYears(currentCalendar, admissionCalendar);
        if (admissionYearsDiff <= 1 || (currentCalendar.get(Calendar.MONTH) == admissionMonth && admissionYearsDiff < 1.1)) {
            return getNewlyAdmittedIllinoisCycle(admissionCalendar);
        } else {
            return getExperiencedIllinoisCycle(admissionCalendar, lastName);
        }
    }

    private static Cycle getNewlyAdmittedIllinoisCycle(Calendar admissionCalendar) {
        int admissionDay = admissionCalendar.get(Calendar.DAY_OF_MONTH);
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        int admissionYear = admissionCalendar.get(Calendar.YEAR);

        Calendar endDateCalendar = Calendar.getInstance(Locale.US);
        endDateCalendar.set(admissionYear, admissionMonth, admissionDay);
        endDateCalendar.add(Calendar.YEAR, 1);

        String startDate = (admissionCalendar.get(Calendar.MONTH) + 1) + "/" + admissionCalendar.get(Calendar.DAY_OF_MONTH) + "/" + admissionCalendar.get(Calendar.YEAR);
        ;
        String endDate = (endDateCalendar.get(Calendar.MONTH) + 1) + "/" + endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + endDateCalendar.get(Calendar.YEAR);
        String minDate = startDate;
        String title = "Newly Admitted Attorney";

        return new Cycle(title, startDate, endDate, minDate, getIllinoisNewlyAdmittedReqs());
    }

    private static Cycle getExperiencedIllinoisCycle(Calendar admissionCalendar, String lastName) {
        int admissionMonth = admissionCalendar.get(Calendar.MONTH);
        int admissionYear = admissionCalendar.get(Calendar.YEAR);

        int completionYearOfLastCycle = admissionYear + 1;
        int startYearOfExpCycle = completionYearOfLastCycle;

        boolean iseven = ((startYearOfExpCycle % 2) == 0);
        if (lastName.toUpperCase().matches("^[A-M].*$")) {
            if (!iseven) {
                startYearOfExpCycle += 1;
            } else if (admissionMonth >= 6) {
                //As last cycle end in July or after that , so we have to start it 2 years later.
                startYearOfExpCycle += 2;
            }
        } else {
            if (iseven) {
                startYearOfExpCycle += 1;
            } else if (admissionMonth >= 6) {
                startYearOfExpCycle += 2;
            }

        }

        int endYear = startYearOfExpCycle + 2;
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.set(endYear, 7, 30);

        while (endDateCalendar.getTime().before(new Date())) {
            startYearOfExpCycle = startYearOfExpCycle + 2;
            endYear = startYearOfExpCycle + 2;
            endDateCalendar.set(endYear, 7, 30);
        }


        String startDate = "7" + "/" + "1" + "/" + startYearOfExpCycle;
        // Add 2 years , set month to June and Day to 30th for endDate
        String endDate = "6" + "/" + "30" + "/" + endYear;
        String minDate = startDate;
        String title = "Experienced Attorney";
        return new Cycle(title, startDate, endDate, minDate, getIllinoisExpeReqs());

    }

    private static Requirements getIllinoisNewlyAdmittedReqs() {
        Category basicSkillCategory = new Category("Basic Skill Course", 1);
        Category otherCategory = new Category("General", 9);

        ArrayList<Category> requiredCategories = new ArrayList<>();
        requiredCategories.add(basicSkillCategory);
        requiredCategories.add(otherCategory);

        return new Requirements(10, requiredCategories);
    }

    private static Requirements getIllinoisExpeReqs() {
        Category profResponsibility = new Category("Professional Responsibility", 6);
        Category otherCategory = new Category("General", 24);

        ArrayList<Category> requiredCategories = new ArrayList<>();
        requiredCategories.add(profResponsibility);
        requiredCategories.add(otherCategory);

        return new Requirements(30, requiredCategories);
    }

    public static Cycle getCurrentCycleForCalifornia(Activity activity, Calendar admissionCalendar, String lastName) {
        Calendar firstCycleEndDateCalendar = Calendar.getInstance(Locale.US);
        if (lastName.toUpperCase().matches("^[A-G].*$")) {
            firstCycleEndDateCalendar.set(2013, 0, 31);
            firstCycleEndDateCalendar = getCaliforniaPeriodEnd(admissionCalendar, firstCycleEndDateCalendar);

        } else if (lastName.toUpperCase().matches("^[H-M].*$")) {
            firstCycleEndDateCalendar.set(2015, 0, 31);
            firstCycleEndDateCalendar = getCaliforniaPeriodEnd(admissionCalendar, firstCycleEndDateCalendar);

        } else {
            firstCycleEndDateCalendar.set(2014, 0, 31);
            firstCycleEndDateCalendar = getCaliforniaPeriodEnd(admissionCalendar, firstCycleEndDateCalendar);
        }

        if (!firstCycleEndDateCalendar.getTime().before(new Date())) {
            return getNewlyAdmittedCaliforniaCycle(activity, firstCycleEndDateCalendar, admissionCalendar);
        } else {
            return getExperiencedCaliforniaCycle(firstCycleEndDateCalendar);
        }

    }

    private static Cycle getNewlyAdmittedCaliforniaCycle(Activity activity, Calendar firstCycleEndDateCalendar, Calendar admissionCalendar) {
        Requirements requirements;
        String title = "Newly Admitted Attorney";
        String endDate = (firstCycleEndDateCalendar.get(Calendar.MONTH) + 1) + "/" + firstCycleEndDateCalendar.get(Calendar.DAY_OF_MONTH) + "/" + firstCycleEndDateCalendar.get(Calendar.YEAR);
        int admissionDiffFromStartDateInMonths = getDiffInMonths(firstCycleEndDateCalendar, admissionCalendar);

        Calendar firstCycleStartYearCalendar = Calendar.getInstance();
        int startYear = firstCycleEndDateCalendar.get(Calendar.YEAR) - 3;
        firstCycleStartYearCalendar.set(startYear, 1, 1);


        String startDate = (firstCycleStartYearCalendar.get(Calendar.MONTH) + 1) + "/" + firstCycleStartYearCalendar.get(Calendar.DAY_OF_MONTH) + "/" + firstCycleStartYearCalendar.get(Calendar.YEAR);
        String resKeyName = "months_" + admissionDiffFromStartDateInMonths;
        int totalHours = getTotalHours(activity, resKeyName);

        if (admissionDiffFromStartDateInMonths >= 28) {
            requirements = getCaliforniaCycleRequirement(totalHours, 4, 1, 1, totalHours - 6);
        } else if (admissionDiffFromStartDateInMonths >= 19) {
            requirements = getCaliforniaCycleRequirement(totalHours, 3, 1, 1, totalHours - 5);
        } else if (admissionDiffFromStartDateInMonths >= 10) {
            requirements = getCaliforniaCycleRequirement(totalHours, 2, 1, 1, totalHours - 4);
        } else if (admissionDiffFromStartDateInMonths >= 5) {
            requirements = getCaliforniaCycleRequirement(totalHours, 1, 1, 1, totalHours - 3);
        } else {
            requirements = getCaliforniaCycleRequirement(totalHours, 0, 0, 0, 0);
        }

        return new Cycle(title, startDate, endDate, startDate, requirements);


    }

    private static int getTotalHours(Activity activity, String integerName) {
        int resId = activity.getResources().getIdentifier(integerName, "integer", activity.getPackageName());
        return activity.getResources().getInteger(resId);
    }


    private static Cycle getExperiencedCaliforniaCycle(Calendar firstCycleEndDateCalendar) {
        String title = "Experienced Attorney";
        firstCycleEndDateCalendar.add(Calendar.YEAR, 3);
        while(firstCycleEndDateCalendar.getTime().before(new Date())){
            firstCycleEndDateCalendar.add(Calendar.YEAR, 3);
        }

        String endDate = (firstCycleEndDateCalendar.get(Calendar.MONTH) + 1) + "/" + firstCycleEndDateCalendar.get(Calendar.DAY_OF_MONTH) + "/" + firstCycleEndDateCalendar.get(Calendar.YEAR);
        firstCycleEndDateCalendar.add(Calendar.YEAR, -3);
        firstCycleEndDateCalendar.set(Calendar.MONTH, 1);
        firstCycleEndDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

        String startDate = (firstCycleEndDateCalendar.get(Calendar.MONTH) + 1) + "/" + firstCycleEndDateCalendar.get(Calendar.DAY_OF_MONTH) + "/" + firstCycleEndDateCalendar.get(Calendar.YEAR);
        Requirements requirements = getCaliforniaCycleRequirement(25, 4, 1, 1, 19);
        return new Cycle(title, startDate, endDate, startDate, requirements);
    }

    private static Calendar getCaliforniaPeriodEnd(Calendar admissionCalendar, Calendar periodEndCalendar) {
        while (periodEndCalendar.getTime().before(admissionCalendar.getTime())) {
            periodEndCalendar.add(Calendar.YEAR, 3);
        }
        return periodEndCalendar;
    }


    private static Requirements getCaliforniaCycleRequirement(int totalHours, int legalEthicsHours, int biasHours, int competenceIssuesHours, int othersHours) {
        ArrayList<Category> requiredCategories = new ArrayList<>();

        Category ethicsCategory = new Category("Legal ethics", legalEthicsHours);
        Category competIssuesCat = new Category("Elimination of bias", biasHours);
        Category recogCategory = new Category("Competence issues", competenceIssuesHours);
        Category othersCategory = new Category("General", othersHours);

        requiredCategories.add(ethicsCategory);
        requiredCategories.add(competIssuesCat);
        requiredCategories.add(recogCategory);
        requiredCategories.add(othersCategory);

        return new Requirements(totalHours, requiredCategories);
    }


    private static float getDiffInYears(Calendar currentCalendar, Calendar admissionCalendar) {
        float diff = currentCalendar.get(Calendar.YEAR) - admissionCalendar.get(Calendar.YEAR);
        if (admissionCalendar.get(Calendar.MONTH) > currentCalendar.get(Calendar.MONTH) ||
                (admissionCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) && admissionCalendar.get(Calendar.DATE) > currentCalendar.get(Calendar.DATE))) {
            int monthsDiff = admissionCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH);
            int daysDiff = admissionCalendar.get(Calendar.DATE) - currentCalendar.get(Calendar.DATE);
            int totalDays = 30 * monthsDiff + daysDiff;

            diff = diff - (totalDays / 365.0f);

        } else {
            int monthsDiff = currentCalendar.get(Calendar.MONTH) - admissionCalendar.get(Calendar.MONTH);
            int daysDiff = currentCalendar.get(Calendar.DATE) - admissionCalendar.get(Calendar.DATE);
            int totalDays = 30 * monthsDiff + daysDiff;

            diff = diff + (totalDays / 365.0f);
        }
        return diff;
    }

    private static int getDiffInMonths(Calendar advanceCalendar, Calendar startCalendar) {
        int diff = (advanceCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12;
        if (startCalendar.get(Calendar.MONTH) > advanceCalendar.get(Calendar.MONTH) ||
                (startCalendar.get(Calendar.MONTH) == advanceCalendar.get(Calendar.MONTH) && startCalendar.get(Calendar.DATE) > advanceCalendar.get(Calendar.DATE))) {

            int monthsDiff = startCalendar.get(Calendar.MONTH) - advanceCalendar.get(Calendar.MONTH);
            diff = diff - (monthsDiff - 1);

        } else {
            int monthsDiff = advanceCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            diff = diff + monthsDiff;
        }
        return diff;
    }


}
