package com.example.testgenerator.questions;

import com.example.testgenerator.Question;

public class CheckSum extends Question {

    private static final int MIN_LEN = 65;
    private static final int MAX_LEN = 85;
    private static final int SIZE_RNG = MAX_LEN - MIN_LEN;

    private static final char HIGH_RNG = 'z';
    private static final char LOW_RNG = 'a';

    public CheckSum() {
        SubquestionTypes.add("BITWISE");
    }

    @Override
    public String getName() {
        return "CheckSum";
    }

    @Override
    public String getHeader() {
        String msg = "<h3>Checksum</h3><p>One of the problem solving question blocks in this paper is based on CheckSums. "
                + "You can find (and should already have read) background information on Checksums in the January Assessment Information module "
                + "on Canvas.</p><p>NOTE there are some fragments of code in the algorithms below, but you are responsible for implementing them in Java"
                + "<h4>Simple CheckSum Algorithm</h4><p>The simple checksum algorithm is as follows:</p>"
                +"<ul>"
                +"<li>We assume you are computing the checksum for a String called str.</li>"
                + "<li>Declare a variable k of type long and initialise it to 7</li>"
                + "<li>For each character in the string str:</li>"
                + "<ul><li>set k to k multiplied by 23</li>"
                + "<li>Add the ith character in str to k (use str.charAt(i) to get the ith character)</li>"
                + "<li>Set k to k multiplied by 13</li>"
                + "<li>Set k to the remainder of dividing k by 1000000009</li>"
                + "</ul><li>At the end of this loop the checksum is stored in k</li></ul>"
                + "<h4>Bitwise Checksum Algorithm</h4><p>The bitwise checksum algorithm is as follows:"
                + "<ul><li>We assume you are computing the checksum for a string called str</li>"
                + "<li>Declare a variable c of type byte and initialise it to 0</li>"
                + "<li>Turn the string str into a byte array (str.getBytes() will return str as an array of bytes);"
                + "<li>For each byte - curByte - in the array input:"
                + "<ul><li>Set c as follows: c = (byte) (((c & 255) >>> 1) + ((c & 1) &lt;&lt; 7));</li>"
                + "<li>Now set c as follows: c = (byte) ((c + curByte) & 255);</li>"
                + "</ul><li>At the end of this loop c now contains the one byte checksum</li></ul>"
                + "<p>Remember: cut-and-paste long strings from the questions; do not try to type them in.</p>";
        return msg;
    }

    @Override
    public String getQuestionVariable() {
        return utils.genRandomString(MIN_LEN, SIZE_RNG, LOW_RNG, HIGH_RNG, false);
    }

    @Override
    // Checksum value
    public String getCorrectAnswer(Object input) {
        if (input instanceof String) {
            String str = (String) input;

            long k = 7;//7
            for (int i = 0; i < str.length(); i++) {
                k *= 23;//23
                k += str.charAt(i);
                k *= 13;//13
                k %= 1000000009;
            }

            return Long.toString(k);
        } else {
            throw new IllegalArgumentException("Expected String argument, but got: " + input.getClass().getName());
        }
    }

    @Override
    public String getWrongAnswer(String answer) {
        long topRange = Long.parseLong(answer) * 2;
        long btmRange = Long.parseLong(answer) / 2;
        return Long.toString(utils.genRandomRangeLong(btmRange, topRange));
    }

    @Override
    public String getReverseWrongAnswer(String answer) {
        return utils.genRandomString(answer.length(), 0, LOW_RNG, HIGH_RNG, false);
    }

    @Override
    public String getSpecificSubQuestion(String subquestion, int qNum) throws IllegalArgumentException {
        if (!SubquestionTypes.contains(subquestion)) {
            throw new IllegalArgumentException("Invalid Subquestion");
        }

        String baseString = getQuestionVariable();
        String questionText = null;
        switch (subquestion) {
            case "BASE":
                questionText = "What is the result of running the Simple Checksum algorithm on the string ";
                break;
            case "REVERSE":
                questionText = "Which of the following strings generates the simple checksum ";
                break;
            case "PAIRS":
                questionText = "Which of the following pairs represents a string and it's simple checksum";
                break;
            case "BITWISE":
                return generateBitwiseSubquestion(baseString, qNum);
            default:
                break;
        }
        if (questionText == null) {
            return "Subquestion type doesn't exist";
        }
        return setupDefaultSubquestion(baseString, qNum, 8, questionText, subquestion);
    }

    public String generateBitwiseSubquestion(String answer, int qNum) {
        byte question = getBitwise(answer);

        String questionWording = qNum + ". Checksum: Which of the following strings generates the bitwise checksum " + question + "?<font style=\"display:none\">" + utils.genRandomRange(1000000, 9999999) + "</font>";

        int ansLoc = utils.genRandomRange(0, 4);
        String[] qSet = generateMultipleChoices(String.valueOf(question), answer, ansLoc, "REVERSE");

        if (utils.testNotUnique(qSet)) {
            System.out.println("***ERROR Qset Checksum simple string Not Unique");
        }

        return generateSubquestion(qNum, 8, questionWording, qSet, ansLoc);
    }

    public byte getBitwise(String input) {
        byte[] in = input.getBytes();
        byte checksum = 0;

        for (byte cur_byte : in) {
            checksum = (byte) (((checksum & 255) >>> 1) + ((checksum & 1) << 7));
            checksum = (byte) ((checksum + cur_byte) & 255);
        }

        return checksum;
    }
}


