package dynamicgradecalculator.model;

import java.util.HashMap;
import java.util.Map;

//Main model class for Dynamic Grade Calculator
public class Calculator {
    //Constants
    public static final double MAX_POINTS = 100.0;
    public static final double EPSILON = 0.001;

    //String array of all the attributes being passed in the HTML request object that are of type double in Java
    public static final String[] doubleAttributeNames = {"exam1Weight", "exam2Weight", "finalExamWeight", "labWeight", "quizWeight",
            "exam1Score", "exam2Score", "finalExamScore", "labScore", "quizScore"};
    //String array of all the string attributes (e.g., targetGrade, responses) being passed in the HTML request
    public static final String[] stringAttributeNames = {"targetGrade", "exam1Response", "exam2Response", "finalExamResponse", "labResponse", "quizResponse"};

    //mapOfStrings includes text based responses while mapOfDoubles includes numeric based responses
    public static Map<String, String> calculateGrade(Map<String, String> mapOfStrings, Map<String, Double> mapOfDoubles) {
        String targetGrade = mapOfStrings.get("targetGrade");

        //Assign a default target grade of F if not specified
        if (targetGrade == null || targetGrade.isEmpty()) {
            targetGrade = "F";
        }

        //finalOverallScore represents the minimum score user needs to achieve their target grade
        double finalOverallScore;

        switch (targetGrade.toUpperCase()) {
            case "A":
                finalOverallScore = 90.0;
                break;

            case "B":
                finalOverallScore = 80.0;
                break;

            case "C":
                finalOverallScore = 70.0;
                break;

            case "D":
                finalOverallScore = 60.0;
                break;

            //default includes "F"
            default:
                finalOverallScore = 0.0;
                break;
        }

        //Get percentage weights of items from map
        double exam1Weight = mapOfDoubles.getOrDefault("exam1Weight", 0.0);
        double exam2Weight = mapOfDoubles.getOrDefault("exam2Weight", 0.0);
        double finalExamWeight = mapOfDoubles.getOrDefault("finalExamWeight", 0.0);
        double labWeight = mapOfDoubles.getOrDefault("labWeight", 0.0);
        double quizWeight = mapOfDoubles.getOrDefault("quizWeight", 0.0);

        double weightTotal = exam1Weight + exam2Weight + finalExamWeight + labWeight + quizWeight;

        if (weightTotal - 100.00 > EPSILON) {
            throw new IllegalArgumentException("Weights don't add up to 100. Try again.");
        }

        //Responses/Scores
        //Grade known flags (determine whether to ask for points)
        boolean exam1GradeKnown = false;
        boolean exam2GradeKnown = false;
        boolean finalExamGradeKnown = false;
        boolean labGradeKnown = false;
        boolean quizGradeKnown = false;

        //Item points (grade * weight/100)
        double exam1Points = 0.0;
        double exam2Points = 0.0;
        double finalExamPoints = 0.0;
        double labPoints = 0.0;
        double quizPoints = 0.0;

        //Total remaining item weight (if not all items known).
        double remainingGradeWeight = 0.0;

        //Exam 1
        String exam1Response = mapOfStrings.getOrDefault("exam1Response", "");
        if ("Y".equalsIgnoreCase(exam1Response) || "Yes".equalsIgnoreCase(exam1Response)) {
            exam1GradeKnown = true;
            double exam1Score = mapOfDoubles.getOrDefault("exam1Score", 0.0);

            exam1Points = exam1Score * (exam1Weight / 100.0);
        } else {
            remainingGradeWeight += exam1Weight;
        }

        //Exam 2 (dependent on user knowing exam 1 score)
        if (exam1GradeKnown) {
            String exam2Response = mapOfStrings.getOrDefault("exam2Response", "");

            if ("Y".equalsIgnoreCase(exam2Response) || "Yes".equalsIgnoreCase(exam2Response)) {
                exam2GradeKnown = true;
                double exam2Score = mapOfDoubles.getOrDefault("exam2Score", 0.0);

                exam2Points = exam2Score * (exam2Weight / 100.0);
            } else {
                remainingGradeWeight += exam2Weight;
            }
        } else {
            remainingGradeWeight += exam2Weight;
        }

        //Final exam (dependent on user knowing exam 2 score)
        if (exam2GradeKnown) {
            String finalExamResponse = mapOfStrings.getOrDefault("finalExamResponse", "");

            if ("Y".equalsIgnoreCase(finalExamResponse) || "Yes".equalsIgnoreCase(finalExamResponse)) {
                finalExamGradeKnown = true;
                double finalExamScore = mapOfDoubles.getOrDefault("finalExamScore", 0.0);

                finalExamPoints = finalExamScore * (finalExamWeight / 100.0);
            } else {
                remainingGradeWeight += finalExamWeight;
            }
        } else {
            remainingGradeWeight += finalExamWeight;
        }

        //Labs
        String labResponse = mapOfStrings.getOrDefault("labResponse", "");
        if ("Y".equalsIgnoreCase(labResponse) || "Yes".equalsIgnoreCase(labResponse)) {
            labGradeKnown = true;
            double labScore = mapOfDoubles.getOrDefault("labScore", 0.0);

            labPoints = labScore * (labWeight / 100.0);
        } else {
            remainingGradeWeight += labWeight;
        }

        //Quizzes
        String quizResponse = mapOfStrings.getOrDefault("quizResponse", "");
        if ("Y".equalsIgnoreCase(quizResponse) || "Yes".equalsIgnoreCase(quizResponse)) {
            quizGradeKnown = true;
            double quizScore = mapOfDoubles.getOrDefault("quizScore", 0.0);

            quizPoints = quizScore * (quizWeight / 100.0);
        } else {
            remainingGradeWeight += quizWeight;
        }

        //Calculate current points with either default value of 0 or user provided value.
        double currentPoints = exam1Points + exam2Points + finalExamPoints + labPoints + quizPoints;

        //Calculate total weight of grades known by user.
        double totalKnownGradeWeight = (MAX_POINTS - remainingGradeWeight);

        //Calculate current score based on current points out of current total weight of grades.
        double currentScore = (currentPoints / totalKnownGradeWeight) * 100;

        Map<String, String> results = new HashMap<>();

        //Current score (stored as String)
        //Use string.format to limit double to 2 digits after decimal
        results.put("currentScore", String.format("%.2f", currentScore));

        //Current letter grade
        if (currentScore >= 90) {
            results.put("currentGrade", "A");
        } else if (currentScore >= 80) {
            results.put("currentGrade", "B");
        } else if (currentScore >= 70) {
            results.put("currentGrade", "C");
        } else if (currentScore >= 60) {
            results.put("currentGrade", "D");
        } else {
            results.put("currentGrade", "F");
        }

        //Store outcome in result message
        //Computes grade average needed on remaining items in order to hit target grade (if applicable)
        double avgToTargetLetterGrade;

        //Check if all grades are known
        if (exam1GradeKnown && exam2GradeKnown && finalExamGradeKnown && labGradeKnown && quizGradeKnown) {
            if (currentPoints >= finalOverallScore) {
                results.put("resultMessage", "Congratulations! You received the " + targetGrade + " that you wanted!");
            } else {
                results.put("resultMessage", "Unfortunately, a grade of " + targetGrade + " is not possible.");
            }
        } else {
            if (currentPoints >= finalOverallScore) {
                results.put("resultMessage", "You will receive at least a grade of " + targetGrade + ".");
            } else {
                //Edge case of invalid data: remaining weight of open items is zero and user doesn't enter that grades of all remaining items are known (skips outer if condition)
                if (remainingGradeWeight == 0) {
                    throw new IllegalArgumentException("Some data was invalid. Please try again.");
                }

                avgToTargetLetterGrade = ((finalOverallScore - currentPoints) / remainingGradeWeight) * 100;
                results.put("result", String.format("%.2f", avgToTargetLetterGrade));
            }
        }

        return results;
    }
}
